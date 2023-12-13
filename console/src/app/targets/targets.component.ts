import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { AgGridAngular } from 'ag-grid-angular';
import { ColDef } from 'ag-grid-community';
import { FilterService } from '../core/service/filter.service';
import { MessageService } from '../core/service/message.service';
import { StorageService } from '../core/service/storage.service';
import { AddTargetComponent } from './add-target/add-target.component';
import { DeleteTargetDialogComponent } from './delete/delete.component';
import { TargetsService } from './targets.service';

@Component({
  selector: 'app-targets',
  templateUrl: './targets.component.html',
  styleUrls: ['./targets.component.sass']
})
export class TargetsComponent implements OnInit {
  data = [];
  filteredData = [];
  metadata: any;
  enableGroupBy = false;
  @ViewChild('agGrid') agGrid: AgGridAngular;
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;
  selectedColumns = ['createdBy.name', 'project.name', 'bookingCount',
    'bookingDoneCount', 'siteVisitCount', 'siteVisitDoneCount',
    'startDate', 'endDate'];

  hideColumns = [];
  showColumns=[];
  //--------------ag-grid----------------
  public defaultColDef: ColDef = {
    flex: 1,
    sortable: true,
    filter: true,
    // enable floating filters by default
    floatingFilter: true,
  };

  targetColumns = [
    { headerName: 'Employee', field: 'employeeName', sortable: true },
    { headerName: 'Project', field: 'project.name', sortable: true },
    { headerName: 'Booking Set', field: 'bookingCount', sortable: true ,filter: 'agNumberColumnFilter' },
    { headerName: 'Booking Done', field: 'bookingDoneCount', sortable: true ,filter: 'agNumberColumnFilter' },
    { headerName: 'Site Visited', field: 'siteVisitCount' ,filter: 'agNumberColumnFilter' },
    { headerName: 'Site Visited Done', field: 'siteVisitDoneCount' ,filter: 'agNumberColumnFilter' },
    { headerName: 'Start Day', field: 'startDate', sortable: true },
    { headerName: 'End Day', field: 'endDate', sortable: true },
  ];

  constructor(private filterService: FilterService,
    private modalService: NgbModal,
    public dialog: MatDialog,
    private targetService: TargetsService,
    private messageService: MessageService,
    private storageService: StorageService,
  ) {
    this.metadata = this.storageService.getStorage();
    this.showColumns=this.selectedColumns;
  }

  ngOnInit(): void {
    this.getData(false);
  }
  refresh() {
    this.getData(false);
  }

  removeItem(event) {
    this.hideColumns.push(event.value);
    this.HideModel()
    this.showColumns=this.removeFromList(this.showColumns,event.value);
  }

  addItem(event) {
    this.showColumns.push(event);
    this.ShowModel();
    this.hideColumns=this.removeFromList(this.hideColumns,event);

  }

  removeFromList(array, value) {
    return array.filter(val => val != value);
  }
  
  HideModel() {
    this.agGrid.columnApi.setColumnsVisible(this.hideColumns, false);
    this.agGrid.api.sizeColumnsToFit();
  }

  ShowModel() {
    this.agGrid.columnApi.setColumnsVisible(this.showColumns, true);
    this.agGrid.api.sizeColumnsToFit();
  }


  /**
   * @method getData
   * @Description get's All Lead Source from the metadaService
   */
  getData(groupBy) {
    this.targetService.getAllTargets(groupBy).subscribe(
      (res: any) => {
        this.data = res;
        this.filteredData = this.data;
      }
    )
  }

  changeToGroupBy(event) {
    this.getData(event.checked);
  }

  /**
   * @method addMenu
   * @param modelId 
   * @description open's the modal to add menu
   */

  addTarget() {

    const dialogRef = this.dialog.open(AddTargetComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'cancel') {
        this.dialog.closeAll();
      } else if (result != null) {
        this.data.unshift(result);
        this.data = [...this.data];
      }
    });


  }
  /**
 * @method close
 * @description closes all the opened ngBModal
 */
  close() {
    this.modalService.dismissAll();
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

  isAdminOrSuperAdmin() {
    let loggedInUser = this.metadata.loggedInUser;
    return loggedInUser.admin || loggedInUser.superAdmin;
  }

  deleteTarget(row) {

    const dialogRef = this.dialog.open(DeleteTargetDialogComponent, {
      data: {
        id: row.id,
        content: row
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {

      if (confirmed) {
        this.messageService.showSuccess("Deleted successfully")
      }
    });
  }
}
