import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ConstantConfig } from 'src/app/config/constant.config';
import { MessageService } from 'src/app/core/service/message.service';
import { StorageService } from 'src/app/core/service/storage.service';
import { ValidateEmail, ValidatePhoneNumber } from 'src/app/core/Validators';
import { ProjectManagementService } from 'src/app/project-management/project-management.service';
import { RawLeadManagementComponent } from '../raw-lead-management.component';
import { LeadManagementService } from 'src/app/lead-management/lead-management.service';


@Component({
  selector: 'app-add-raw-lead',
  templateUrl: './add-raw-lead.component.html',
  styleUrls: ['./add-raw-lead.component.sass']
})
export class AddRawLeadComponent implements OnInit {
  action: string;
    dialogTitle: string;
    leadManagmentForm: UntypedFormGroup;
    rawLeadManagmentForm: UntypedFormGroup;
    isSubmitted: boolean = false;
    leadSources: any = [];
    projects: any = [];
    leadType: any = [];
    leadStatus: any = [];
    users: any = [];
    roles: any = [];
    leadInventorySizes: any = [];
    phoneMask = ConstantConfig.PHONE_MASK;
    filteredUsers:any=[];
    record:any
   // rawLeadManagementService: any;
   
    constructor(
       public dialogRef: MatDialogRef<RawLeadManagementComponent>,
      @Inject(MAT_DIALOG_DATA) public data: any,
      private storageService: StorageService,
      private fb: UntypedFormBuilder,
      private projectService: ProjectManagementService,
      private msgService: MessageService,
      private router: Router,
      private  leadManagementService: LeadManagementService
    ) {
      this.leadSources = this.storageService.getLeadSource();
      this.leadStatus = this.storageService.getLeadStatus();
      this.leadType = this.storageService.getLeadType();
      this.roles = this.storageService.getRoles();
      this.users = this.storageService.getUsers();
      this.leadInventorySizes = this.storageService.getLeadInventorySize();
     
      // Set the defaults
      this.action = data.action;
      this.createContactForm(data.record);
    
    }
  
    ngOnInit(): void { 
      this.getProjects();
      if (this.action === 'edit') {
        this.dialogTitle = 'Edit Record';
        const lead = this.data.leadManagement;
        this.createContactForm(lead);
      } else {
        this.dialogTitle = 'New Lead';
      }
    }
  
    //creating Lead Management Form
    createContactForm(lead) {
      this.rawLeadManagmentForm = this.fb.group({
        id: [lead == null ? null : lead.id],
  
        customer: this.fb.group({
          id: [lead == null ? null : lead.id],
          name: [lead == null ? null : lead.customerName,Validators.required],
          email: [lead == null ? null : lead.customerEmail],
          phone: [lead == null ? null : lead.customerPhone, [ ValidatePhoneNumber]],
          alternatePhone: null,
        }),
  
  
        source: [lead == null ? null : lead.source,Validators.required],
        project: [lead == null ? null : lead.project,Validators.required],
        type: [lead == null ? null : lead.type,Validators.required],
        assignedTo: [lead == null ? null : lead.assignedTo?.id,Validators.required],
        status: [lead == null ? null : lead.status,Validators.required],
        leadInventorySize: [lead == null ? null : lead.leadInventorySize,Validators.required],
        comments: this.fb.group({
          id: [lead == null ? null : lead.comments?.id],
          message: [lead == null ? null : lead.comments?.message,Validators.required],
        }),
  
        budget: this.fb.group({
          id: [null],
          startAmount: [null,Validators.required],
          startUnit: [null,Validators.required],
          endAmount: [null,Validators.required],
          endUnit: [null,Validators.required],
        })
      })
      
    }
    
  
    setValue() {
      this.rawLeadManagmentForm.patchValue({
        source: this.data.leadManagement.source,
        assignedTo: this.data.rawLeadManagement.assignedTo
      })
    }
  
    selectedProject(projectId) {
      this.filteredUsers = this.users.filter(e => e.project.id === projectId.id);
    }
  
    //On Lead Management Form Submit
  submitForm(form: UntypedFormGroup) {
    var rawLeadData=this.data.record;
    this.isSubmitted = true;
    if (form.valid) {
      this.isSubmitted = true
      const param = form.value;

      let newcomment = param.comments;

      //clear old comment in case of resubmit
      param.comments = null;
      // submit the data to backend
      param.comments = [newcomment]
      this.leadManagementService.addRawToLead(param,rawLeadData.id).subscribe({
        next: (res) => {
          if (res.status && res.status == 500) {
            this.isSubmitted = false;
            this.msgService.showError(res.message);
          } else {
            this.msgService.showSuccess("Lead Added Successfully");
            this.dialogRef.close(res);
          }
          this.isSubmitted = false;
        },
        error: (e) => {
          this.isSubmitted = false;
        },
        complete: () => console.log('done'),
      });


    }
    }
  
    onNoClick(): void {
      this.dialogRef.close();
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


