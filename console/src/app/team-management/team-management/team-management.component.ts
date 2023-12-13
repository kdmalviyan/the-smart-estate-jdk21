import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import {
  UntypedFormGroup,
  UntypedFormBuilder,
  UntypedFormControl,
  Validators
} from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { NavigationExtras, Router } from '@angular/router';
import { TeamService } from '../team.service';
import { MessageService } from 'src/app/core/service/message.service';
import { ProjectManagementService } from 'src/app/project-management/project-management.service';
import { FilterService } from 'src/app/core/service/filter.service';

@Component({
  selector: 'app-team-management',
  templateUrl: './team-management.component.html',
  styleUrls: ['./team-management.component.sass']
})
export class TeamManagementComponent implements OnInit {

  @ViewChild('roleTemplate', { static: true }) roleTemplate: TemplateRef<any>;
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;
  rows = [];
  newUserImg = 'assets/images/user/user1.jpg';
  data: any | null;
  filteredData: any | null;
  teamForm: UntypedFormGroup;
  selectedOption: string;
  loadingIndicator = true;
  projects: any = [];

  constructor(
    private fb: UntypedFormBuilder,
    private _snackBar: MatSnackBar,
    private modalService: NgbModal,
    private router: Router,
    private teamService: TeamService,
    private messageService: MessageService,
    private projectService: ProjectManagementService,
    private filterService: FilterService
  ) {
    this.createTeamForm();
  }

  ngOnInit() {
    this.getTeams();
    this.getProjects();
  }

  createTeamForm() {
    this.teamForm = this.fb.group({
      name: new UntypedFormControl('', Validators.required),
      project: new UntypedFormControl('', Validators.required),
    });
  }

  refresh() {
    this.getTeams();
  }

  getTeams() {
    this.teamService.getAllTeams().subscribe(
      (res: any) => {
        this.data = res;
        this.filteredData = [...this.data];
        this.loadingIndicator = false;
      }, err => {
        this.loadingIndicator = false;
        throw err;
      }
    )
  }

  editRow(row, rowIndex, content) {
    this.teamService.setEditData(row);
    let navigationExtras: NavigationExtras = {
      queryParams: {
        "teamId": row.id,
        "projectId": row.project.id
      }
    };
    this.router.navigate(['/team-management/add-team'], navigationExtras);
  }

  addRow(content) {
    let ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false,
      ariaLabelledBy: 'modal-basic-title'
    };
    this.modalService.open(content, ngbModalOptions);
  }

  deleteRow(row, index) {
    if (confirm(`Are you sure you want to delete ${row.name}.?`)) {
      this.teamService.deactivateTeam(row.id).subscribe(
        res => {
          this.messageService.showSuccess("Team Deleted Successfully");
          this.getTeams();

        }
      )
    }
  }

  arrayRemove(array, id) {
    return array.filter(function (element) {
      return element.id != id;
    });
  }

  onAddRowSave(form: UntypedFormGroup) {
    const params = form.value;
    this.teamService.createTeam(params).subscribe(
      res => {
        this.data.push(res);
        this.data = [...this.data]
        this.filteredData = [...this.data]
        this.messageService.showSuccess("Team created successfully");
        form.reset();
        this.modalService.dismissAll();
      }, err => {
        this.messageService.showError("Error in creating Team");
        this.modalService.dismissAll();
      }
    )
  }

  filterDatatable(event) {
    this.data = this.filterService.filter(event, this.filteredData, this.data);
    // whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }

  getId(min, max) {
    // min and max included
    return Math.floor(Math.random() * (max - min + 1) + min);
  }

  changeStatus(event, row) {
    let project = row;
    project.enabled = event.checked;

    this.teamService.updateTeam(project).subscribe((res: any) => {
      var data: any;
      data = res;
      if (data.isActive) {
        this.messageService.showSuccess("Project " + data.name + " enabled successfully")
      } else {
        this.messageService.showSuccess("Project " + data.name + "  disabled successfully")
      }

    });
  }

  //getting projects
  getProjects() {
    this.projectService.getAllProjectsWithMinimalDetails().subscribe(
      res => {
        this.projects = res;
      }
    )
  }

}
