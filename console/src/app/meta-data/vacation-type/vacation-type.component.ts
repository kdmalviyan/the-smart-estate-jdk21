import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { FilterService } from 'src/app/core/service/filter.service';
import { MessageService } from 'src/app/core/service/message.service';
import { MetaDataService } from '../meta-data.service';

@Component({
  selector: 'app-vacation-type',
  templateUrl: './vacation-type.component.html',
  styleUrls: ['./vacation-type.component.sass']
})
export class VacationTypeComponent implements OnInit {
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;
  vacationTypeForm: UntypedFormGroup;
  vacationTypeData = [];
  dataSource = [];
  modalType: string;
  modalName: string;
  formValue: any;
  rowData: any;
  vacationId: any;
  constructor(private fb: UntypedFormBuilder,
    private modalService: NgbModal,
    private messageService: MessageService,
    private metadataservice: MetaDataService,
    private filterService: FilterService
  ) {
    this.vacationTypeForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.getVacationType();
  }
  getVacationType() {
    this.metadataservice.getVacation('vacation/type').subscribe((res: any) => {
      this.vacationTypeData = res;
      this.dataSource = this.vacationTypeData;
    }
      , err => {
        throw err;
      })
  }
  filterDatatable(event) {
    this.vacationTypeData = this.filterService.filter(event, this.dataSource, this.vacationTypeData);
    // whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }
  createUpdateVacationType(form: UntypedFormGroup) {

    if (form.valid) {
      if (this.modalType === 'add') {
        this.formValue =
        {
         active: 'true',
          ...form.value
        }
        this.metadataservice.createVacation('vacation/type', this.formValue).subscribe((res: any) => {
          if (res !== null) {
            this.messageService.showSuccess('Added Successfully!');
            this.getVacationType();
            this.modalService.dismissAll();
            form.reset();
            this.formValue = '';
          }
          else {
            this.messageService.showError('Something Went Wrong!');
          }
        });
      }
      else if (this.modalType === 'edit') {
        this.formValue = {
          ...form.value,
          active: 'true',
          id: this.rowData.id
        }
        this.metadataservice.updateVacation('vacation/type', this.formValue).subscribe((res: any) => {
          if (res !== null) {
            this.messageService.showSuccess('Updated Successfully!');
            this.getVacationType();
            this.modalService.dismissAll();
            form.reset();
          }
          else {
            this.messageService.showError('Something Went Wrong!');

          }
        });
      }
    }

  }
  openDeleteVacation(content: any, data: any) {
    this.vacationId = data.id;
    const modaRef = this.modalService.open(content, {
      ariaLabelledBy: '',
      backdrop: false
    });
  }
  deleteVacationType() {
    this.metadataservice.deleteVacation('vacation/type', this.vacationId).subscribe((res: any) => {
      if (res !== null) {
        this.messageService.showSuccess(res.message);
        this.getVacationType();
        this.onCloseModal();
      }
      else {
        this.messageService.showError('Something Went Wrong!');
      }

    });
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
      this.rowData = row;
      this.vacationTypeForm.patchValue({
        name: row.name,
        description: row.description
      });
    }
  }
  onCloseModal() {
    this.modalService.dismissAll();
    this.vacationTypeForm.reset();
  }
}
