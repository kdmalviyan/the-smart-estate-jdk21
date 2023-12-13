import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MessageService } from 'src/app/core/service/message.service';
import { StorageService } from 'src/app/core/service/storage.service';
import { UserManagementService } from 'src/app/user-management/user-management.service';
import { LeadManagementService } from '../../lead-management.service';

@Component({
  selector: 'app-project-change',
  templateUrl: './project-change.component.html',
  styleUrls: ['./project-change.component.sass']
})
export class ProjectChangeComponent implements OnInit {


  //deactivationReasons = [];

  changeEvent: any;
  leadId: any;
  metadata: any;
  users: any;
  leadDetails: any;
  changeProjectForm: UntypedFormGroup;
  constructor(
    private storageService: StorageService,
    private leadManagementService: LeadManagementService,
    private messageService: MessageService,
    private userService: UserManagementService,
    private fb: UntypedFormBuilder,
    public dialogRef: MatDialogRef<ProjectChangeComponent>,
    @Inject(MAT_DIALOG_DATA) public data) {
    this.leadDetails = data.leadDetails;
    this.changeEvent = data.changeProject;
    this.leadId = data.leadId;
    dialogRef.disableClose = true;

  }

  ngOnInit(): void {
    this.metadata = this.storageService.getStorage();
    this.userService.getUsersByProject(this.changeEvent).subscribe(res => {
      this.users = res;
    });

    this.changeProjectForm = this.fb.group({
      userdetail: new UntypedFormControl('', [Validators.required])
    });
  }

  submit() {

    const selectedProject = this.metadata.projects.filter(e => e.id === this.changeEvent)[0];
    this.leadDetails.project = selectedProject;
    this.leadDetails.assignedTo = this.changeProjectForm.value.userdetail;
    
    let projectchage={
      'leadId':this.leadDetails.id,
      'projectId':this.leadDetails.project.id,
      'userId':this.leadDetails.assignedTo.id
    }
    this.leadManagementService.updateProject(projectchage).subscribe(res => {
      //this.leadDetails.assignedTo = this.changeProjectForm.value.userdetail;
      //this.changeAssigne();
      this.dialogRef.close();
      this.messageService.showSuccess('Project changed to ' + res.project.name + 'and Assignee changed to ' +  this.leadDetails.assignedTo.name + ' Successfully');
    });
  }

  changeAssigne() {
    this.leadManagementService.updateLead(this.leadDetails, 'USER_ASSIGNED').subscribe(res => {
      //this.messageService.showSuccess('Assignee changed to ' + res.assignedTo.name + ' Successfully');
    });
  }

}
