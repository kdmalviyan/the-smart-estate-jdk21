import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { AddVactionComponent } from './add-vaction/add-vaction.component';

@Component({
  selector: 'app-vacation-management',
  templateUrl: './vacation-management.component.html',
  styleUrls: ['./vacation-management.component.sass']
})
export class VacationManagementComponent implements OnInit {
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;
  constructor(
    private dialog:MatDialog
  ) { }

  ngOnInit(): void {
  }
  // filterDatatable(event) {
  //   this.vacationTypeData = this.filterService.filter(event, this.dataSource, this.vacationTypeData);
  //   // whenever the filter changes, always go back to the first page
  //   this.table.offset = 0;
  // }
  openVacationModal() {
    const dialogRef = this.dialog.open(AddVactionComponent, {
      width: '600px',
      data: {
        
      },
      disableClose: true
    });
  }
}
