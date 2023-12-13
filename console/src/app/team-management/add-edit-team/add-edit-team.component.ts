import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, FormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { MessageService } from 'src/app/core/service/message.service';
import { TeamService } from '../team.service';
import { ConstantConfig } from '../../config/constant.config';
import { ValidateEmail, ValidatePhoneNumber } from '../../core/Validators/index';
import { ProjectManagementService } from 'src/app/project-management/project-management.service';
import { StorageService } from 'src/app/core/service/storage.service';


@Component({
  selector: 'app-add-edit-team',
  templateUrl: './add-edit-team.component.html',
  styleUrls: ['./add-edit-team.component.scss']
})
export class AddEditTeamComponent implements OnInit {
  teamId: any;
  projectId: any;
  team: any;
  teamForm!: UntypedFormGroup;
  projects: any = [];
  users: any = [];
  supervisorId: any;
  userdata = [];
  userfilteredData = [];
  userId: any;
  usersToBeAdded = [];
  isSubmitted: boolean = false;
  isUserSubmitted: boolean = false;
  isLeadSubmitted: boolean = false;
  @ViewChild(DatatableComponent, { static: false }) userTable: DatatableComponent;

  //mask
  phoneMask = ConstantConfig.PHONE_MASK;

  constructor(
    private route: ActivatedRoute,
    private fb: UntypedFormBuilder,
    private modalService: NgbModal,
    private teamService: TeamService,
    private msgService: MessageService,
    private projectService: ProjectManagementService,
  ) {
    this.createTeamForm(null);
    this.route.queryParams.subscribe(params => {
      if (params["teamId"]) {
        this.teamId = params["teamId"];
        this.projectId = params["projectId"];
      }
    });

  }

  ngOnInit(): void {
    this.teamService.requestDataFromMultipleSources(this.teamId, this.projectId).subscribe(
      resList => {
        this.team = resList[0];
        this.projects = resList[1];
        this.users = resList[2];
        this.setData(this.team);
        this.createTeamForm(this.team);
        this.removeAlreadyAddedUsers(this.team.members, this.users);
      }
    )
  }

  setData(team) {
    this.supervisorId = team?.supervisor?.id;
    this.userdata = team?.members;
  }

  // creating team form with data
  createTeamForm(p) {
    this.teamForm = this.fb.group({
      id: [p == null ? null : p.id],
      name: [p == null ? null : p.name, Validators.required],
      project: [p == null ? null : p.project.id, Validators.required],
    })
  }

  // on project form submit
  onTeamSave(form: UntypedFormGroup) {
    this.isSubmitted = true;

    if (form.valid) {

      let params = form.value;
      const project = this.projects.filter(ele => {
        return ele.id = params.project
      })
      params.project = project[0];
      if (this.team.name === params.name) {
        this.teamService.updateOnlyProject(this.team.id, this.team.project.id, params.project.id).subscribe(
          res => {
            this.msgService.showSuccess("Project Updated successfully.")
            this.team = res;
            this.setData(this.team);
            this.isSubmitted = false;
          }
        )
      } else {
        this.team.name = params.name;
        this.teamService.updateTeam(this.team).subscribe(
          res => {
            this.msgService.showSuccess("Team Updated successfully.")
            this.team = res;
            this.setData(this.team);
            this.isSubmitted = false;
          }
        )
      }
    }
  }

  // Opening Add User Model
  addRow(content) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  submitLead() {
    this.isLeadSubmitted = true;
    if (this.team?.supervisor == null) {
      this.teamService.addTeamLead(this.team.id, this.supervisorId).subscribe(
        res => {
          this.team = res;
          this.setData(this.team);
          this.removeAlreadyAddedUsers(this.team.members, this.users);
          this.msgService.showSuccess("Team Lead added successfully");
          this.isLeadSubmitted = false;
        }
      )
    } else {
      if (this.team.supervisor.id != this.supervisorId) {
        this.teamService.changeTeamLead(this.team.id, this.team.supervisor.id, this.supervisorId).subscribe(
          res => {
            this.team = res;
            this.setData(this.team);
            this.removeAlreadyAddedUsers(this.team.members, this.users);
            this.msgService.showSuccess("Team Lead Updated successfully");
            this.isLeadSubmitted = false;
          }
        )
      } else {
        this.msgService.showError("Please select a different Team Lead")
        this.isLeadSubmitted = false;
      }
    }
  }

  submitUser() {
    this.isUserSubmitted = true;
    this.teamService.addTeamMember(this.team.id, this.userId).subscribe(
      res => {
        this.team = res;
        this.setData(this.team);
        this.removeAlreadyAddedUsers(this.team.members, this.users);
        this.msgService.showSuccess("Member added successfully");
        this.modalService.dismissAll();
        this.isUserSubmitted = false;
      }
    )
  }

  deleteMember(row) {
    if (confirm("Are you sure you want to remove " + row.name)) {
      this.teamService.removeTeamMember(this.team.id, row.id).subscribe(
        res => {
          this.team = res;
          this.setData(this.team);
          this.removeAlreadyAddedUsers(this.team.members, this.users);
          this.msgService.showSuccess("Member removed successfully");
        }
      )
    }
  }

  removeAlreadyAddedUsers(myFilter, myArray) {
    this.usersToBeAdded = myArray.filter((el) => {
      return myFilter.every((f) => {
        return f.id != el.id
      });
    });
  }

}
