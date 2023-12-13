import { Injectable } from '@angular/core';
import * as moment from 'moment';

@Injectable()
export class DateService {

  // Temporarily stores data from dialogs
  dialogData: any;
  viewLeadData: any;

  constructor() {
  }

  ranges: any = {
    'Today': [moment(), moment()],
    'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
    'Last 7 Days': [moment().subtract(6, 'days'), moment()],
    'Last 30 Days': [moment().subtract(29, 'days'), moment()],
    'This Month': [moment().startOf('month'), moment().endOf('month')],
    'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
    'Last 3 Month': [moment().subtract(3, 'month').startOf('month'),  moment()],
    'Last 6 Month': [moment().subtract(6, 'month').startOf('month'),  moment()]
  }

  invalidDates = [moment().subtract(10, 'days'), ];

}
