import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Component, Inject } from '@angular/core';
import { TargetsService } from '../targets.service';

@Component({
  selector: 'app-delete-target',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.sass']
})
export class DeleteTargetDialogComponent {
  content;
  constructor(
    public dialogRef: MatDialogRef<DeleteTargetDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public targetService: TargetsService
  ) {
    console.log(data);
    this.content = data.content;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
  confirmDelete(): void {
    this.targetService.deleteTarget(this.data.id).subscribe(
      (res: any) => {
        this.dialogRef.close(true);
      }
    );
  }
}
