import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { FilterService } from 'src/app/core/service/filter.service';
import { MetaDataService } from '../meta-data.service';
@Component({
  selector: 'app-ui-tabs',
  templateUrl: './ui-tabs.component.html',
  styleUrls: ['./ui-tabs.component.sass']
})
export class UiTabsComponent implements OnInit {

  permissionTabs: any;
  filteredData = [];
  columnsToSearch = [];
  columns = [
    { name: 'id' },
    { name: 'name' },
    { name: 'description' }
  ];
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;

  constructor(
    private _metaDataService: MetaDataService,
    private _filterService: FilterService,
    private modalService: NgbModal,
  ) { }

  ngOnInit(): void {
    this.getAllMenus();
  }

  getAllMenus() {
    this._metaDataService.getAllMenus().subscribe(res => {
      this.permissionTabs = res;
      this.filteredData = this.permissionTabs;
    });
  }

  /**
   * @method addMenu
   * @param modelId 
   * @description open's the modal to add menu
   */
  addMenu(modelId) {
    let ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false,
      ariaLabelledBy: 'modal-basic-title',
      size: 'lg'
    };
    this.modalService.open(modelId, ngbModalOptions);
  }


  /**
   * @method filterDatatable
   * @param event 
   * @description fiter out's the data tabe on the basis of user search input
   */
  filterDatatable(event) {
    this.permissionTabs = this._filterService.filter(event, this.filteredData, this.permissionTabs);
    this.table.offset = 0;
  }

  /**
   * @method close
   * @description closes all the opened ngBModal
   */
  close() {
    this.modalService.dismissAll();
  }
}
