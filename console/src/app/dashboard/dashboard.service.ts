import { Injectable } from '@angular/core';
import { forkJoin } from 'rxjs';
import { HttpService } from '../core/service/http.service';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private httpService: HttpService,) { }

  getTotalLead() {
    return this.httpService.get("dashboard/totalLeads");
  }

  getActiveLead() {
    return this.httpService.get("dashboard/activeLeads");
  }

  getFollowUps() {
    return this.httpService.get("dashboard/followUps");
  }

  getTargetVisitsAll() {
    return this.httpService.get("dashboard/targets/visits");
  }
  getTargetWeekly() {
    return this.httpService.get("dashboard/targets/visits/weekly");
  }
  getTargetMonthly() {
    return this.httpService.get("dashboard/targets/visits/monthly");
  }
  getBookingMonthly() {
    return this.httpService.get("dashboard/targets/bookings/monthly");
  }
  getBookingAll() {
    return this.httpService.get("dashboard/targets/bookings");
  }

  getBookingweekly() {
    return this.httpService.get("dashboard/targets/bookings/weekly");
  }

}
