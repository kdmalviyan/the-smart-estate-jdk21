import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { ColDef } from 'ag-grid-community';
import * as FileSaver from 'file-saver';
import { MessageService } from 'src/app/core/service/message.service';
import { ConstantConfig } from '../../config/constant.config';
import { ValidateEmail } from '../../core/Validators/index';
import { AddEditInventoryComponent } from '../add-edit-inventory/add-edit-inventory.component';
import { ProjectManagementService } from '../project-management.service';
import { UploadInventoryComponent } from '../upload-inventory/upload-inventory.component';

@Component({
  selector: 'app-add-edit-project',
  templateUrl: './add-edit-project.component.html',
  styleUrls: ['./add-edit-project.component.scss']
})
export class AddEditProjectComponent implements OnInit {

  action: String;
  project: any;
  projectForm!: UntypedFormGroup;
  accept = ".pdf";
  // Flat Data

  //--------------ag-grid----------------
  public defaultColDef: ColDef = {
    flex: 1,
    sortable: true,
    filter: 'agTextColumnFilter',
    // enable floating filters by default
    floatingFilter: true,
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
  inventoriesData = [];
  flatfilteredData = [];
  @ViewChild(DatatableComponent, { static: false }) flatTable: DatatableComponent;
  uploadForm!: UntypedFormGroup;
  projectId;
  files = [];
  // edit data
  isReadOnly: boolean = false;
  isEdit: boolean = false;


  //mask
  phoneMask = ConstantConfig.PHONE_MASK;

  constructor(
    private route: ActivatedRoute,
    private fb: UntypedFormBuilder,
    public inventoryAddDialog: MatDialog,
    private projectService: ProjectManagementService,
    private msgService: MessageService
  ) {
    this.route.queryParams.subscribe(params => {
      if (params["projectId"]) {
        this.projectId = params["projectId"];
      }
    });

    this.createProjectForm(null);
  }

  ngOnInit(): void {
    this.project = this.projectService.getEditData();
    if (this.project == null) {
      this.getProjectData();
    } else {
      this.setData(this.project);
      this.createProjectForm(this.project);
    }
    this.createProjectBrochuerForm(this.project);
  }

  createProjectBrochuerForm(project) {
    this.uploadForm = this.fb.group({
      brochurePath: [project?.brochurePath == null ? null : project.brochurePath.id]
    });
  }

  getProjectData() {
    this.projectService.getProjectById(this.projectId).subscribe(
      res => {
        this.project = res;
        this.setData(this.project);
        this.createProjectForm(this.project);
      }
    )
  }

  setData(project) {
    this.inventoriesData = project?.inventories;
    this.files = [];
    if (project?.brochure) {
      this.files.push(project?.brochure);
    }
    if (project?.priceFiles) {
      project.priceFiles.forEach(ele => {
        this.files.push(ele);
      });
    }
  }

  // creating project form with data
  createProjectForm(project) {
    this.projectForm = this.fb.group({
      id: [project == null ? null : project.id],
      name: [project == null ? null : project.name, Validators.required],
      email: [project == null ? null : project.email, [Validators.email, ValidateEmail]],
      address: [project == null ? null : project.address, Validators.required],
      phone: [project == null ? null : project.phone],
      flats: [project == null ? null : project.flats],
      broucer: [project == null ? null : project.broucer],
      pricelist: [project == null ? null : project.pricelist],
      enabled: [project == null ? true : project.enabled],
      brochure: this.fb.group({
        fileName: [project?.brochure == null ? null : project.brochure.fileName],
        filePath: [project?.brochure == null ? null : project.brochure.filePath],
      })

    })
  }



  // on project form submit
  onProjectSave(form: UntypedFormGroup) {
    if (form.valid) {

      let params = form.value;
      params.inventories = this.inventoriesData;
      this.projectService.updateProject(params).subscribe(
        res => {
          this.msgService.showSuccess("Project Edited successfully.")
          this.project = res;
        }
      )
    }
  }

  // Opening Add Flat Model
  addRow() {
    const dialogRef = this.inventoryAddDialog.open(AddEditInventoryComponent, {
      width: '600px',
      data: {
        projectId: this.projectId,
        action: 'add'
      },
      disableClose: true
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.msgService.showSuccess("Inventory added successfully.")
      }
    });

  }


  // Opening Edit Flat Model
  editRow(row) {
    /*   this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
      this.createFlatForm(row); */

    const dialogRef = this.inventoryAddDialog.open(AddEditInventoryComponent, {
      width: '600px',
      data: {
        projectId: this.projectId,
        inventory: row,
        action: 'add'
      },
      disableClose: true
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.msgService.showSuccess("Inventory Edited successfully.")
      }
    });
  }


  //make project form readlonly
  readonly() {
    this.isReadOnly = !this.isReadOnly;
  }

  deleteRow(row) {
    this.inventoriesData = this.inventoriesData.filter(function (element) {
      return element.id != row.id;
    });
  }

  uploadFile(event) {
    this.projectService.uploadFiles(this.projectId, event).subscribe(
      res => {
        this.project = res;
        this.setData(res);
        this.msgService.showSuccess("File Uploaded successfully.")
      }
    )
  }

  downloadFile(event) {
    this.projectService.downloadProjectFile(event.id).subscribe(
      (res: any) => {
        const data = new Blob([res])
        FileSaver.saveAs(data, this.getFileName(event.fileName));
      }
    )
  }

  deleteFile(event) {
    if (confirm(" Are you sure you want to delete this file!!")) {
      this.projectService.deleteFile(this.projectId, event.id).subscribe(
        (res) => {
          this.project = res;
          this.setData(res);
          this.msgService.showSuccess("Project file deleted successfully.")
        }
      )
    }
  }

  getFileName(fileName) {
    if(fileName.split('^')[1]==undefined){
      return fileName;
    }
    return fileName.split('^')[1];
  }


  // Opening Add Flat Model
  uploadInventory() {
    const dialogRef = this.inventoryAddDialog.open(UploadInventoryComponent, {
      width: '600px',
      data: {
        projectId: this.projectId,
        action: 'add'
      },
      disableClose: true
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.msgService.showSuccess("Inventory Uplaoded successfully.")
        var inventoriesAdded=result.inventories;
        //this.inventoriesData.unshift(inventoriesAdded);
        this.inventoriesData = [...inventoriesAdded];
      }
    });

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