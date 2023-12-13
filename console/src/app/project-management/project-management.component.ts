import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import {
  UntypedFormBuilder,
  UntypedFormControl, UntypedFormGroup, Validators
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { NavigationExtras, Router } from '@angular/router';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { ConstantConfig } from 'src/app/config/constant.config';
import { FilterService } from 'src/app/core/service/filter.service';
import { MessageService } from 'src/app/core/service/message.service';
import { ValidateEmail } from 'src/app/core/Validators';
import { ProjectManagementService } from './project-management.service';
import { ViewProjectComponent } from './view-project/view-project.component';
// declare const M: any;
interface Gender {
  id: string;
  value: string;
}

@Component({
  selector: 'app-project-management',
  templateUrl: './project-management.component.html',
  styleUrls: ['./project-management.component.scss']
})
export class ProjectManagementComponent implements OnInit {

  @ViewChild('roleTemplate', { static: true }) roleTemplate: TemplateRef<any>;
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;
  rows = [];
  newUserImg = 'assets/images/user/user1.jpg';
  data: any | null;
  filteredData: any | null;
  editForm: UntypedFormGroup;
  register: UntypedFormGroup;
  selectedOption: string;
  loadingIndicator = true;
  projectForm!: UntypedFormGroup;
  isSubmitted: boolean = false;

  //mask
  phoneMask = ConstantConfig.PHONE_MASK;

  constructor(
    private fb: UntypedFormBuilder,
    private modalService: NgbModal,
    public viewProjectDialog: MatDialog,
    private router: Router,
    private projectService: ProjectManagementService,
    private messageService: MessageService,
    private filterService: FilterService
  ) {
    this.editForm = this.fb.group({
      id: new UntypedFormControl(),
      img: new UntypedFormControl(),
      firstName: new UntypedFormControl(),
      lastName: new UntypedFormControl(),
      phone: new UntypedFormControl(),
      email: new UntypedFormControl(),
      address: new UntypedFormControl()
    });

    this.createProjectForm(null);
  }

  ngOnInit() {
    this.getProjects();
  }

  refresh() {
    this.getProjects();
  }

  getProjects() {
    this.projectService.getAllProjects().subscribe(
      (res: any) => {
        this.data = res;
        this.filteredData = [...this.data];
        this.loadingIndicator = false;
      }, err => {
        this.loadingIndicator = false;
      }
    )
  }

  createProjectForm(p) {
    this.projectForm = this.fb.group({
      name: [p == null ? null : p.name, Validators.required],
      email: [p == null ? null : p.email, [Validators.email, ValidateEmail]],
      address: [p == null ? null : p.address, Validators.required],
      phone: [p == null ? null : p.phone],
      enabled: [true]
    })
  }

  editRow(row, rowIndex, content) {
    this.projectService.setEditData(row);
    let navigationExtras: NavigationExtras = {
      queryParams: {
        projectId: row?.id
      }
    };
    this.router.navigate(['/project-management/edit-project'], navigationExtras);
  }

  addRow(content) {
    let ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false,
      ariaLabelledBy: 'modal-basic-title'
    };
    this.modalService.open(content, ngbModalOptions);
  }

  arrayRemove(array, id) {
    return array.filter(function (element) {
      return element.id != id;
    });
  }

  onAddRowSave(form: UntypedFormGroup) {
    this.isSubmitted = true;
    this.projectService.createProject(form.value).subscribe(
      res => {
        this.data.unshift(res);
        this.data = [...this.data];
        this.filteredData = [...this.data];
        this.modalService.dismissAll();
        this.messageService.showSuccess("Project Added Successfully");
        this.isSubmitted = false;
      }
    )
  }

  filterDatatable(event) {
    this.data = this.filterService.filter(event, this.filteredData, this.data);
    // whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }

  changeStatus(event, row) {
    let project = row;
    project.enabled = event.checked;

    this.projectService.updateProject(project).subscribe((res: any) => {
      var data: any;
      data = res;
      if (data.isActive) {
        this.messageService.showSuccess("Project " + data.name + " enabled successfully")
      } else {
        this.messageService.showSuccess("Project " + data.name + "  disabled successfully")
      }

    });
  }


  viewProject(event) {
    this.viewProjectDialog.open(ViewProjectComponent, {
      width: '750px',
      height: '450px',
      data: event
    });
  }
}

