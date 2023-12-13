import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StorageService } from 'src/app/core/service/storage.service';

@Component({
  selector: 'app-advance-filter',
  templateUrl: './advance-filter.component.html',
  styleUrls: ['./advance-filter.component.sass']
})
export class AdvanceFilterComponent implements OnInit {


  deactivationReasons = [];
  leadSources = [];

  reason = new UntypedFormControl('');
  source = new UntypedFormControl('');
  budget = new UntypedFormGroup({
    startAmount: new UntypedFormControl('', Validators.required),
    startUnit: new UntypedFormControl('', Validators.required),
    endAmount: new UntypedFormControl('', Validators.required),
    endUnit: new UntypedFormControl('', Validators.required),
  })

  constructor(
    private storageService: StorageService,
    public dialogRef: MatDialogRef<AdvanceFilterComponent>,
    @Inject(MAT_DIALOG_DATA) public data) {
    dialogRef.disableClose = true;

  }

  ngOnInit(): void {
    const metadata = this.storageService.getStorage();
    this.deactivationReasons = metadata.deactivationReasons;
    this.leadSources = metadata.leadSources;

  }

  addFilter() {
    let data = {
      deactivationReason: this.reason.value.id,
      budget: this.budget.value,
      source: this.source.value
    }
    this.dialogRef.close(data);
  }

}
