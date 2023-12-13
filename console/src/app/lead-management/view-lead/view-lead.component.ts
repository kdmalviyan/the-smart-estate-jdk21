import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';

import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSelect } from '@angular/material/select';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import * as moment from 'moment';
import { MessageService } from 'src/app/core/service/message.service';
import { StorageService } from 'src/app/core/service/storage.service';
import { FollowUpService } from 'src/app/follow-up/follow-up.service';
import { DeactivateLeadComponent } from '../dialogs/deactivate-lead/deactivate-lead.component';
import { FollowupCommentComponent } from '../dialogs/followup-comment/followup-comment.component';
import { FollowupCreateComponent } from '../dialogs/followup-create/followup-create.component';
import { ProjectChangeComponent } from '../dialogs/project-change/project-change.component';
import { LeadManagementService } from '../lead-management.service';
@Component({
  selector: 'app-view-lead',
  templateUrl: './view-lead.component.html',
  styleUrls: ['./view-lead.component.scss']
})
export class ViewLeadComponent implements OnInit, OnDestroy {

  leadId;
  leadDetails: any;
  addCommentForm: UntypedFormGroup;
  isCommentAdding = false;
  commentAdded = false;
  metadata: any;
  invalidDate: any;
  pageParams: any;
  backButtonPath: any;
  currentFollowUp: any;
  status = new UntypedFormControl();
  @ViewChild("statusSelect") statusSelect: MatSelect
  allowEdit: boolean = false;
  showHide: boolean = true;
  advanceFilter:boolean;
  showHideCalender: boolean;
  custName;
  constructor(
    private route: ActivatedRoute,
    public followupCommentdialog: MatDialog,
    public projectChangedialog: MatDialog,
    private leadService: LeadManagementService,
    private messageService: MessageService,
    private storageService: StorageService,
    public leadManagementService: LeadManagementService,
    private fb: UntypedFormBuilder,
    private router: Router,
    private followUpService: FollowUpService,
    public dialog: MatDialog,
  ) {
    this.route.queryParams.subscribe(params => {
      if (params["leadId"]) {
        this.leadId = JSON.parse(params["leadId"]);
      }
      if (params['pageParms']) {
        this.pageParams = JSON.parse(params['pageParms']);
      }
      if (params['backButton']) {
        this.backButtonPath = params['backButton'];
      }
      if (params['isAdvanceFilter']) {
        this.advanceFilter = params['isAdvanceFilter'];
      }
    });
    this.leadDetails = this.leadService.getViewLeadData();
    if (this.leadDetails?.status?.name === 'BOOKED') {
      this.showHideCalender = true;
    } else {
      this.showHideCalender = false;
    }
    this.addCommentForm = this.fb.group({
      id: new UntypedFormControl(),
      message: new UntypedFormControl(),
      createdBy: new UntypedFormControl()
    });

    if (this.leadDetails != undefined) {

      this.currentFollowUp = this.getFollowup(this.leadDetails.followups);
    }

  }

  isInvalidDate(date) {
    var newdate = new Date(new Date().getTime());
    newdate.setHours(0, 0, 0, 0);
    return date.toDate() < newdate;
  }

  ngOnInit(): void {
    this.metadata = this.storageService.getStorage();
    if (!this.leadDetails) {
      this.getLeadData();
    } else {
      this.status.setValue(this.leadDetails.status.id);
    }
  }
  getLeadData() {
    this.leadService.getLeadById(this.leadId).subscribe(
      res => {
        this.leadDetails = res;
        console.log(this.leadDetails)
        this.currentFollowUp = this.getFollowup(this.leadDetails.followups);
        this.status.setValue(this.leadDetails.status.id);
        if (this.leadDetails.status.name === 'BOOKED') {
          this.showHideCalender = true;
        }
        else {
          this.showHideCalender = false;
        }
      }, err => {
        this.messageService.showError(err);
      }
    )
  }

  onDblClick(event: any) {

    if (this.leadDetails?.status?.name !== "BOOKED" && event.type === "dblclick") {
      this.allowEdit = true;
      this.showHide = false;
      this.custName = this.leadDetails.customer.name;
    }
  }
  updateName() {
    if (this.leadDetails.customer.name !== this.custName && this.custName !== '') {
      const customerName = this.custName;
      const customer = this.leadDetails.customer;
      customer.name = customerName;
      this.leadManagementService.updateCustomerName(customer).subscribe((res: any) => {
        this.allowEdit = false;
        this.showHide = true;
        this.messageService.showSuccess('Name Updated Successfully');
      });
    }
    else if (this.custName === '') {
      this.messageService.showError('Customer Name Cant be Blank!');
    }
    else {
      this.messageService.showError('Please Update Name!');
    }
  }

  closeInput() {
    this.allowEdit = false;
    this.showHide = true;
  }
  enableAddCommentSection() {
    this.isCommentAdding = true;
    this.commentAdded = false;
  }

  disableAddCommentSection() {
    this.isCommentAdding = false;
  }


  addComment() {
    this.commentAdded = true;
    this.leadManagementService.saveComment(this.leadDetails.id, this.addCommentForm.value).subscribe(res => {
      this.leadDetails.comments.unshift(res);
      this.addCommentForm.reset();
      this.disableAddCommentSection();
    });
  }


  changeStatus(event) {

    const selectedId = this.status.value;
    const selectedStatus = this.metadata.leadStatus.filter(e => e.id === selectedId);
    if (event != null && selectedStatus[0].name === "DEACTIVE") {
      const dialogRef = this.dialog.open(DeactivateLeadComponent);
      dialogRef.afterClosed().subscribe(result => {
        if (result === 'cancel') {
          ///set old status
          this.status.setValue(this.leadDetails.status.id);
        } else if (result != null) {
          const comment = {
            id: null,
            message: result.comment
          }
          result.comment = comment;

          this.leadManagementService.deactivateLead(this.leadDetails.id, result).subscribe(res => {
            this.messageService.showSuccess('Status changed to ' + res.status.description + ' Successfully');
          });
        }

      });
    }
    else if (event != null && selectedStatus[0].name === "FOLLOW-UP-COMPLETE") {
      const dialogRef = this.followupCommentdialog.open(FollowupCommentComponent, {
        width: '350px',
        data: {
          followupdata: this.leadDetails.followups[0],
          leadId: this.leadDetails.id
        }
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result === 'cancel') {
          this.status.setValue(this.leadDetails.status.id);
        } else if (result != null) {
          const followupStatus = this.metadata.leadStatus.filter(e => e.name == "FOLLOW-UP-COMPLETE")[0];
          this.leadDetails.status = followupStatus;
        }
      });
    }
    else if (event != null && selectedStatus[0].name === "FOLLOW-UP") {
      const dialogRef = this.dialog.open(FollowupCreateComponent, {
        data: {
          leadData: this.leadDetails
        }
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result === 'cancel') {
          ///set old status
          this.status.setValue(this.leadDetails.status.id);
        } else if (result != null) {

        }
      });
    }
    else if (event != null && selectedStatus[0].name === "BOOKED") {
      this.messageService.showError("Status can't changed to booking. Pls Create a booking in order to change the Status to Booking");
      this.status.setValue(this.leadDetails.status.id);
    }
    else {
      this.leadDetails.status = selectedStatus[0];
      this.leadManagementService.updateLead(this.leadDetails, 'STATUS_CHANGED').subscribe(res => {
        this.messageService.showSuccess('Status changed to ' + res.status.description + ' Successfully');
      });
    }

  }

  changeSource(event) {
    const selectedSource = this.metadata.leadSources.filter(e => e.id === event);
    if (event != null) {
      this.leadDetails.source = selectedSource[0];
      this.leadManagementService.updateLead(this.leadDetails, 'SOURCE_CHANGED').subscribe(res => {
        this.messageService.showSuccess('Source changed to ' + res.source.description + ' Successfully');
      });
    }
  }


  backToLead() {
    let navigationExtras: NavigationExtras = {
      queryParams: {
        "pageParms": JSON.stringify(this.pageParams),
        "isAdvanceFilter": this.advanceFilter
      }
    };
    this.router.navigate([this.backButtonPath], navigationExtras);
  }


  changeAssigne(event) {
    const selectedUser = this.metadata.users.filter(e => e.id === event);
    if (event != null) {
      this.leadDetails.assignedTo = selectedUser[0];
      this.leadManagementService.updateLead(this.leadDetails, 'USER_ASSIGNED').subscribe(res => {
        this.messageService.showSuccess('Assignee changed to ' + res.assignedTo.name + ' Successfully');
      });
    }
  }

  ngOnDestroy(): void {
    //throw new Error('Method not implemented.');
  }

  choosedDate(event) {
    if (event.startDate !== null || event.endDate !== null) {
      //let followUpData = event.startDate.toDate().toLocaleString();
      let followUpData = moment(event.startDate.toDate()).format('M/DD/yyyy, h:mm:ss A').toLocaleString()
      this.followUpService.addFollowup(this.leadDetails.id, followUpData).subscribe(res => {
        //this.currentFollowUp.unshift(res);
        this.leadDetails.followups.unshift(res);
        this.leadDetails.comments.unshift(this.mapFollowUpToComment(res));

        const followupStatus = this.metadata.leadStatus.filter(e => e.name == "FOLLOW-UP")[0];
        this.leadDetails.status = followupStatus;
        this.status.setValue(this.leadDetails.status.id);
        this.currentFollowUp=res;
        this.messageService.showSuccess("New Follow up created");
      });
    }


  }

  markComplete(followups) {
    const dialogRef = this.followupCommentdialog.open(FollowupCommentComponent,
      {
        width: '350px',
        data: {
          followupdata: followups,
          leadId: this.leadDetails.id
        }
      });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'cancel') {

      } else if (result != null) {
        const followupStatus = this.metadata.leadStatus.filter(e => e.name == "FOLLOW-UP-COMPLETE")[0];
        this.leadDetails.status = followupStatus;
        this.leadDetails.comments.unshift(this.mapFollowUpToComment(result.resultData));
        this.status.setValue(this.leadDetails.status.id);
        this.currentFollowUp=result.resultData;
        console.log("Check")
      }
    });
  }

  mapFollowUpToComment(result: any) {
    var comment = {
      commentType: "Followup",
      createdAt: result.createdAt,
      createdBy: result.createdBy,
      message: result.followupMessage
    }
    return comment;
  }

  setSiteVisit(checked) {
    this.leadDetails.siteVisit = checked;
    this.leadManagementService.updateLead(this.leadDetails, 'SITE_VISIT').subscribe(res => {
      if (checked) {
        this.messageService.showSuccess('Updated to site visited');
      } else {
        this.messageService.showInfo('Updated to Site not visited ');
      }
    });
  }
  setEventClass(event) {
    var className = 'label '
    if (event === 'Assignment') {
      return className + ' bg-yellow m-b-10';
    }
    else if (event === 'Source') {
      return className + ' bg-teal m-b-10';
    }
    else if (event === 'Visit') {
      return className + ' bg-blue m-b-10';
    }
    else if (event === 'Lead Progress') {
      return className + ' bg-green m-b-10';
    }
    else if (event === 'Followup') {
      return className + ' bg-orange m-b-10';
    }
    else if (event === 'Transfer') {
      return className + ' bg-teal m-b-10';
    }
    else if (event === 'Inquiry' || event === 'Remark') {
      return className + ' bg-cyan m-b-10';
    }
    return className + ' bg-red';;
  }

  changeProject(event) {
    const dialogRef = this.projectChangedialog.open(ProjectChangeComponent,
      {
        width: '400px',
        data: {
          changeProject: event,
          leadId: this.leadDetails.id,
          leadDetails: this.leadDetails
        }
      });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'cancel') {
      } else if (result != null) {
        // const followupStatus = this.metadata.leadStatus.filter(e => e.name == "FOLLOW-UP-COMPLETE")[0];
        // this.leadDetails.status = followupStatus;
      }
    });
  }

  goToAddBooking() {
    let navigationExtras: NavigationExtras = {
      queryParams: {
        action: 'add',
        lead: this.leadDetails.id,
        customerPhone: this.leadDetails.customer.phone
      }
    };
    this.router.navigate(['/booking-management/add-booking'], navigationExtras)
  }

  getImagePath(comment) {
    return comment?.createdBy?.profileImageThumbPath != null ? comment?.createdBy?.profileImageThumbPath : 'assets/images/user.jpg';
  }

  getFollowup(followups){
    if(followups!=null && followups!=undefined && followups.length>0){
      return followups[0];
    }
  }

  changeInventory(event) {
    const selectedInventory = this.metadata.leadInventorySize.filter(e => e.id === event);
    if (event != null) {
      this.leadDetails.leadInventorySize = selectedInventory[0];
      this.leadManagementService.updateLead(this.leadDetails, 'INVENTORY_CHANGED').subscribe(res => {
        this.messageService.showSuccess('inventory changed to ' + res.leadInventorySize.description + ' Successfully');
      });
    }
  }
}
