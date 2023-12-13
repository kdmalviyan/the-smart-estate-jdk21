import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MetaDataService } from 'src/app/meta-data/meta-data.service';

@Component({
  selector: 'app-add-vaction',
  templateUrl: './add-vaction.component.html',
  styleUrls: ['./add-vaction.component.sass']
})
export class AddVactionComponent implements OnInit {
  vacationType = [];
  dayType = [
    { key: 'Morning Half', value: true },
    { key: 'After Noon Half', value: true },
  ];
  addVacationForm: UntypedFormGroup;
  formValue: any;
  selectedDay: string;
  constructor(
    public dialogRef: MatDialogRef<AddVactionComponent>,
    private metaDataServise: MetaDataService,
    private formBuilder: UntypedFormBuilder
  ) {
    this.addVacationForm = this.formBuilder.group({
      vacationtype: [],
      daytype: [],
      startDate: [],
      endDate: [],
      reason: []
    });
  }

  ngOnInit(): void {
    this.getVacationType();
  }
  getVacationType() {
    this.metaDataServise.getVacation('vacation/type').subscribe((res: any) => {
      this.vacationType = res;
      console.log(this.vacationType);
    });
  }
  createVacation() {

    if (this.selectedDay === 'Morning Half') {
      this.formValue = {
        startDate: this.addVacationForm.controls.startDate.value,
        endDate: this.addVacationForm.controls.endDate.value,
        vacationType: this.addVacationForm.controls.vacationtype.value,
        morningHalf: true,
        reason: this.addVacationForm.controls.reason.value,
        createdFor: localStorage.getItem('loggedInUserName')
      }

    }
    else if (this.selectedDay === 'After Noon Half') {
      this.formValue = {
        startDate: this.addVacationForm.controls.startDate.value,
        endDate: this.addVacationForm.controls.endDate.value,
        vacationType: this.addVacationForm.controls.vacationtype.value,
        afternoonHalf: true,
        reason: this.addVacationForm.controls.reason.value,
      }
    }
    this.metaDataServise.createVacation('vacation', this.formValue).subscribe((res: any) => {
      console.log(res);
      
    })
    console.log(JSON.stringify(this.formValue))


  }
  selectDayType(data: any) {
    this.selectedDay = data.value;
  }
}
