import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { FilterService } from 'src/app/core/service/filter.service';
import { MessageService } from 'src/app/core/service/message.service';
import { MetaDataService } from '../meta-data.service';

@Component({
  selector: 'app-lead-status',
  templateUrl: './lead-status.component.html',
  styleUrls: ['./lead-status.component.sass']
})
export class LeadStatusComponent implements OnInit {
  statusData = [];
  dataSource = [];
  statusForm: UntypedFormGroup;
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
    this.statusForm = this.fb.group({
      id: [''],
      name:['',Validators.required],
      description: ['', Validators.required],
    });

  }

  ngOnInit(): void {
    this.getLeadStatus();
  }
  getLeadStatus() {
    this.metaDataService.getAllLead('leadstatus').subscribe((res: any) => {
      this.statusData = res;
      this.dataSource = this.statusData;
    },
      err => {
        throw err;
      }
    )
  }
  filterDatatable(event) {
    this.statusData = this.filterService.filter(event, this.dataSource, this.statusData);
    // whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }
  createUpdateStatus(form: UntypedFormGroup) {
    if (form.valid) {
      if (this.modalType === "add") {
        this.metaDataService.createLead('leadstatus', form.value).subscribe((res: any) => {
          if (res != null) {
            this.messageService.showSuccess('Saved Sucessfully!');
            this.getLeadStatus();
            this.modalService.dismissAll();
            form.reset();
          }
          else {
            this.messageService.showError('Something Went Wrong!');
          }
        });
      }
      else if (this.modalType === "edit") {
        this.metaDataService.updateLead('leadstatus', form.value).subscribe((res: any) => {
          if (res != null) {
            this.messageService.showSuccess('Updated Sucessfully!');
            this.getLeadStatus();
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
      this.statusForm.patchValue({
        id: row.id,
        description: row.description,
        name:row.name
      })
    }
  }
  openStatus(content:any, data:any) {
    this.statusId = data.id;
    const modaRef = this.modalService.open(content, {
      ariaLabelledBy: '',
      backdrop: false
    });

   
  }
  deleteStatus() {
    this.metaDataService.deactive('leadstatus',this.statusId).subscribe((res: any) => {
      this.statusData = this.statusData.filter((value: any) => value.id != res.id);
      this.messageService.showSuccess('Deleted Successfully!');
      this.modalService.dismissAll();
    });
  }
  onCloseModal() {
    this.modalService.dismissAll();
    this.statusForm.reset();
  }
}
