import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { UntypedFormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import * as moment from 'moment';
import { DateService } from '../core/service/date-utils';
import { MessageService } from '../core/service/message.service';
import { StorageService } from '../core/service/storage.service';
import { AdvanceFilterComponent } from '../lead-management/dialogs/advance-filter/advance-filter.component';
import { DeactivateLeadComponent } from '../lead-management/dialogs/deactivate-lead/deactivate-lead.component';
import { FollowupCommentComponent } from '../lead-management/dialogs/followup-comment/followup-comment.component';
import { FollowupCreateComponent } from '../lead-management/dialogs/followup-create/followup-create.component';
import { Budget } from '../lead-management/lead-management.component';
import { LeadManagementService } from '../lead-management/lead-management.service';
import { UnsubscribeOnDestroyAdapter } from '../shared/UnsubscribeOnDestroyAdapter';
import { FollowUpService } from './follow-up.service';
import { ViewFollowupComponent } from './view-followup/view-followup.component';

@Component({
  selector: 'app-follow-up',
  templateUrl: './follow-up.component.html',
  styleUrls: ['./follow-up.component.css']
})
export class FollowUpComponent extends UnsubscribeOnDestroyAdapter
  implements OnInit {
  isOpen = true;
  //STARTS: Dynamic pagination, filters
  lastSearchText = '';
  currentSearchText = '';
  assignedToFilter = 'NO';
  typeFilter = 'NO';
  statusFilter = 'FOLLOW';
  projectNameFilter = '';
  inventorySizeFilter = '';
  deactivationReasonFilter = '';
  page: any = {
    count: 0, // total count of items
    offset: 0, // Page number
    limit: 20, // number of items per page
    orderBy: 'createdAt',
    orderDir: 'DESC'
  };
  budget: Budget = {
    startAmount: 1,
    startUnit: 'LAC',
    endAmount: 800,
    endUnit: 'CRORE'
  }
  expandedAll = false;
  rows: any = [];
  selectedDateRange;
  dateFilter = {
    startDate: moment().subtract(0, 'days').format('DD/MM/yyyy'),
    endDate: moment(new Date()).format('DD/MM/yyyy')
  };
  // ENDS: Dynamic pagination, filters

  id: number;
  advanceTable: any | null;
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;

  metadata: any;
  filteredFollowUpStatus: any;
  loadingIndicator = true;
  private pageParams: any;
  private backFromLeadView = false;
  status = new UntypedFormControl();
  range = this.dateservice.ranges;


  isUploadSubmitted: boolean = false;
  constructor(public httpClient: HttpClient,
    public dialog: MatDialog,
    public followupCommentdialog: MatDialog,
    public projectChangedialog: MatDialog,
    private storageService: StorageService,
    public viewLeadDialog: MatDialog,
    private followUpService: FollowUpService,
    private messageService: MessageService,
    public leadManagementService: LeadManagementService,
    private router: Router,
    private route: ActivatedRoute,
    private elem: ElementRef,
    private dateservice: DateService
  ) {
    super();

    this.route.queryParams.subscribe(params => {
      if (params['pageParms']) {
        this.pageParams = JSON.parse(params['pageParms']);
        this.parseFilterParams(this.pageParams);
        this.backFromLeadView = true;
      }
    });
  }

  parseFilterParams(filterParams) {
    Object.keys(filterParams).forEach(key => {
      if (key === 'pageNumber') {
        this.page.offset = parseInt(filterParams[key]);
      }
      if (key === 'pageSize') {
        this.page.limit = parseInt(filterParams[key]);
      }

      if (key === 'searchText') {
        this.currentSearchText = filterParams[key];
        this.lastSearchText = filterParams[key];
      }

      if (key === 'startDate') {
        this.dateFilter.startDate = filterParams[key];
        // this.selectedDateRange = ;
      }

      if (key === 'endDate') {
        this.dateFilter.endDate = filterParams[key];
        const selected = { startDate: this.dateParser(filterParams['startDate']), endDate: this.dateParser(filterParams[key]) };
        this.selectedDateRange = selected;
      }

      if (key === 'assignedTo') {
        this.assignedToFilter = filterParams[key];
      }

      if (key === 'status') {
        this.statusFilter = filterParams[key];
      }
      if (key === 'projectName') {
        this.projectNameFilter = filterParams[key];
      }
      if (key === 'inventorySize') {
        this.inventorySizeFilter = filterParams[key];
      }
      if (key === 'deactivationReason') {
        this.deactivationReasonFilter = filterParams[key];
      }
    });
  }

  ngOnInit() {
    this.reloadTable();
    this.metadata = this.storageService.getStorage();
    this.filteredFollowUpStatus = this.metadata.leadStatus.filter(e => e.name.startsWith("FOLLOW"));

  }

  refresh() {
    this.reloadTable();
  }


  // oepn edit lead
  gotoLead(row) {
    let navigationExtras: NavigationExtras = {
      queryParams: {
        "leadId": JSON.stringify(row.lead.id),
        "pageParms": JSON.stringify(this.getPageParams(null)),
        "backButton": "follow-up"
      }
    };
    this.leadManagementService.setViewLeadData(row.lead, [row]);
    this.router.navigate(['/follow-up/view'], navigationExtras);
  }


  changeStatus(event, leadData, statusInput: HTMLSelectElement) {
    const selectedStatus = this.metadata.leadStatus.filter(e => e.id === event);
    if (event != null && selectedStatus[0].name === "DEACTIVE") {
      const dialogRef = this.dialog.open(DeactivateLeadComponent);
      dialogRef.afterClosed().subscribe(result => {
        if (result === 'cancel') {
          ///set old status
          statusInput.selectedIndex = this.metadata.leadStatus.filter(e => e.name === leadData.status.name)[0].id;
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
          statusInput.selectedIndex = this.metadata.leadStatus.filter(e => e.name === leadData.status.name)[0].id;
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
          statusInput.selectedIndex = this.metadata.leadStatus.filter(e => e.name === leadData.status.name)[0].id;
        } else if (result != null) {
          console.log("");
        }
      });
    }

    else {
      leadData.status = selectedStatus[0];
      this.leadManagementService.updateLead(leadData, 'STATUS_CHANGED').subscribe(res => {
        this.pageCallback({ offset: 0 });
        this.messageService.showSuccess('Status changed to ' + res.status.description + ' Successfully');
      });
    }

  }

  checkFollowupPending(followup) {
    if (followup != undefined) {
      if ((new Date(followup.followupTime) < new Date()) && followup.open) {
        return "label label-danger";
      }
    }
  }

  onSelect(event) {
    this.viewLeadDialog.open(ViewFollowupComponent, {
      width: '650px',
      height: '450px',
      data: event
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
    let pageParams = this.getPageParams(null);
    if (this.backFromLeadView === true) {
      pageParams = this.pageParams;
      this.backFromLeadView = false;
    }
    this.loadData(pageParams);
  }

  loadData(pageParams) {
    this.followUpService.getPageableFollowups(pageParams).subscribe((data) => {
      this.page.count = (data as any).count;
      this.rows = (data as any).leads;
      this.loadingIndicator = false;
    });
  }

  getPageParams(type) {
    let pageParams = {
      'orderColumn': this.page.orderBy,
      'orderDir': this.page.orderDir,
      'pageNumber': this.page.offset,
      'pageSize': this.setPageSize(type),
      'assignedTo': this.assignedToFilter,
      'status': this.statusFilter,
      'project': this.projectNameFilter,
      'leadInventorySize': this.inventorySizeFilter,
      'deactivationReason': this.deactivationReasonFilter,
      'type': this.typeFilter,
      'budget': this.budget,
      'open': this.isOpen
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
    const dialogRef = this.dialog.open(AdvanceFilterComponent);
    dialogRef.afterClosed().subscribe(result => {

      if (result === 'cancel') {
        ///set old status
      } else if (result != null) {
        this.deactivationReasonFilter = result.deactivationReason;
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
    this.statusFilter = 'FOLLOW';
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
    // this.isAdvanceFilter = false;
  }


  isAdminOrSuperAdmin() {
    let loggedInUser = this.metadata.loggedInUser;
    return loggedInUser.admin || loggedInUser.superAdmin;
  }

  dateParser(date) {
    const [day, month, year] = date.split('/');
    return new Date(+year, month - 1, +day);
  }
  changeOpenFolloups() {
    this.isOpen = !this.isOpen;
    this.page.offset = 0;
    var pageParams = this.getPageParams(null);
    this.loadData(pageParams);
  }
}