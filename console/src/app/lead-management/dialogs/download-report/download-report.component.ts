import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MessageService } from 'src/app/core/service/message.service';
import { LeadManagementService } from '../../lead-management.service';

@Component({
  selector: 'app-download-report',
  templateUrl: './download-report.component.html',
  styleUrls: ['./download-report.component.sass']
})
export class DownloadReportComponent implements OnInit {

  otpForm: UntypedFormGroup;

  constructor(
    private messageService: MessageService,
    private leadManagementService: LeadManagementService,
    private fb: UntypedFormBuilder,
    public dialogRef: MatDialogRef<DownloadReportComponent>,

    @Inject(MAT_DIALOG_DATA) public data) {
    dialogRef.disableClose = true;
  }

  ngOnInit(): void {
    this.otpForm = this.fb.group({
      otp: new UntypedFormControl('', [Validators.required])
    });
  }

  submit() {
    this.messageService.showInfo("Validating OTP .")
    this.leadManagementService.validateDownloadFileOtp(this.otpForm.value.otp).subscribe((result) => {
      var data = result as any;
      console.log(result)
      if (data.status == 500) {
        this.messageService.showError(data.message);
      } else {
        this.messageService.showSuccess("OTP validated successfully");
        this.dialogRef.close("success");
      }
    });

  }
}
