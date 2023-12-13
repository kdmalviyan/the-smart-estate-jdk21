package com.sfd.thesmartestate.importdata.services;

import com.sfd.thesmartestate.importdata.dto.XlsLeadRowDto;
import com.sfd.thesmartestate.lms.exceptions.LeadException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class XlsLeadFileReader {
    public static final int TOTAL_COLUMNS_ALLOWED = 12;

    // Return List of Leads by reading from the Excel File
    public List<XlsLeadRowDto> getLeadRows(MultipartFile file) {

        List<XlsLeadRowDto> leadList = new ArrayList<>();
        try {

            String fileName = file.getOriginalFilename();
            assert Objects.nonNull(fileName);
            // Create Workbook instance for xlsx/xls file input stream
            Workbook workbook = null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }

            assert workbook != null;
            Sheet sheet = workbook.getSheetAt(0);

            int rowNumber = 0;

            for (Row row : sheet) {
                if (rowNumber == 0) {
                    if (row.getLastCellNum() != TOTAL_COLUMNS_ALLOWED) {
                        throw new LeadException("Please check all required columns(Size,Budget,Customer Name,Customer Email,Phone,Alternative Contact,Lead Source,Project,Lead Status,Lead Type,Latest Remark,Assigned to) are there");
                    }
                    rowNumber++;
                    continue;
                }
                if (isEmptyRow(row)) {
                    continue;
                }
                int cid = 0;
                XlsLeadRowDto lead = new XlsLeadRowDto();
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    Cell cell = row.getCell(cn);
                    setLeadProperties(cid, lead, cell);
                    cid++;
                }
                leadList.add(lead);
                rowNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leadList;
    }

    private void setLeadProperties(int cid, XlsLeadRowDto lead, Cell cell) {
        switch (cid) {
            case 0:
                lead.setSize(getCellValue(cell));
                break;
            case 1:
                lead.setBudget(getCellValue(cell));
                break;
            case 2:
                lead.setCustomerName(getCellValue(cell));
                break;
            case 3:
                lead.setCustomerEmail(getCellValue(cell));
                break;
            case 4:
                lead.setCustomerPhone(getCellValue(cell));
                break;
            case 5:
                lead.setCustomerAlternatePhone(getCellValue(cell));
                break;
            case 6:
                lead.setLeadSource(getCellValue(cell));
                break;
            case 7:
                lead.setProjectName(getCellValue(cell));
                break;
            case 8:
                lead.setLeadStatus(getCellValue(cell));
                break;
            case 9:
                lead.setLeadType(getCellValue(cell));
                break;
            case 10:
                lead.setComment(getCellValue(cell));
                break;
            case 11:
                lead.setAssignTo(getCellValue(cell));
                break;

            default:
                break;
        }
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
            } else {
                cellValue = cell.getStringCellValue().stripLeading().stripTrailing();
            }
        } catch (Exception e) {
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

    public List<XlsLeadRowDto> getRawLeadRows(MultipartFile file) {

        List<XlsLeadRowDto> leadList = new ArrayList<>();
        try {

            String fileName = file.getOriginalFilename();
            assert Objects.nonNull(fileName);
            // Create Workbook instance for xlsx/xls file input stream
            Workbook workbook = null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }

            assert workbook != null;
            Sheet sheet = workbook.getSheetAt(0);

            int rowNumber = 0;

            for (Row row : sheet) {
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                if (isEmptyRow(row)) {
                    continue;
                }
                int cid = 0;
                XlsLeadRowDto lead = new XlsLeadRowDto();
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    Cell cell = row.getCell(cn);
                    setRawLeadProperties(cid, lead, cell);
                    cid++;
                }
                leadList.add(lead);
                rowNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leadList;
    }

    private void setRawLeadProperties(int cid, XlsLeadRowDto lead, Cell cell) {
        switch (cid) {
            case 0:
                lead.setCustomerPhone(getCellValue(cell));
                break;
            case 1:
                lead.setCustomerName(getCellValue(cell));
                break;
            case 2:
                lead.setCustomerEmail(getCellValue(cell));
                break;
            case 3:
                lead.setProjectName(getCellValue(cell));
                break;
            case 4:
                lead.setComment(getCellValue(cell));
                break;
            default:
                break;
        }
    }
}
