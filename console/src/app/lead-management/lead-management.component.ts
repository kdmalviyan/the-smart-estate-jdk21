import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';

import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent, SelectionType } from '@swimlane/ngx-datatable';
import * as FileSaver from 'file-saver';
import * as moment from 'moment';

import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from 'src/environments/environment';
import { DateService } from '../core/service/date-utils';
import { MessageService } from '../core/service/message.service';
import { StorageService } from '../core/service/storage.service';
import { UnsubscribeOnDestroyAdapter } from '../shared/UnsubscribeOnDestroyAdapter';
import { AddEditLeadComponent } from './dialogs/add-edit-lead/add-edit-lead.component';
import { AdvanceFilterComponent } from './dialogs/advance-filter/advance-filter.component';
import { DeactivateLeadComponent } from './dialogs/deactivate-lead/deactivate-lead.component';
import { DownloadReportComponent } from './dialogs/download-report/download-report.component';
import { FollowupCommentComponent } from './dialogs/followup-comment/followup-comment.component';
import { FollowupCreateComponent } from './dialogs/followup-create/followup-create.component';
import { ProjectChangeComponent } from './dialogs/project-change/project-change.component';
import { LeadManagementService } from './lead-management.service';

@Component({
  selector: 'app-lead-management',
  templateUrl: './lead-management.component.html',
  styleUrls: ['./lead-management.component.scss'],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'en-GB' }]
})
export class LeadManagementComponent
  extends UnsubscribeOnDestroyAdapter
  implements OnInit, AfterViewInit {
  //STARTS: Dynamic pagination, filters
  lastSearchText = '';
  currentSearchText = '';
  assignedToFilter = 'NO';
  typeFilter = 'NO';
  statusFilter = '';
  projectNameFilter = '';
  inventorySizeFilter = '';
  deactivationReasonFilter = '';
  sourceFilter = '';
  page: any = {
    count: 0, // total count of items
    offset: 0, // Page number
    limit: 20, // number of items per page
    orderBy: 'createdAt',
    orderDir: 'DESC'
  };
  expandedAll = false;
  rows: any = [];
  selectedDateRange;
  dateFilter = {
    startDate: moment().subtract(6, 'days').format('DD/MM/yyyy'),
    endDate: moment(new Date()).format('DD/MM/yyyy')
  };
  // ENDS: Dynamic pagination, filters

  id: number;
  advanceTable: any | null;
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;

  metadata: any;
  loadingIndicator = true;
  transferLeadForm!: UntypedFormGroup;
  SelectionType = SelectionType;
  selectedLeads = [];
  // file upload
  uploadForm!: UntypedFormGroup;
  importResponse: String;
  showResponse: boolean = false;
  private pageParams: any;
  private backFromLeadView = false;
  status = new UntypedFormControl();
  range = this.dateservice.ranges;
  transferErrors = [];
  budget: Budget = {
    startAmount: 0,
    startUnit: 'LAC',
    endAmount: 800,
    endUnit: 'CRORE'
  }

  filteredUsers = [];
  isAdvanceFilter: boolean = false;
  isUploadSubmitted: boolean = false;
  isCreatedAt: boolean = true;
  transferMessage:boolean=false;

  public openMenu: boolean = false;
  isOver = false;

  constructor(public httpClient: HttpClient,
    public dialog: MatDialog,
    public followupCommentdialog: MatDialog,
    public projectChangedialog: MatDialog,
    private storageService: StorageService,
    private messageService: MessageService,
    public leadManagementService: LeadManagementService,
    private modalService: NgbModal,
    private router: Router,
    private fb: UntypedFormBuilder,
    private spinner: NgxSpinnerService,
    private route: ActivatedRoute,
    private elem: ElementRef,
    private dateservice: DateService
  ) {
    super();

    this.transferLeadForm = this.fb.group({
      assignedTo: [null, Validators.required],
      project: [null, Validators.required],
      leadTransferType: [null, Validators.required],
    });

    this.uploadForm = this.fb.group({
      file: ['', Validators.required]
    });

    this.route.queryParams.subscribe(params => {
      if (params['pageParms']) {
        this.pageParams = JSON.parse(params['pageParms']);
        this.parseFilterParams(this.pageParams);
        if(params.isAdvanceFilter!=undefined){
          this.isAdvanceFilter =JSON.parse(params.isAdvanceFilter)
        }
        this.backFromLeadView = true;
      }
    });
  }
  clickMenu(){
    this.openMenu = !this.openMenu;
  }


  parseFilterParams(filterParams) {
    //const filterKayValuePair = filterParams.split("&");
    let filter_param = filterParams;

    Object.keys(filter_param).forEach(key => {
      if (key === 'pageNumber') {
        this.page.offset = parseInt(filter_param[key]);
      }
      if (key === 'pageSize') {
        this.page.limit = parseInt(filter_param[key]);
      }

      if (key === 'searchText') {
        this.currentSearchText = filter_param[key];
        this.lastSearchText = filter_param[key];
      }

      if (key === 'startDate') {
        this.dateFilter.startDate = filter_param[key];
      }

      if (key === 'endDate') {
        this.dateFilter.endDate = filter_param[key];
        const selected = { startDate: this.dateParser(filterParams['startDate']), endDate: this.dateParser(filterParams[key]) };
        this.selectedDateRange = selected;
      }

      if (key === 'assignedTo') {
        this.assignedToFilter = filter_param[key];
      }

      if (key === 'status') {
        this.statusFilter = filter_param[key];
      }

      if (key === 'type') {
        this.typeFilter = filter_param[key];
      }
      if (key === 'project') {
        this.projectNameFilter = filter_param[key];
      }
      if (key === 'leadInventorySize') {
        this.inventorySizeFilter = filter_param[key];
      }
      if (key === 'deactivationReason') {
        this.deactivationReasonFilter = filter_param[key];
      }
      if (key === 'source') {
        this.sourceFilter = filter_param[key];
      }
      if (key === 'budget') {
        this.budget = filter_param[key];
      }

      if (key === 'orderColumn') {
        this.isCreatedAt = filter_param[key] === 'lastUpdateAt' ? false : true;
      }

    });
  }

  ngOnInit() {
    //We dont need reloadTable() here table load call get hit by ngx-datatable by method pageCallback()
    // this.reloadTable();
    this.metadata = this.storageService.getStorage();
  }

  isAdminOrSuperAdmin() {
    // this.metadata = this.storageService.getStorage();
    let loggedInUser = this.metadata.loggedInUser;
    return loggedInUser?.admin || loggedInUser?.superAdmin;
  }

  ngAfterViewInit(): void {
    //this.metadata.users = this.metadata.users;
  }


  refresh() {
    this.reloadTable();
  }

  transferLeads(content) {
    if (this.selectedLeads.length == 0) {
      return this.messageService.showInfo("Select leads to transfer");
    }
    this.transferErrors=[];
    this.transferMessage=false;
    this.transferLeadForm.reset();
    let ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false
    };
    this.modalService.open(content, ngbModalOptions);
  }

  addNew() {
    const dialogRef = this.dialog.open(AddEditLeadComponent, {
      width: '800px',
      data: {
        advanceTable: this.advanceTable,
        action: 'add'
      },
      disableClose: true
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.pageCallback({ offset: 0 });
       // this.openMenu = !this.openMenu;
      }
    });
  }


  // oepn edit lead
  editLead(row) {

    let navigationExtras: NavigationExtras = {
      queryParams: {
        "leadId": JSON.stringify(row.id),
        "pageParms": JSON.stringify(this.getPageParams(null)),
        "backButton": "lead-management",
        "isAdvanceFilter": this.isAdvanceFilter

      }
    };
    this.leadManagementService.setViewLeadData(row, row.followups);
    this.router.navigate(['/lead-management/view'], navigationExtras);
  }


  changeStatus(event, leadData, statusInput: HTMLSelectElement) {

    const selectedStatus = this.metadata.leadStatus.filter(e => e.id === event);
    if (event != null && selectedStatus[0].name === "DEACTIVE") {
      const dialogRef = this.dialog.open(DeactivateLeadComponent);
      dialogRef.afterClosed().subscribe(result => {
        if (result === 'cancel') {
          ///set old status
          statusInput.selectedIndex = this.metadata.leadStatus.findIndex(e => e.name === leadData.status.name) + 1;
        } else if (result != null) {
          const comment = {
            id: null,
            message: result.comment
          }
          result.comment = comment;

          this.leadManagementService.deactivateLead(leadData.id, result).subscribe(res => {
            this.pageCallback({ offset: 0 });
            this.messageService.showSuccess('Status changed to ' + res.status.description + ' Successfully');
          });
        }

      });
    }
    else if (event != null && selectedStatus[0].name === "FOLLOW-UP-COMPLETE") {
      const dialogRef = this.followupCommentdialog.open(FollowupCommentComponent, {
        width: '350px',
        data: {
          followupdata: leadData.followups[0],
          leadId: leadData.id
        }
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result === 'cancel') {
          statusInput.selectedIndex = this.metadata.leadStatus.findIndex(e => e.name === leadData.status.name) + 1;
        } else if (result != null) {
          const followupStatus = this.metadata.leadStatus.filter(e => e.name == "FOLLOW-UP-COMPLETE")[0];
          leadData.status = followupStatus;
        }
      });
    } else if (event != null && selectedStatus[0].name === "FOLLOW-UP") {
      const dialogRef = this.dialog.open(FollowupCreateComponent, {
        data: {
          leadData: leadData
        }
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result === 'cancel') {
          ///set old status
          statusInput.selectedIndex = this.metadata.leadStatus.findIndex(e => e.name === leadData.status.name) + 1;
        } else if (result != null) {

        }
      });
    } else if (event != null && selectedStatus[0].name === "BOOKED") {
      this.messageService.showError("Status can't changed to booking. Pls Create a booking in order to change the Status to Booking");
      statusInput.selectedIndex = this.metadata.leadStatus.findIndex(e => e.name === leadData.status.name) + 1;
    }

    else {
      leadData.status = selectedStatus[0];
      this.leadManagementService.updateLead(leadData, 'STATUS_CHANGED').subscribe(res => {
        this.pageCallback({ offset: 0 });
        this.messageService.showSuccess('Status changed to ' + res.status.description + ' Successfully');
      });
    }

  }

  changeProject(event, leadData) {

    const dialogRef = this.projectChangedialog.open(ProjectChangeComponent,
      {
        width: '400px',
        data: {
          changeProject: event,
          leadId: leadData.id,
          leadDetails: leadData
        }
      });

    dialogRef.afterClosed().subscribe(result => {

      if (result === 'cancel') {

      } else if (result != null) {
        this.pageCallback({ offset: 0 });
      }
    });
  }


  changeAssigne(event, leadData) {
    const selectedUser = this.metadata.users.filter(e => e.id === event);
    if (event != null) {
      leadData.assignedTo = selectedUser[0];
      this.leadManagementService.updateLead(leadData, 'USER_ASSIGNED').subscribe(res => {
        this.pageCallback({ offset: 0 });
        this.messageService.showSuccess('Assignee changed to ' + res.assignedTo.name + ' Successfully');
      });
    }
  }

  changeLeadType(event, leadData) {
    const selectedType = this.metadata.leadType.filter(e => e.id === event);
    if (event != null) {
      leadData.type = selectedType[0];
      this.leadManagementService.updateLead(leadData, 'LEAD_TYPE_CHANGED').subscribe(res => {
        this.pageCallback({ offset: 0 });
        this.messageService.showSuccess('Lead Type changed to ' + res.type.description + ' Successfully');
      });
    }
  }

  exportExcel() {
    this.leadManagementService.downloadOtp().subscribe((data) => {
      this.enterOTPAndDownloadReport();
    })
  }


  enterOTPAndDownloadReport() {

    const dialogRef = this.projectChangedialog.open(DownloadReportComponent,
      {
        width: '400px',
      });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'cancel') {
        console.log("cancel event");
      } else if (result == "success") {
        //validate otp success
        var exportData = [];
        this.loadingIndicator = true;
        let pageParams = this.getPageParams('download');
        this.leadManagementService.exportPageableLeads(pageParams).subscribe((data) => {
          let rows = (data as any);
          exportData = this.leadManagementService.createLeadDataForExport(rows);
          if (exportData.length > 0) {
            import("xlsx").then(xlsx => {
              const worksheet = xlsx.utils.json_to_sheet(exportData);
              const workbook = { Sheets: { 'data': worksheet }, SheetNames: ['data'] };
              const excelBuffer: any = xlsx.write(workbook, { bookType: 'xlsx', type: 'array' });
              this.saveAsExcelFile(excelBuffer, "ExportExcel");
            });
          }
          this.loadingIndicator = false;
        });
      }
    });



  }

  saveAsExcelFile(buffer: any, fileName: string): void {
    this.loadingIndicator = true;
    let EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
    let EXCEL_EXTENSION = '.xlsx';
    const data: Blob = new Blob([buffer], {
      type: EXCEL_TYPE
    });
    FileSaver.saveAs(data, fileName + '_export_' + new Date().getTime() + EXCEL_EXTENSION);
    this.loadingIndicator = false;
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
      this.leadManagementService.uploadLeadsWithExcel(formData).subscribe(
        (res) => {
          if (res != null) {
            const response = res as any;
            this.showResponse = true;
            this.importResponse = response;
            this.uploadForm.reset();
            this.reloadTable()
          }
          this.isUploadSubmitted = false;
        }, err => {
          this.modalService.dismissAll();
          this.isUploadSubmitted = false;
        }
      )
    }
  }


  transferLeadToUser() {
    var data = this.transferLeadForm.value
    data.assignedTo = this.metadata.users.filter(e => e.id === data.assignedTo)[0];
    data.project = this.metadata.projects.filter(e => e.id === data.project)[0];
    data.leadList = this.selectedLeads.map(e => e.id);
    this.transferMessage=false;
    this.leadManagementService.transferLeads(data, 'TRANSFER_LEAD').subscribe(res => {
      this.pageCallback({ offset: 0 });
      this.transferErrors = res.errors;
      if(this.transferErrors.length==0){
        this.transferMessage=true;
      }
      // this.messageService.showSuccess('Leads transfered to ' + data.assignedTo.name + ' Successfully');
      //this.modalService.dismissAll();
      this.selectedLeads = [];
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

  assignedToFilterCallback(value) {
    this.assignedToFilter = value;
    this.page.offset = 0;
    var pageParams = this.getPageParams(null);
    this.loadData(pageParams);
  }

  statusCallbackFilter(value) {
    this.statusFilter = value;
    this.page.offset = 0;
    var pageParams = this.getPageParams(null);
    this.loadData(pageParams);
  }

  typeCallbackFilter(value) {
    this.typeFilter = value;
    this.page.offset = 0;
    var pageParams = this.getPageParams(null);
    this.loadData(pageParams);
  }

  projectCallbackFilter(value) {
    this.projectNameFilter = value;
    this.page.offset = 0;
    var pageParams = this.getPageParams(null);
    this.loadData(pageParams);
  }

  inventoryCallbackFilter(value) {
    this.inventorySizeFilter = value;
    this.page.offset = 0;
    var pageParams = this.getPageParams(null);
    this.loadData(pageParams);
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

  reloadTable() {

    this.selectedLeads = [];
    let pageParams = this.getPageParams(null);
    if (this.backFromLeadView === true) {
      pageParams = this.pageParams;
      this.backFromLeadView = false;
    }
    this.loadData(pageParams);
  }

  loadData(pageParams) {
    this.leadManagementService.pageableLeads(pageParams).subscribe((data: any) => {
      this.page.count = (data as any).count;
      this.rows = (data as any).leads;
      this.loadingIndicator = false;

    });
  }

  getPageParams(type) {
    let pageParams = {
      'orderColumn': this.isCreatedAt ? 'createdAt' : 'lastUpdateAt',
      'orderDir': this.page.orderDir,
      'pageNumber': this.page.offset,
      'pageSize': this.setPageSize(type),
      'assignedTo': this.assignedToFilter,
      'status': this.statusFilter,
      'project': this.projectNameFilter,
      'leadInventorySize': this.inventorySizeFilter,
      'deactivationReason': this.deactivationReasonFilter,
      'source': this.sourceFilter,
      'type': this.typeFilter,
      'budget': this.budget
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

  addFilterParam(pageParams, paramName, paramValue) {
    return pageParams + '&' + paramName + '=' + paramValue;
  }

  onLeadSelect({ selected }) {
    this.selectedLeads = [];
    this.selectedLeads.push(...selected);
  }

  downloadFile() {
    let link = document.createElement("a");
    link.download = "upload_format";
    link.href = "assets/file_format/upload_format.xlsx";
    link.click();
  }

  setSiteVisit(checked, row) {
    row.siteVisit = checked;
    this.leadManagementService.updateLead(row, 'SITE_VISIT').subscribe(res => {
      if (checked) {
        this.messageService.showSuccess('Updated to site visited');
      } else {
        this.messageService.showInfo('Updated to Site not visited ');
      }
    });
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

  goToLead(id) {
    var url = environment.frontendUrl + "/#/lead-management/view?leadId=" + id
    window.open(url, "_blank").focus();
  }


  selectedProject(projectId) {
    this.filteredUsers = this.metadata.users.filter(e => e.project.id === projectId);
  }

  onDetailToggle(event) {
  }
  toggleExpandRow(row) {
    this.table.rowDetail.toggleExpandRow(row);
  }

  toggleExpandAllRows(event) {
    this.expandedAll = !this.expandedAll;
    if (this.expandedAll) {
      this.table.rowDetail.expandAllRows();
    } else {
      this.table.rowDetail.collapseAllRows();
    }
  }

  getClassStatusColor(item) {
    if (item == "ACTIVE") {
      return "col-blue";
    } else if (item == "IN_PROGRESS") {
      return "col-orange";
    } else if (item == "BOOKED") {

      return "col-green";
    } else if (item == "FOLLOW-UP") {
      return "col-red";
    } else if (item == "FOLLOW-UP-EXPIRE") {
      return "col-red";
    } else if (item == "DEACTIVE") {
      return "col-cyan";
    }
  }

  addAdvanceFilter() {
    const dialogRef = this.dialog.open(AdvanceFilterComponent, {
      width: '600px'
    });
    dialogRef.afterClosed().subscribe(result => {

      if (result === 'cancel') {
        ///set old status
      } else if (result != null) {
        this.isAdvanceFilter = true;
        this.deactivationReasonFilter = result.deactivationReason;
        this.sourceFilter = result.source.name;
        if (result.budget.startAmount != '' && result.budget.endAmount != '') {
          this.budget = result.budget;
        }
        this.page.offset = 0;
        var pageParams = this.getPageParams(null);
        this.loadData(pageParams);
      }
    });
  }


  clearFilters() {
    this.lastSearchText = '';
    this.currentSearchText = '';
    this.assignedToFilter = 'NO';
    this.typeFilter = 'NO';
    this.statusFilter = '';
    this.sourceFilter = '';

    this.projectNameFilter = '';
    this.inventorySizeFilter = '';
    this.deactivationReasonFilter = '';
    this.page = {
      count: 0, // total count of items
      offset: 0, // Page number
      limit: 20, // number of items per page
      orderBy: 'createdAt',
      orderDir: 'DESC'
    };
    this.budget = {
      startAmount: 1,
      startUnit: 'LAC',
      endAmount: 800,
      endUnit: 'CRORE'
    }
    this.selectedDateRange = null; // now we can do
    var filtersDropdown = this.elem.nativeElement.querySelectorAll('.filtervalue');
    filtersDropdown.forEach(filter => {
      filter.options.selectedIndex = 0;
    });

    var pageParams = this.getPageParams(null);
    this.loadData(pageParams);
    this.isAdvanceFilter = false;
  }

  changeSort() {
    this.isCreatedAt = !this.isCreatedAt;
    this.loadData(this.getPageParams(null));
  }

  dateParser(date) {
    const [day, month, year] = date.split('/');
    return new Date(+year, month - 1, +day);
  }
}


export interface Budget {
  startAmount: number,
  startUnit: string,
  endAmount: number,
  endUnit: string
}