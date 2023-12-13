import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MessageService } from 'src/app/core/service/message.service';
import { StorageService } from 'src/app/core/service/storage.service';
import { FollowUpService } from 'src/app/follow-up/follow-up.service';

@Component({
  selector: 'app-followup-comment',
  templateUrl: './followup-comment.component.html',
  styleUrls: ['./followup-comment.component.sass']
})
export class FollowupCommentComponent implements OnInit {

  deactivationReasons = [];

  followups: any;
  leadId: any;
  followupCommentForm: UntypedFormGroup;
  constructor(
    private storageService: StorageService,
    private messageService: MessageService,
    private followUpService: FollowUpService,
    private fb: UntypedFormBuilder,
    public dialogRef: MatDialogRef<FollowupCommentComponent>,

    @Inject(MAT_DIALOG_DATA) public data) {
      dialogRef.disableClose = true;
    this.followups = data.followupdata;
    this.leadId = data.leadId;
  }

  ngOnInit(): void {
    const metadata = this.storageService.getStorage();
    this.followupCommentForm = this.fb.group({
      remark: new UntypedFormControl('', [Validators.required])
    });
    // this.deactivationReasons = metadata.deactivationReasons;
  }

  submit() {
    let data = {
      followUpId: this.followups.id,
      followupMessage :this.followupCommentForm.value.remark
    }
    this.followUpService.updateFollowup(data, this.leadId).subscribe(
      res => {
      this.messageService.showSuccess("Follow up complete")
      this.dialogRef.close(res);
    },
    err =>{
      console.log('WEEeeee Error', err);
    } 
    );
  }
}
