import { AfterViewInit, Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/core/service/auth.service';
import { MessageService } from 'src/app/core/service/message.service';
import { StorageService } from 'src/app/core/service/storage.service';
import { ProfileService } from '../profile.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.sass']
})
export class ProfileComponent implements OnInit {

  user;
  subscription: Subscription;
  passwordForm!: UntypedFormGroup;
  file = new UntypedFormControl();

  constructor(
    private storageService: StorageService,
    private fb: UntypedFormBuilder,
    private profileService: ProfileService,
    private msgService: MessageService) {
    this.createPasswordForm();
  }

  ngOnInit() {
    const userName = localStorage.getItem("loggedInUserName");
    this.profileService.getUser(userName).subscribe(
      res => {
        this.user = res;
      }
    )
  }

  createPasswordForm() {
    this.passwordForm = this.fb.group({
      oldPassword: ['', [Validators.required, Validators.minLength(5)]],
      password: ['', [Validators.required, Validators.minLength(5)]],
      rePassword: ['', [Validators.required, Validators.minLength(5)]],
    })
  }

  submit() {
    if (this.passwordForm.valid) {
      const param = this.passwordForm.value;
      param.username = this.user.username;
      this.profileService.changePassword(param).subscribe(
        res => {
          this.msgService.showSuccess("Password Changed successfully")
        }
      )
    }
  }

  uploadPhoto() {
    if (this.file.valid) {
      const file = this.file.value;
      const size = file.size / 1024; // in kb
      if (size > Number(500)) {
        this.msgService.showError(`Upto 500 KB file size allowed`);
      } else {
        const formData = new FormData();
        formData.append("file", this.file.value);
        this.profileService.uploadPhoto(formData, this.user?.id).subscribe(
          res => {
            this.user = res;
          }
        )
      }

    }
  }

  getImagePath() {
    return this.user?.profileImagePath != null ? this.user?.profileImagePath : 'assets/images/user/usrbig3.jpg';
  }

}
