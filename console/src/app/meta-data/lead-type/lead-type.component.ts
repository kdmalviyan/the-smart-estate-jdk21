import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { FilterService } from 'src/app/core/service/filter.service';
import { MessageService } from 'src/app/core/service/message.service';
import { MetaDataService } from '../meta-data.service';

@Component({
  selector: 'app-lead-type',
  templateUrl: './lead-type.component.html',
  styleUrls: ['./lead-type.component.sass']
})
export class LeadTypeComponent implements OnInit {
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;
  leadTypeForm: UntypedFormGroup;
  leadTypeData = [];
  dataSource = [];
  modalType: string;
  modalName: string;
  leadTypeId: number;
  constructor(
    private metaDataService: MetaDataService,
    private messageService: MessageService,
    private modalService: NgbModal,
    private filterService: FilterService,
    private fb: UntypedFormBuilder
  ) {
    this.leadTypeForm = this.fb.group({
      id: [],
      type: ['', Validators.required],
      description: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.getLeadType();
  }
  getLeadType() {
    this.metaDataService.getAllLead('leadtype').subscribe((res: any) => {
      this.leadTypeData = res;
      this.dataSource = this.leadTypeData;
    },
      err => {
        throw err;
      }
    )
  }
  filterDatatable(event) {
    this.leadTypeData = this.filterService.filter(event, this.dataSource, this.leadTypeData);
    // whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }
  createUpdateLeadType(form: UntypedFormGroup) {
    if (form.valid) {
      if (this.modalType === "add") {
        this.metaDataService.createLead('leadtype', form.value).subscribe((res: any) => {
          if (res != null) {
            this.messageService.showSuccess('Saved Sucessfully!');
            this.getLeadType();
            this.modalService.dismissAll();
            form.reset();
          }
          else {
            this.messageService.showError('Something Went Wrong!');
          }
        });
      }
      else if (this.modalType === "edit") {
        this.metaDataService.updateLead('leadtype', form.value).subscribe((res: any) => {
          if (res != null) {
            this.messageService.showSuccess('Updated Sucessfully!');
            this.getLeadType();
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
      this.leadTypeForm.patchValue({
        id: row.id,
        description: row.description,
        type: row.type
      })
    }
  }
  openLeadType(content: any, data: any) {
    this.leadTypeId = data.id;
    const modaRef = this.modalService.open(content, {
      ariaLabelledBy: '',
      backdrop: false
    });


  }
  deleteLeadType() {
    this.metaDataService.deactive('leadtype', this.leadTypeId).subscribe((res: any) => {
      this.leadTypeData = this.leadTypeData.filter((value: any) => value.id != res.id);
      this.messageService.showSuccess('Deleted Successfully!');
      this.modalService.dismissAll();
    });
  }
  onCloseModal() {
    this.modalService.dismissAll();
    this.leadTypeForm.reset();
  }

}
