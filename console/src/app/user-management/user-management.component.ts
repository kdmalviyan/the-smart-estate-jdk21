import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { FilterService } from '../core/service/filter.service';
import { MessageService } from '../core/service/message.service';
import { StorageService } from '../core/service/storage.service';
import { AddUserComponent } from './dialogs/add-user/add-user.component';
import { UserManagementService } from './user-management.service';

@Component({
  selector: 'app-advance-table',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css'],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'en-GB' }]
})
export class UserManagementComponent implements OnInit {

  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;
  currentRow: any;
  displayedColumns = [
    'name',
    'username',
    'email',
    'gender',
    'mobile',
    'address',
    'actions'
  ];
  dataSource: any[];
  id: number;
  filteredData: any[];
  loadingIndicator = true;
  changePasswordForm!: UntypedFormGroup;
  constructor(
    public httpClient: HttpClient,
    public dialog: MatDialog,
    private messageService: MessageService,
    private modalService: NgbModal,
    private fb: UntypedFormBuilder,
    private userManagementService: UserManagementService,
    private storage: StorageService,
    private filterService: FilterService
  ) {

    this.changePasswordForm = this.fb.group({
      password: [null, Validators.required]
    });

  }
  contextMenuPosition = { x: '0px', y: '0px' };

  ngOnInit() {
    this.loadData();
  }
  refresh() {
    this.loadData();
  }


  public loadData() {
    this.userManagementService.getAllUsers().subscribe((res: any) => {
      this.dataSource = res as any;
      this.filteredData = this.dataSource;
      this.loadingIndicator = false;
    });;

  }

  addNew() {
    let tempDirection;
    if (localStorage.getItem('isRtl') === 'true') {
      tempDirection = 'rtl';
    } else {
      tempDirection = 'ltr';
    }

    const dialogRef = this.dialog.open(AddUserComponent, {
      data: {
        advanceTable: this.dataSource,
        action: 'add'
      },
      direction: tempDirection,
      disableClose: true
    });


    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'closed') {
        // After dialog is closed we're doing frontend updates
        // For add we're just pushing a new row inside DataService
        var data = this.userManagementService.getDialogData();
        this.dataSource.unshift(data);
        this.storage.setUsers(this.removeSuperAdmin(this.dataSource));

        this.dataSource = [...this.dataSource];
        //this.refreshTable();

        this.messageService.showSuccess('User Added Successfully.')
        this.storage.updateMetadata(data, 'USER');
      }
    });
  }


  editUser(row) {
    this.id = row.id;
    let tempDirection;
    if (localStorage.getItem('isRtl') === 'true') {
      tempDirection = 'rtl';
    } else {
      tempDirection = 'ltr';
    }

    this.dialog.open(AddUserComponent, {
      data: {
        userData: row,
        action: 'edit'
      },
      direction: tempDirection,
      disableClose: true
    });

  }



  changeStatus(event, row) {
    let user = row;
    user.enabled = event.checked;

    this.userManagementService.update(user).subscribe((res: any) => {
      var data: any;
      data = res;
      if (data.enabled) {
        this.messageService.showSuccess("User " + data.name + " Enabled successfully")
      } else {
        this.messageService.showSuccess("User " + data.name + "  Disabled successfully")
      }

    });
  }


  filterDatatable(event) {
    this.dataSource = this.filterService.filter(event, this.filteredData, this.dataSource);
    // whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }

  changePasswordOpen(content, row) {
    this.currentRow = row;
    this.modalService.open(content);
  }

  changePassword() {
    if (this.changePasswordForm.valid) {
      const param = this.changePasswordForm.value;
      const user = this.currentRow;

      param.username = user.username;
      this.userManagementService.changePassword(param).subscribe(
        res => {
          this.messageService.showSuccess("Password Changed successfully")
          this.modalService.dismissAll();
        }
      )
    }
  }

  removeSuperAdmin(arr) {
    return arr = arr.filter(ele => {
      return ele.username != 'superadmin'
    })
  }

  getImagePath(user) {
  //  console.log(user.profileImageThumbPath != null ? user.profileImageThumbPath : 'assets/images/user.jpg')
    return user.profileImageThumbPath != null ? user.profileImageThumbPath : 'assets/images/user.jpg';
  }

}
