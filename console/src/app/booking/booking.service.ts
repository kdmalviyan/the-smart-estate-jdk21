import { Injectable } from '@angular/core';
import { HttpService } from '../core/service/http.service';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  constructor(
    private httpService: HttpService
  ) { }

  findCustomer(number) {
    return this.httpService.get(`customer/phone/${number}`);
  }

  findProject(id) {
    return this.httpService.get(`project/${id}`);
  }


  createBooking(bookingData) {
    return this.httpService.post('booking', bookingData);
  }

  getAllBookings() {
    return this.httpService.get("booking");
  }
}
