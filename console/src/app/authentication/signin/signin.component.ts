import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/service/auth.service';
import { UnsubscribeOnDestroyAdapter } from 'src/app/shared/UnsubscribeOnDestroyAdapter';
@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent
  extends UnsubscribeOnDestroyAdapter
  implements OnInit {

  loginForm: UntypedFormGroup;
  submitted = false;
  error = '';
  hide = true;
  show: boolean = false;

  constructor(
    private formBuilder: UntypedFormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    super();
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      email: [
        '',
        [Validators.required]
      ],
      tenantId:['', Validators.required],
      password: ['', Validators.required]
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    this.error = '';
    if (this.loginForm.invalid) {
      this.error = 'Username and Password not valid !';
      return;
    } else {
      let userData = {
        username: this.f.email.value,
        password: this.f.password.value
      }
      let  userObject = { name: "user", token: "", tenantId: this.f.tenantId.value };
      this.authService.setcurrentUserValue(userObject);

      localStorage.setItem("tenant", this.f.tenantId.value);

      this.authService.login(userData).subscribe(
        (res: any) => {
          if (res as any) {
            let user = this.authService.getcurrentUserValue();
            user.token = res.token;
            localStorage.setItem('token', res.token);
            localStorage.setItem("currentUser", JSON.stringify(user));
            localStorage.setItem("refreshToken", JSON.stringify(res.refreshToken));
            localStorage.setItem('loggedInUserName',res.username);
            if (user.token) {
              this.router.navigate(['/dashboard/main']);
            }
          } else {
            this.error = 'Invalid Login';
          }
          this.submitted = false;
        },
        (error) => {
           this.error = 'Invalid Username or Password!';
          this.submitted = false;
        }
      );
    }
  }

}
