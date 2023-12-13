import { Component, Inject } from '@angular/core';
import {
  UntypedFormBuilder, UntypedFormControl,

  UntypedFormGroup, Validators
} from '@angular/forms';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ConstantConfig } from 'src/app/config/constant.config';
import { MessageService } from 'src/app/core/service/message.service';
import { StorageService } from 'src/app/core/service/storage.service';
import { ValidateEmail, ValidatePhoneNumber } from 'src/app/core/Validators';
import { ProjectManagementService } from 'src/app/project-management/project-management.service';
import { UserManagementService } from '../../user-management.service';

@Component({
  selector: 'app-form-dialog',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.sass'],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'en-GB' }]
})
export class AddUserComponent {
  action: string;
  dialogTitle: string;
  addUserForm: UntypedFormGroup;
  editUserForm: UntypedFormGroup;
  userData: any;
  roles: any;
  phoneMask = ConstantConfig.PHONE_MASK;
  projects: any = [];
  startDate = new Date(1990, 0, 1);
  submitted: boolean = false;
  today = new Date(); 

  constructor(
    public dialogRef: MatDialogRef<AddUserComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public userManagementService: UserManagementService,
    private projectService: ProjectManagementService,
    private storage: StorageService,
    private fb: UntypedFormBuilder,
    private messageService: MessageService
  ) {
    this.roles = this.removeSperAdmin(storage.getRoles());
    //load projects
    this.getProjects();
    // Set the defaults
    this.action = data.action;

    if (this.action === 'edit') {
      this.dialogTitle = data.userData.name;
      this.userData = data;
      this.addUserForm = this.createContactForm();
    } else {
      this.dialogTitle = 'New Employee';
      this.addUserForm = this.createContactForm();
    }
  }


  formControl = new UntypedFormControl('', [
    Validators.required
  ]);



  createContactForm(): UntypedFormGroup {
    if (this.userData) {
      const userData = this.userData.userData;
      return this.fb.group({
        name: new UntypedFormControl(userData.name, [Validators.required]),
        username: new UntypedFormControl(userData.username, [Validators.required]),
        email: new UntypedFormControl(userData.email, [Validators.required, Validators.email, ValidateEmail]),
        gender: new UntypedFormControl(userData.gender, [Validators.required]),
        address: new UntypedFormControl(userData.address, [Validators.required]),
        mobile: new UntypedFormControl(userData.mobile, [Validators.required, ValidatePhoneNumber]),
        alternatePhone: new UntypedFormControl(userData.alternatePhone, [ValidatePhoneNumber]),
        project: new UntypedFormControl(userData.project.id, [Validators.required]),
        role: new UntypedFormControl(userData.roles[0].id, [Validators.required]),
        dateOfBirth: new UntypedFormControl(userData.dateOfBirth, [Validators.required]),
      });
    } else {
      return this.fb.group({
        name: new UntypedFormControl(null, [Validators.required]),
        username: new UntypedFormControl(null, [Validators.required]),
        email: new UntypedFormControl(null, [Validators.required, Validators.email, ValidateEmail]),
        gender: new UntypedFormControl(null, [Validators.required]),
        address: new UntypedFormControl(null, [Validators.required]),
        mobile: new UntypedFormControl(null, [Validators.required, ValidatePhoneNumber]),
        alternatePhone: new UntypedFormControl(null, [ValidatePhoneNumber]),
        project: new UntypedFormControl(null, [Validators.required]),
        role: new UntypedFormControl(null, [Validators.required]),
        dateOfBirth: new UntypedFormControl(null, [Validators.required]),
      });
    }


  }


  getErrorMessage() {
    return this.formControl.hasError('required')
      ? 'Required field'
      : this.formControl.hasError('email')
        ? 'Not a valid email'
        : '';
  }


  public confirmAdd(): void {
    this.submitted = true;
    if (this.action === 'edit') {
      if (this.addUserForm.invalid) {
        return;
      }

      const user = this.addUserForm.value;
      //filter role on id
      const role = this.roles.filter((e) => e.id === parseInt(user.role));
      const project = this.projects.filter((e) => e.id === parseInt(user.project))[0];

      user.roles = role;
      user.project = project;
      let userUpdated = Object.assign(this.data.userData, user);
      // userUpdated.authorities=[];
      this.userManagementService.update(userUpdated).subscribe((res: any) => {
        this.userManagementService.dialogData = res;
        this.dialogRef.close('closed');
        this.submitted = false;
        this.messageService.showSuccess("User Edited successfullly.")
      });

    } else {
      if (this.addUserForm.invalid) {
        return;
      }
      const user = this.addUserForm.value;
      //filter role on id
      const role = this.roles.filter((e) => e.id === parseInt(user.role));
      const project = this.projects.filter((e) => e.id === parseInt(user.project))[0];
      user.roles = role;
      user.project = project;
      this.userManagementService.createUser(user).subscribe((res: any) => {
        if(res.status==500){
          this.messageService.showError(res.message);
        }else{
          this.userManagementService.dialogData = res;
          this.dialogRef.close('closed');
          this.submitted = false;
        }
      });
    }
  }

  removeSperAdmin(arr) {
    return arr.filter((ele, i) => {
      return ele.name != 'SUPERADMIN';
    })

  }

  getProjects() {
    this.projectService.getAllProjects().subscribe(
      res => {
        this.projects = res;
      }
    )
  }


}
