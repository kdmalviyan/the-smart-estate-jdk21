import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddEditBookingComponent } from './add-edit-booking/add-edit-booking.component';
import { BookingComponent } from './booking.component';

const routes: Routes = [
    {
        path: '',
        component: BookingComponent
    },
    {
        path: 'add-booking',
        component: AddEditBookingComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class BookingManagementRoutingModule { }