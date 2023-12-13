import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import * as moment from 'moment';
import { MessageService } from 'src/app/core/service/message.service';
import { StorageService } from 'src/app/core/service/storage.service';
import { FollowUpService } from 'src/app/follow-up/follow-up.service';

@Component({
  selector: 'app-followup-create',
  templateUrl: './followup-create.component.html',
  styleUrls: ['./followup-create.component.css']
})
export class FollowupCreateComponent implements OnInit {

  deactivationReasons = [];
  leadDetails: any;
  metadata: any;
  constructor(
    private storageService: StorageService,
    private messageService: MessageService,
    private followUpService: FollowUpService,
    public dialogRef: MatDialogRef<FollowupCreateComponent>,
    @Inject(MAT_DIALOG_DATA) public data
  ) {
    dialogRef.disableClose = true;
    this.metadata = this.storageService.getStorage();
    this.leadDetails = data.leadData;
  }

  ngOnInit(): void {
  }

  choosedDate(event) {
    if (event.startDate !== null || event.endDate !== null) {
      let followUpData = moment(event.startDate.toDate()).format('M/DD/yyyy, h:mm:ss A').toLocaleString()
      this.followUpService.addFollowup(this.leadDetails.id, followUpData).subscribe(res => {
        const followupStatus = this.metadata.leadStatus.filter(e => e.name == "FOLLOW-UP")[0];
        this.leadDetails.status = followupStatus;
        this.messageService.showSuccess("New Follow up created")
      });
    }
    this.dialogRef.close();
  }


  isInvalidDate(date) {
    var newdate = new Date(new Date().getTime());
    newdate.setHours(0, 0, 0, 0);
    return date.toDate() < newdate;
  }

}
