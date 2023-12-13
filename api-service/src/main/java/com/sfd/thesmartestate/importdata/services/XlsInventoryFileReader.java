package com.sfd.thesmartestate.importdata.services;

import com.sfd.thesmartestate.importdata.dto.XlsInventoryRowDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class XlsInventoryFileReader {
    public static final int TOTAL_COLUMNS_ALLOWED = 12;

    // Return List of Leads by reading from the Excel File
    public List<XlsInventoryRowDto> getInventoryRows(MultipartFile multipartFile, List<String> errors) {

        List<XlsInventoryRowDto> inventoryList = new ArrayList<>();
        try {

            String fileName = multipartFile.getOriginalFilename();
            // Create Workbook instance for xlsx/xls file input stream
            Workbook workbook = null;
            assert fileName != null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(multipartFile.getInputStream());
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(multipartFile.getInputStream());
            }
            assert workbook != null;
            int sheets = workbook.getNumberOfSheets();
            for (int i = 0; i < sheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                String[] availableSize = new String[0];
                boolean[] cornerFlat = new boolean[0];
                int rowNumber = 0;
                String towerName = "";
                for (Row row : sheet) {
                    try {
                        if (rowNumber == 0) {
                            rowNumber++;
                            continue;
                        }
                        if (rowNumber == 1) {
                            //Tower name field
                            towerName = getCellValue(row.getCell(0)).trim().replaceAll(" ", "");
                            rowNumber++;
                            continue;
                        }
                        if (rowNumber == 2) {
                            // size field
                            availableSize = fetchAvailableSizes(row, errors);
                            rowNumber++;
                            continue;
                        }
                        if (rowNumber == 3) {
                            //flat corner details
                            cornerFlat = fetchCornerFlat(row);
                            rowNumber++;
                            continue;
                        }
                        if (isEmptyRow(row)) {
                            continue;
                        }

                        for (int index = 0; index < row.getLastCellNum(); index++) {
                            XlsInventoryRowDto inventoryDto = new XlsInventoryRowDto();
                            Cell cell = row.getCell(index);
                            inventoryDto.setSize(availableSize[index]);
                            inventoryDto.setInventoryName(getCellValue(cell));
                            inventoryDto.setCorner(cornerFlat[index]);
                            inventoryDto.setTower(towerName);
                            inventoryList.add(inventoryDto);
                        }
                        rowNumber++;
                    } catch (Exception e) {
                        errors.add("Error on row -" + rowNumber + "-" + e.getMessage());
                        log.error("Error --- {}", e.getLocalizedMessage());
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return inventoryList;
    }

    private String[] fetchAvailableSizes(Row row, List<String> errors) {

        String[] sizes = new String[row.getLastCellNum()];
        for (int cn = 0; cn < row.getLastCellNum(); cn++) {
            Cell cell = row.getCell(cn);
            try {
                sizes[cn] = getCellValue(cell);
            } catch (Exception e) {
                errors.add("Value not acceptable -" + cell.toString());
            }
        }
        return sizes;
    }

    private boolean[] fetchCornerFlat(Row row) {
        boolean[] corner = new boolean[row.getLastCellNum()];
        for (int cn = 0; cn < row.getLastCellNum(); cn++) {
            Cell cell = row.getCell(cn);
            String cornerValue = getCellValue(cell);
            if (cornerValue.toLowerCase().contains("corner")) {
                corner[cn] = true;
            }
        }
        return corner;
    }

    // provides the cell value in string
    private String getCellValue(Cell cell) {

        String cellValue = "";
        if (cell == null) {
            return "";
        }
        try {
            if (CellType.NUMERIC == cell.getCellType()) {
                cellValue = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                return String.valueOf(Double.valueOf(cellValue).longValue());
            } else {
                cellValue = cell.getStringCellValue().stripLeading().stripTrailing();
            }
        } catch (Exception e) {
            log.info("Error in data - {}", e.getMessage());
            e.printStackTrace();
        }
        return cellValue;
    }


    boolean isEmptyRow(Row row) {
        boolean isEmptyRow = true;
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.hasText(cell.toString())) {
                isEmptyRow = false;
            }
        }
        return isEmptyRow;
    }
}
