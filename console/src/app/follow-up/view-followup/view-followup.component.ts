import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-view-followup',
  templateUrl: './view-followup.component.html',
  styleUrls: ['./view-followup.component.scss']
})
export class ViewFollowupComponent implements OnInit {

  leadData: any;
  constructor(public viewLeaddialogRef: MatDialogRef<ViewFollowupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.leadData = data;
  }

  ngOnInit(): void {
  }


  close(): void {
    this.viewLeaddialogRef.close();
  }


}
