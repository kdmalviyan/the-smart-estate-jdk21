import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProjectManagementService } from '../project-management.service';
import * as FileSaver from 'file-saver';
import { ColDef } from 'ag-grid-community';

@Component({
  selector: 'app-view-project',
  templateUrl: './view-project.component.html',
  styleUrls: ['./view-project.component.sass']
})
export class ViewProjectComponent implements OnInit {
  //--------------ag-grid----------------
  public defaultColDef: ColDef = {
    flex: 1,
    sortable: true,
    filter: true,
  };
  flatcolumns = [
    { headerName: 'Name', field: 'name', sortable: true, cellStyle: function (params) { return setRowColor(params) } },
    { headerName: 'Size', field: 'size', sortable: true, cellStyle: function (params) { return setRowColor(params) } },
    //{headerName: 'Facing', field: 'facing',sortable: true },
    { headerName: 'Corner', field: 'corner', sortable: true, cellStyle: function (params) { return setRowColor(params) } },
    //{headerName: 'Selling Price', field: 'sellingPrice',sortable: true},
    { headerName: 'Tower', field: 'tower', sortable: true, cellStyle: function (params) { return setRowColor(params) } },
    {
      headerName: 'Status', field: 'inventoryStatus.description', sortable: true,
      cellStyle: function (params) { return setRowColor(params) }
    }
  ];
  //--------------ag-grid----------------
  projectData: any;
  constructor(public viewProjectdialogRef: MatDialogRef<ViewProjectComponent>,
    private projectManagmentService: ProjectManagementService,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.projectData = data;
  }

  ngOnInit(): void {
  }


  close(): void {
    this.viewProjectdialogRef.close();
  }


  downloadFile(event) {
    this.projectManagmentService.downloadProjectFile(event.id).subscribe(
      (res: any) => {
        const data = new Blob([res])
        FileSaver.saveAs(data, this.getFileName(event.fileName));
      }
    )
  }

  getFileName(fileName) {
    if (fileName != null) {
      return fileName.split('^')[1];
    }
  }
}
function setRowColor(params: any) {
  if (params.node.data.inventoryStatus.name == "AVAILABLE") {
    return { color: 'green' };
  }
  else if (params.node.data.inventoryStatus.name == "BOOKED") {
    return { color: 'red' };
  }
  else if (params.node.data.inventoryStatus.name == "ON_HOLD") {
    return { color: 'orange' };
  } else {
    return null;
  }
}