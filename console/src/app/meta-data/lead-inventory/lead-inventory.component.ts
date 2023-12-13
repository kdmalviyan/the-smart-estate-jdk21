import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { FilterService } from 'src/app/core/service/filter.service';
import { MessageService } from 'src/app/core/service/message.service';
import { MetaDataService } from '../meta-data.service';

@Component({
  selector: 'app-lead-inventory',
  templateUrl: './lead-inventory.component.html',
  styleUrls: ['./lead-inventory.component.sass']
})
export class LeadInventoryComponent implements OnInit {

  inventoryData = [];
  dataSource = [];
  inventoryForm: UntypedFormGroup;
  modalName: string;
  modalType: string;
  inventoryId: number;
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;
  constructor(
    private metaDataService: MetaDataService,
    private fb: UntypedFormBuilder,
    private modalService: NgbModal,
    private messageService: MessageService,
    private filterService: FilterService
  ) {
    this.inventoryForm = this.fb.group({
      id: [''],
      size: ['', Validators.required],
      description: ['', Validators.required],
    });

  }

  ngOnInit(): void {
    this.getLeadInventory();
  }
  getLeadInventory() {
    this.metaDataService.getAllLead('leadInventorySize').subscribe((res: any) => {
      this.inventoryData = res;
      this.dataSource = this.inventoryData;
    },
      err => {
        throw err;
      }
    )
  }
  filterDatatable(event) {
    this.inventoryData = this.filterService.filter(event, this.dataSource, this.inventoryData);
    // whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }
  createUpdateInventory(form: UntypedFormGroup) {
    if (form.valid) {
      if (this.modalType === "add") {
        this.metaDataService.createLead('leadInventorySize', form.value).subscribe((res: any) => {
          if (res != null) {
            this.messageService.showSuccess('Saved Sucessfully!');
            this.getLeadInventory();
            this.modalService.dismissAll();
            form.reset();
          }
          else {
            this.messageService.showError('Something Went Wrong!');
          }
        });
      }
      else if (this.modalType === "edit") {
        this.metaDataService.updateLead('leadInventorySize', form.value).subscribe((res: any) => {
          if (res != null) {
            this.messageService.showSuccess('Updated Sucessfully!');
            this.getLeadInventory();
            this.modalService.dismissAll();
            form.reset();
          }
          else {
            this.messageService.showError('Something Went Wrong!');
          }
        })
      }
    }
  }
  addEditRow(content: any, row: any, type: string) {
    const modalRef = this.modalService.open(content,
      {
        ariaLabelledBy: '',
        backdrop: false
      })
    this.modalType = type;
    if (this.modalType === "add") {
      this.modalName = "New";
    }
    else {
      this.modalName = "Edit";
      this.inventoryForm.patchValue({
        id: row.id,
        description: row.description,
        size: row.size
      })
    }
  }
  openInventory(content: any, data: any) {
    this.inventoryId = data.id;
    const modaRef = this.modalService.open(content, {
      ariaLabelledBy: '',
      backdrop: false
    });


  }
  deleteInventory() {
    this.metaDataService.deactive('leadInventorySize', this.inventoryId).subscribe((res: any) => {
      this.inventoryData = this.inventoryData.filter((value: any) => value.id != res.id);
      this.messageService.showSuccess('Deleted Successfully!');
      this.modalService.dismissAll();
    });
  }
  onCloseModal() {
    this.modalService.dismissAll();
    this.inventoryForm.reset();
  }

}
