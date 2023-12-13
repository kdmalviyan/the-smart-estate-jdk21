import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MessageService } from 'src/app/core/service/message.service';
import { ProjectManagementService } from '../project-management.service';

@Component({
  selector: 'app-upload-inventory',
  templateUrl: './upload-inventory.component.html',
  styleUrls: ['./upload-inventory.component.sass']
})
export class UploadInventoryComponent implements OnInit {
  uploadForm!: UntypedFormGroup;
  isUploadSubmitted: boolean = false;
  projectId: any;
  uploadInventoryErrors= [];
  constructor(private projectManagementService: ProjectManagementService,
    public dialogRef: MatDialogRef<UploadInventoryComponent>,
    private fb: UntypedFormBuilder,
    private msgService: MessageService,
    @Inject(MAT_DIALOG_DATA) public data
  ) {
    dialogRef.disableClose = true;
    this.projectId = data.projectId;
    this.uploadForm = this.fb.group({
      file: ['', Validators.required]
    });
  }

  ngOnInit(): void {
  }

  uploadFile() {
    this.isUploadSubmitted = true;
    if (this.uploadForm.valid) {
      const param = this.uploadForm.value;
      const formData: FormData = new FormData();
      formData.append('file', param.file);
      this.projectManagementService.uploadInventoryWithExcel(formData, this.projectId).subscribe(
        (res) => {
          const response = res as any;
          this.uploadForm.reset();
          this.isUploadSubmitted = false;
          this.uploadInventoryErrors = response.errors;
          if(!(response.errors && response.errors.length > 0)) {
            this.dialogRef.close(response);
          }
         }
      )
    }
  }

  close() {
    this.dialogRef.close();
  }

  downloadFile() {
    let link = document.createElement("a");
    link.download = "inventory_format";
    link.href = "assets/file_format/inventory_format.xlsx";
    link.click();
  }
}
