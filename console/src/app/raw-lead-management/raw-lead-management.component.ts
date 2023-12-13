import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { StorageService } from '../core/service/storage.service';

import { MatDialog } from '@angular/material/dialog';
import { LeadManagementService } from '../lead-management/lead-management.service';
import { AddRawLeadComponent } from './add-raw-lead/add-raw-lead.component';
import * as moment from 'moment';
import { DateService } from '../core/service/date-utils';

@Component({
  selector: 'app-raw-lead-management',
  templateUrl: './raw-lead-management.component.html',
  styleUrls: ['./raw-lead-management.component.scss']
})
export class RawLeadManagementComponent implements OnInit {
  rawleadManagmentForm: UntypedFormGroup;
  metadata: any;
  showResponse: boolean = false;
  importResponse: String;
  advanceTable: any | null;
  isUploadSubmitted: boolean = false;
  uploadForm!: UntypedFormGroup;
  rows: any = [];
  loadingIndicator = true;
  range = this.dateservice.ranges;

 
 //STARTS: Dynamic pagination, filters
 lastSearchText = '';
 currentSearchText = '';
 projectNameFilter = '';
 page: any = {
   count: 0, // total count of items
   offset: 0, // Page number
   limit: 10, // number of items per page
   orderBy: 'createdAt',
   orderDir: 'DESC'
 };
 expandedAll = false;
 selectedDateRange;
 dateFilter = {
   startDate: moment().subtract(6, 'days').format('DD/MM/yyyy'),
   endDate: moment(new Date()).format('DD/MM/yyyy')
 };
 isCreatedAt: boolean = true;

 // ENDS: Dynamic pagination, filters

  constructor(
    private modalService: NgbModal,
    private storageService: StorageService,
    public dialog: MatDialog,
    private fb: UntypedFormBuilder,
    private dateservice: DateService,
    private leadManagementService: LeadManagementService
  ) {

    this.uploadForm = this.fb.group({
      file: ['', Validators.required]
    });
  }


  ngOnInit(): void {
    this.metadata = this.storageService.getStorage();
    var pageParams = this.getPageParams(null);
    this.loadData(pageParams);

  }
  isAdminOrSuperAdmin() {
    let loggedInUser = this.metadata.loggedInUser;
    return loggedInUser?.admin || loggedInUser?.superAdmin;
  }

  addNew(row) {
    const dialogRef = this.dialog.open(AddRawLeadComponent, {
      width: '800px',
      data: {
        advanceTable: this.advanceTable,
        action: 'add',
        record: row
      },
      disableClose: true
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        var pageParams = this.getPageParams(null);
        this.loadData(pageParams);
      }
    });
  }

  importLeads(content) {
    this.showResponse = false;
    let ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false,
      size: 'lg'
    };
    this.modalService.open(content, ngbModalOptions);
  }

  //upload file to the server
  uploadFile() {
    this.isUploadSubmitted = true;

    if (this.uploadForm.valid) {
      const param = this.uploadForm.value;
      const formData: FormData = new FormData();
      formData.append('file', param.file);
      this.leadManagementService.uploadRawLeadsWithExcel(formData).subscribe(
        (res) => {
          if (res != null) {
            const response = res as any;
            this.showResponse = true;
            this.importResponse = response;
            this.uploadForm.reset();
            this.loadData(this.getPageParams(null));
          }
          this.isUploadSubmitted = false;
        }, err => {
          this.modalService.dismissAll();
          this.isUploadSubmitted = false;
        }
      )
    }
  }

  loadData(pageParams) {
    this.leadManagementService.pageableRawLeads(pageParams).subscribe((data: any) => {
      this.page.count = (data as any).count;
      this.rows = (data as any).rawLeads;
      this.loadingIndicator = false;
    });
  }

  pageCallback(pageInfo: { count?: number, pageSize?: number, limit?: number, offset?: number }) {
    this.page.offset = pageInfo.offset;
    this.reloadTable();
  }

  sortCallback(sortInfo: { sorts: { dir: string, prop: string }[], column: {}, prevValue: string, newValue: string }) {
    this.page.orderDir = sortInfo.sorts[0].dir;
    this.page.orderBy = sortInfo.sorts[0].prop;
    this.reloadTable();
  }

  reloadTable() {
    let pageParams = this.getPageParams(null);
    this.loadData(pageParams);
  }

  choosedDate(event) {
    if (event.startDate !== null && event.startDate !== undefined) {
      this.dateFilter.startDate = moment(event.startDate.toDate()).format('DD/MM/yyyy')
      
      if (event.endDate !== null && event.endDate !== undefined) {
        this.dateFilter.endDate = moment(event.endDate.toDate()).format('DD/MM/yyyy')
      } else {
        this.dateFilter.endDate = moment(new Date()).format('DD/MM/yyyy')
      }
      this.loadData(this.getPageParams(null));
    }
  }


  downloadFile() {
    let link = document.createElement("a");
    link.download = "upload_format";
    link.href = "assets/file_format/upload_format.xlsx";
    link.click();
  }
  getPageParams(type) {
    let pageParams = {
      'orderColumn': this.isCreatedAt ? 'createdAt' : 'lastUpdateAt',
      'orderDir': this.page.orderDir,
      'pageNumber': this.page.offset,
      'pageSize': this.setPageSize(type),
      'project': this.projectNameFilter,
    }
    if (this.lastSearchText.length > 2 || this.lastSearchText.length == 0) {
      pageParams["searchText"] = this.lastSearchText;
    }

    if (this.dateFilter.startDate) {
      pageParams["startDate"] = this.dateFilter.startDate
      pageParams["endDate"] = this.dateFilter.endDate;
    }

    return pageParams;
  }

  setPageSize(type) {
    return type == 'download' ? this.page.count : this.page.limit;
  }
  
  searchCallback(event) {
    this.currentSearchText = event.target.value;
    if (this.lastSearchText !== this.currentSearchText) {
      this.lastSearchText = this.currentSearchText;
      if (this.currentSearchText.length > 2 || this.currentSearchText.length == 0) {
        this.page.offset = 0;
        var pageParams = this.getPageParams(null);
        this.loadData(pageParams);
      }
    }
  }

}

