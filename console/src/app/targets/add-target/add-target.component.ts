import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import * as moment from 'moment';
import { MessageService } from 'src/app/core/service/message.service';
import { StorageService } from 'src/app/core/service/storage.service';
import { TargetsService } from '../targets.service';

@Component({
  selector: 'app-add-target',
  templateUrl: './add-target.component.html',
  styleUrls: ['./add-target.component.sass']
})
export class AddTargetComponent implements OnInit {
  targetForm: UntypedFormGroup;
  isSubmitted: boolean = false;
  metadata;
  currentProject: any;
  today = new Date();
  constructor(private targetService: TargetsService,
    private msgService: MessageService,
    private storageService: StorageService,
    public dialogRef: MatDialogRef<AddTargetComponent>,
    private fb: UntypedFormBuilder,) {
    dialogRef.disableClose = true;
    this.metadata = this.storageService.getStorage();
    var projectid = this.metadata.loggedInUser.project.id;
    const currentProject = this.metadata.projects.filter(e => e.id === projectid)[0];
    this.createContactForm(null, currentProject);
  }


  weekendsDatesFilter = (d: Date): boolean => {
    if (d != undefined) {
      const day = d.getDay();
      /* Prevent all other days to select */
      return day == 1;
    }

  }

  createContactForm(target, currentProject) {
    this.targetForm = this.fb.group({
      id: [target == null ? null : target.id],
      bookingCount: [target == null ? null : target.booking, Validators.required],
      siteVisitCount: [target == null ? null : target.siteVisit, Validators.required],
      startDate: [target == null ? null : target.startTime, Validators.required],
      duration: [target == null ? null : target.duration, Validators.required],
      project: currentProject
    });
    console.log(this.targetForm.value);
  }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
  
  //On Lead Management Form Submit
  submitForm(form: UntypedFormGroup) {

    this.isSubmitted = true;

    if (form.valid) {
      this.isSubmitted = true
      const param = form.value;

      this.targetService.create(param).subscribe({
        next: (res) => {
          if (res.status && res.status == 500) {
            this.isSubmitted = false;
            this.msgService.showError(res.message);
          } else {
            this.msgService.showSuccess("Target Added Successfully");
            this.dialogRef.close(res);
          }
          this.isSubmitted = false;
        },
        error: (e) => {
          this.isSubmitted = false;
        },
        complete: () => console.log('done'),
      });


    }
  }

}

