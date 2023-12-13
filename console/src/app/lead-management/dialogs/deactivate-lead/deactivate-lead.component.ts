import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StorageService } from 'src/app/core/service/storage.service';

@Component({
  selector: 'app-deactivate-lead',
  templateUrl: './deactivate-lead.component.html',
  styleUrls: ['./deactivate-lead.component.sass']
})
export class DeactivateLeadComponent implements OnInit {

  deactivationReasons = [];
  reason = new UntypedFormControl('', [Validators.required]);
  remark = new UntypedFormControl('', [Validators.required]);

  constructor(
    private storageService: StorageService,
    public dialogRef: MatDialogRef<DeactivateLeadComponent>,
    @Inject(MAT_DIALOG_DATA) public data) { 
      dialogRef.disableClose = true;

    }

  ngOnInit(): void {
    const metadata = this.storageService.getStorage();
    this.deactivationReasons = metadata.deactivationReasons;
  }

  submit() {
    let data = {
      deactivationReason: this.reason.value,
      comment: this.remark.value
    }
    this.dialogRef.close(data);
  }

}
