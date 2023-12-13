import { Component, OnInit, ViewChild } from '@angular/core';
import {
  UntypedFormBuilder,
  UntypedFormControl,
  UntypedFormGroup,
  Validators
} from '@angular/forms';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { MessageService } from 'src/app/core/service/message.service';
import { StorageService } from 'src/app/core/service/storage.service';
import { FilterService } from '../../core/service/filter.service';
import { MetaDataService } from '../meta-data.service';

@Component({
  selector: 'app-lead-source',
  templateUrl: './lead-source.component.html',
  styleUrls: ['./lead-source.component.scss'],
  providers: [FilterService]
})
export class LeadSourceComponent implements OnInit {

  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;
  rows = [];
  data = [];
  filteredData = [];
  editForm: UntypedFormGroup;
  isEdit: boolean = false;
  leadSourceId: number;
  constructor(
    private fb: UntypedFormBuilder,
    private modalService: NgbModal,
    private metaDataService: MetaDataService,
    private messageService: MessageService,
    private storageService: StorageService,
    private filterService: FilterService
  ) {
    this.editForm = this.fb.group({
      id: new UntypedFormControl(),
      description: ['', [Validators.required, Validators.minLength(3)]],
      name: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.getData();
  }

  /**
   * @method getData
   * @Description get's All Lead Source from the metadaService
   */
  getData() {
    this.metaDataService.getAllLeadSource().subscribe(
      (res: any) => {
        this.data = res;
        this.filteredData = this.data;
      }
    )
  }

  /**
   * @method edit
   * @param leadSource 
   * @param content 
   * @param isEdit
   * @description open's the modal for the Edit and add Lead Source
   */
  editSource(leadSource, content, isEdit) {
    this.editForm.reset();
    this.isEdit = isEdit;
    let ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false,
      ariaLabelledBy: 'modal-basic-title'
    };
    this.modalService.open(content, ngbModalOptions);
    if (isEdit) {
      this.editForm.setValue({
        id: leadSource.id,
        description: leadSource.description,
        name: leadSource.name
      });
    }
  }
  openLeadSource(content: any, data: any) {
    this.leadSourceId = data.id;
    const modaRef = this.modalService.open(content, {
      ariaLabelledBy: '',
      backdrop: false
    });

  }
  /**
   * @method deleteSource
   * @description deactivate the lead source
   */
  deleteSource() {
    this.metaDataService.deactive('leadsource', this.leadSourceId).subscribe(
      res => {
        this.data = this.data.filter(e => e.id != res.id);
        this.messageService.showSuccess("Record Deleted Successfully");
        this.modalService.dismissAll();
      }
    )
  }

  /**
   * @method onEditSave
   * @param form 
   * @description Save's the edit end add leadSoruce Form
   */
  onEditSave(form: UntypedFormGroup) {
    if (form.valid && this.isEdit) {
      // Edit Form Submit
      this.metaDataService.updateLeadSource(form.value).subscribe((res: any) => {
        this.updateObjectInList(res);
        this.editForm.reset();
        this.modalService.dismissAll();
        this.messageService.showSuccess("Record Edited Successfully");
      }
      )
    } else if (form.valid && !this.isEdit) {
      // Add form Submit
      this.metaDataService.createLeadSource(form.value).subscribe(
        res => {
          form.reset();
          this.modalService.dismissAll();
          this.messageService.showSuccess('Record Added Successfully');
          this.storageService.updateMetadata(res, 'LEAD_SOURCE')
          this.data.unshift(res);
          this.data = [...this.data];
        }
      )
    }
  }

  /**
   * @method updateObjectInList
   * @param object 
   * @description add the updated object on the top of the list of Lead Source's
   */
  updateObjectInList(object) {
    const data = this.data.filter(ele => ele.id != object.id);
    data.unshift(object);
    this.data = [...data];
  }

  /**
   * @method filterDatatable
   * @param event 
   * @description search for the filter data table
   */
  filterDatatable(event) {
    this.data = this.filterService.filter(event, this.filteredData, this.data);
    this.table.offset = 0;
  }
}

