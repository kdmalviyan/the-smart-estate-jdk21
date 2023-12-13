import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { FilterService } from 'src/app/core/service/filter.service';
import { MessageService } from 'src/app/core/service/message.service';
import { MetaDataService } from '../meta-data.service';

@Component({
  selector: 'app-deactivate-reason',
  templateUrl: './deactivate-reason.component.html',
  styleUrls: ['./deactivate-reason.component.sass']
})
export class DeactivateReasonComponent implements OnInit {

  reasonData = [];
  dataSource = [];
  deactivateForm: UntypedFormGroup;
  modalName: string;
  modalType: string;
  statusId: number;
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;
  constructor(
    private metaDataService: MetaDataService,
    private fb: UntypedFormBuilder,
    private modalService: NgbModal,
    private messageService: MessageService,
    private filterService: FilterService
  ) {
    this.deactivateForm = this.fb.group({
      id: [''],
      name: ['', Validators.required],
      description: ['', Validators.required],
    });

  }

  ngOnInit(): void {
    this.getDeactivateReason();
  }
  getDeactivateReason() {
    this.metaDataService.getAllLead('deactivationReason').subscribe((res: any) => {
      this.reasonData = res;
      this.dataSource = this.reasonData;
    },
      err => {
        throw err;
      }
    )
  }
  filterDatatable(event) {
    this.reasonData = this.filterService.filter(event, this.dataSource, this.reasonData);
    // whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }
  createUpdateReason(form: UntypedFormGroup) {
    if (form.valid) {
      if (this.modalType === "add") {
        this.metaDataService.createLead('deactivationReason', form.value).subscribe((res: any) => {
          if (res != null) {
            this.messageService.showSuccess('Saved Sucessfully!');
            this.getDeactivateReason();
            this.modalService.dismissAll();
            form.reset();
          }
          else {
            this.messageService.showError('Something Went Wrong!');
          }
        });
      }
      else if (this.modalType === "edit") {
        this.metaDataService.updateLead('deactivationReason', form.value).subscribe((res: any) => {
          if (res != null) {
            this.messageService.showSuccess('Updated Sucessfully!');
            this.getDeactivateReason();
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
      this.deactivateForm.patchValue({
        id: row.id,
        description: row.description,
        name: row.name
      })
    }
  }
  openDeleteReason(content: any, data: any) {
    this.statusId = data.id;
    const modaRef = this.modalService.open(content, {
      ariaLabelledBy: '',
      backdrop: false
    });


  }
  deleteReason() {
    this.metaDataService.deactive('leadstatus', this.statusId).subscribe((res: any) => {
      this.reasonData = this.reasonData.filter((value: any) => value.id != res.id);
      this.messageService.showSuccess('Deleted Successfully!');
      this.modalService.dismissAll();
    });
  }
  onCloseModal() {
    this.modalService.dismissAll();
    this.deactivateForm.reset();
  }

}
