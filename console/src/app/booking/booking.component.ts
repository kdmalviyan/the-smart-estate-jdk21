import { Component, OnInit, ViewChild } from '@angular/core';
import { NavigationExtras, Router } from '@angular/router';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { FilterService } from '../core/service/filter.service';
import { MessageService } from '../core/service/message.service';
import { BookingService } from './booking.service';

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.sass']
})
export class BookingComponent implements OnInit {

  data: any | null;
  @ViewChild(DatatableComponent, { static: false }) table: DatatableComponent;
  filteredData: any | null;


  constructor(
    private router: Router,
    private bookingService: BookingService,
    private messageService: MessageService,
    private filterService: FilterService
  ) { }

  ngOnInit(): void {
    this.getAllBookkings();
  }


  getAllBookkings() {
    this.bookingService.getAllBookings().subscribe(
      res => {
        console.log(res);
        this.data = res.contentList;
        this.filteredData = [...this.data];
      }
    )
  }

  filterDatatable(event) {
    this.data = this.filterService.filter(event, this.filteredData, this.data);
    // whenever the filter changes, always go back to the first page
    this.table.offset = 0;
  }

  addRow(action) {
    let navigationExtras: NavigationExtras = {
      queryParams: {
        action: action
      }
    };
    this.router.navigate(['/booking-management/add-booking'], navigationExtras);
  }

  refresh() {
    this.getAllBookkings();
  }

}
