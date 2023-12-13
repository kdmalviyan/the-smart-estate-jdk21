import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ColDef } from 'ag-grid-community';
import { MessageService } from 'src/app/core/service/message.service';
import { ProjectManagementService } from '../project-management.service';

@Component({
  selector: 'app-add-edit-inventory',
  templateUrl: './add-edit-inventory.component.html',
  styleUrls: ['./add-edit-inventory.component.sass']
})
export class AddEditInventoryComponent implements OnInit {

  inventoryForm !: UntypedFormGroup;
  editFlatForm !: UntypedFormGroup;
  isFlatSubmitted: boolean = false;
  projectId: any;
  inventory: any;
  constructor(private fb: UntypedFormBuilder,
    private projectService: ProjectManagementService,
    public dialogRef: MatDialogRef<AddEditInventoryComponent>,
    private msgService: MessageService,
    @Inject(MAT_DIALOG_DATA) public data
  ) {
    dialogRef.disableClose = true;
    this.projectId = data.projectId;
    this.inventory = data.inventory;
    if (this.inventory == undefined) {
      this.inventory = null;
      this.createFlatForm(this.inventory);
    } else {
      this.createFlatForm(this.inventory);
    }
  }


  ngOnInit(): void {

  }

  // creating flat form with data
  createFlatForm(inventory) {
    this.inventoryForm = this.fb.group({
      id: [inventory == null ? null : inventory.id],
      size: [inventory == null ? null : inventory.size, Validators.required],
      corner: [inventory == null ? null : inventory.corner],
      facing: [inventory == null ? null : inventory.facing],
      sellingPrice: [inventory == null ? null : inventory.sellingPrice, Validators.required],
      tower: [inventory == null ? null : inventory.tower],
    })
  }

  // on flat form submit
  onAddFlat() {
    this.isFlatSubmitted = true;
    if (this.inventoryForm.valid) {
      let params = this.inventoryForm.value;
      this.projectService.addInventory(params, this.projectId).subscribe((res: any) => {
        this.dialogRef.close(res);
      });
    }
  }

  close() {
    this.dialogRef.close();
  }

}
