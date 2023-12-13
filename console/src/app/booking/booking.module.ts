import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingComponent } from './booking.component';
import { SharedModule } from '../shared/shared.module';
import { MatIconModule } from '@angular/material/icon';
import { BookingManagementRoutingModule } from './booking-routing.module';
import { AddEditBookingComponent } from './add-edit-booking/add-edit-booking.component';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { NgxMaskModule } from 'ngx-mask';
import { BookingService } from './booking.service';
import { NgxMatFileInputModule } from '@angular-material-components/file-input';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';


@NgModule({
  declarations: [
    BookingComponent,
    AddEditBookingComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    MatIconModule,
    MatSelectModule,
    MatDatepickerModule,
    MatInputModule,
    BookingManagementRoutingModule,
    NgxMatFileInputModule,
    NgxDatatableModule,
    NgxMaskModule.forRoot(),
  ],
  providers: [BookingService]
})
export class BookingModule { }
