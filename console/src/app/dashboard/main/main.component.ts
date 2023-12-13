import { Component, OnInit, ViewChild } from '@angular/core';

import { NavigationExtras, Router } from '@angular/router';
import * as moment from 'moment';
import {
  ApexAxisChartSeries,
  ApexChart, ApexDataLabels, ApexGrid, ApexLegend, ApexMarkers, ApexPlotOptions, ApexStroke, ApexTitleSubtitle, ApexTooltip, ApexXAxis, ApexYAxis, ChartComponent
} from 'ng-apexcharts';
import { FilterConstants } from '../../shared/filter-constant';
import { DashboardService } from '../dashboard.service';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  stroke: ApexStroke;
  dataLabels: ApexDataLabels;
  markers: ApexMarkers;
  colors: string[];
  yaxis: ApexYAxis;
  grid: ApexGrid;
  legend: ApexLegend;
  title: ApexTitleSubtitle;
  tooltip: ApexTooltip;
  plotOptions: ApexPlotOptions;
};

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {
  @ViewChild('chart', { static: false }) chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  public chartOptions2: Partial<ChartOptions>;
  siteVisitChartFormat: string = "weekly";
  bookingChartFormat: string = "weekly";


  // barChart
  public barChartOptions: any = {

    scaleShowVerticalLines: true,
    responsive: true,
    legend: {
      display: true,
      labels: {
        fontColor: '#9aa0ac'
      }
    },
    scales: {
      xAxes: [
        {
          gridLines: {
            display: false
          },
          ticks: {
            fontFamily: 'Poppins',
            fontColor: '#9aa0ac' // Font Color
          }
        }
      ],
      yAxes: [
        {
          ticks: {
            beginAtZero: true,
            fontFamily: 'Poppins',
            fontColor: '#9aa0ac' // Font Color
          }
        }
      ]
    }
  };


  public barChartLabels = []
  public barChartType = 'bar';
  public barChartLegend = true;
  public barChartData = [];

  public barChartDataBooking = [];



  public barChartColors: Array<any> = [
    {
      backgroundColor: 'orange',
      borderColor: 'rgba(174, 174, 174, 1)',
      pointBackgroundColor: 'green',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(174, 174, 174, 0.8)'
    },
    {
      backgroundColor: '#1b1f52',
      borderColor: 'rgba(90, 155, 246, 1)',
      pointBackgroundColor: 'rgba(90, 155, 246, 1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(90, 155, 246, 0.8)'
    },
  ];

  totalLeads: any;
  activeLeads: any;
  followUps: any;
  weeklyTargetSitevisit = 0;
  weeklyTargetBooking = 0;

  constructor(private _dashService: DashboardService,
    private router: Router,
  ) {

  }


  createBarChartDataVisit(targets, type) {

    this.barChartLabels = []
    this.barChartData = []
    var visitTarget = []
    var visitTargetDone = []
    var currentDate = moment();
    var currentWeek = currentDate.week()

    targets.forEach(target => {
      this.barChartLabels.push(target.projectName)
      if ("weekly" == type) {
        target.targetCountMap.forEach(tr => {
          if (currentWeek == tr.weekNo) {
            visitTarget.push(tr.value)
          }
        });
        target.targetDoneCountMap.forEach(tr => {
          if (currentWeek == tr.weekNo) {
            visitTargetDone.push(tr.value)
          }
        });
      }
      else {
        target.targetCountMap.forEach(tr => {
          visitTarget.push(tr.value)
        });
        target.targetDoneCountMap.forEach(tr => {
          visitTargetDone.push(tr.value)
        });
      }
    });
    this.barChartData.push({ data: visitTarget, label: 'Site Visit' })
    this.barChartData.push({ data: visitTargetDone, label: 'Site Visit Done' })
  }

  createBarChartDataBooking(targets, type) {
    var currentDate = moment();
    var currentWeek = currentDate.week()
    this.barChartLabels = []
    this.barChartDataBooking = []
    var bookingTarget = []
    var bookingTargetDone = []
    targets.forEach(target => {
      this.barChartLabels.push(target.projectName)

      if ("weekly" == type) {
        target.targetCountMap.forEach(tr => {
          if (currentWeek == tr.weekNo) {
            bookingTarget.push(tr.value)
          }
        });
        target.targetDoneCountMap.forEach(tr => {
          if (currentWeek == tr.weekNo) {
            bookingTargetDone.push(tr.value)
          }
        });
      }
      else {
        target.targetCountMap.forEach(tr => {
          bookingTarget.push(tr.value)
        });
        target.targetDoneCountMap.forEach(tr => {
          bookingTargetDone.push(tr.value)
        });
      }


    });

    this.barChartDataBooking.push({ data: bookingTarget, label: 'Booking' })
    this.barChartDataBooking.push({ data: bookingTargetDone, label: 'Booking Done' })

  }


  // end bar chart

  ngOnInit() {
    'use strict';
    this.getTotalLead();
    this.getActiveLead();
    this.getFollowUps();
    this.siteVisitTargetGraph(this.siteVisitChartFormat);
    this.bookingTargetGraph(this.siteVisitChartFormat);
  }

  siteVisitTargetGraph(value) {
    if ("all" == value) {
      this.getSiteTargetsAll();
    } else if ("weekly" == value) {
      this.getSiteTargetsWeekly();
    } else if ("monthly" == value) {
      this.getSiteTargetsMonthly();
    }
  }
  bookingTargetGraph(value) {
    if ("all" == value) {
      this.getBookingsAll();
    } else if ("weekly" == value) {
      this.getBookingsWeekly();
    } else if ("monthly" == value) {
      this.getBookingsMonthly();
    }
  }



  getTotalLead() {
    this._dashService.getTotalLead().subscribe(
      res => {
        this.totalLeads = res;
      }
    )
  }

  getActiveLead() {
    this._dashService.getActiveLead().subscribe(
      res => {
        this.activeLeads = res;
      }
    )
  }

  getFollowUps() {
    this._dashService.getFollowUps().subscribe(
      res => {
        this.followUps = res;
      }
    )
  }

  getSiteTargetsAll() {
    this._dashService.getTargetVisitsAll().subscribe(
      res => {
        var targets = res;
        this.createBarChartDataVisit(targets, "all");
      }
    )
  }
  getSiteTargetsMonthly() {
    this._dashService.getTargetMonthly().subscribe(
      res => {
        var targetsMonthly = res;
        this.createBarChartDataVisit(targetsMonthly, "monthly");
      }
    )
  }
  getSiteTargetsWeekly() {
    var currentDate = moment();
    var currentWeek = currentDate.week()

    this._dashService.getTargetWeekly().subscribe(
      res => {
        var targetsWeekly = res;

        this.createBarChartDataVisit(targetsWeekly, "weekly");
        this.weeklyTargetSitevisit = 0;

        targetsWeekly.forEach(target => {
          target.targetCountMap.forEach(tr => {
            if (currentWeek == tr.weekNo) {
              this.weeklyTargetSitevisit = this.weeklyTargetSitevisit + tr.value
            }
          });
        });

        console.log(this.weeklyTargetSitevisit)
      }
    )

  }

  getBookingsMonthly() {
    this._dashService.getBookingMonthly().subscribe(
      res => {
        var bookingMonth = res;
        this.createBarChartDataBooking(bookingMonth, "monthly");
      }
    )
  }
  getBookingsAll() {
    this._dashService.getBookingAll().subscribe(
      res => {
        var bookingMonth = res;
        this.createBarChartDataBooking(bookingMonth, "all");
      }
    )
  }
  getBookingsWeekly() {
    var currentDate = moment();
    var currentWeek = currentDate.week()

    this._dashService.getBookingweekly().subscribe(
      res => {
        var bookingMonth = res;
        this.createBarChartDataBooking(bookingMonth, "weekly");
        this.weeklyTargetBooking = 0;
        bookingMonth.forEach(target => {
          target.targetCountMap.forEach(tr => {
            if (currentWeek == tr.weekNo) {
              this.weeklyTargetBooking = this.weeklyTargetBooking + tr.value
            }
          });
        });
      }
    )
  }

  setFormate(event) {
    const val = event.target.value;
    this.siteVisitChartFormat = event.target.value;
    switch (val) {
      case 'all':
        this.siteVisitTargetGraph("all");
        break;
      case 'weekly':
        this.siteVisitTargetGraph("weekly");
        break;
      case 'monthly':
        this.siteVisitTargetGraph("monthly");
        break;
      default:
        this.siteVisitTargetGraph("all");
    }
  }

  setBookingFormate(event) {
    const val = event.target.value;
    this.bookingChartFormat = event.target.value;
    switch (val) {
      case 'all':
        this.bookingTargetGraph("all");
        break;
      case 'weekly':
        this.bookingTargetGraph("weekly");
        break;
      case 'monthly':
        this.bookingTargetGraph("monthly");
        break;
      default:
        this.bookingTargetGraph("all");
    }
  }

  goToFollowup() {
    // let navigationExtras: NavigationExtras = {
    //   queryParams: {
    //     "origin": "dashboard",
    //     "backButton": "follow-up"
    //   }
    // };
    this.router.navigate(['/follow-up/']);

  }

  goToTotalLead() {
    let navigationExtras: NavigationExtras = {
      queryParams: {
        "origin": "dashboard",
        "pageParms": JSON.stringify(this.getPageParamsForDashboard('ACTIVE,IN-PROCESS,FOLLOW,FOLLOW-UP,FOLLOW-UP-EXPIRE,BOOKED,DEACTIVE,FOLLOW-UP-COMPLETE')),
        "backButton": "follow-up"
      }
    };
    this.router.navigate(['/lead-management/'], navigationExtras);
  }

  goToActiveLead() {
    let navigationExtras: NavigationExtras = {
      queryParams: {
        "origin": "dashboard",
        "pageParms": JSON.stringify(this.getPageParamsForDashboard('ACTIVE,IN-PROCESS,FOLLOW,FOLLOW-UP,FOLLOW-UP-EXPIRE')),
        "backButton": "follow-up"
      }
    };
    this.router.navigate(['/lead-management/'], navigationExtras);
  }

  getPageParams() {

    let pageParams = {
      'orderColumn': 'createdAt',
      'orderDir': FilterConstants.page.orderDir,
      'pageNumber': FilterConstants.page.offset,
      'pageSize': FilterConstants.page.limit,
      'assignedTo': 'NO',
      'status': 'ACTIVE,IN-PROCESS,FOLLOW,FOLLOW-UP,FOLLOW-UP-EXPIRE',
      'project': '',
      'leadInventorySize': '',
      'deactivationReason': '',
      'type': 'NO',
      'budget': FilterConstants.budget,
      'searchText': '',
      startDate: moment(new Date("01/01/2022")).format('DD/MM/yyyy'),
      endDate: moment(new Date()).format('DD/MM/yyyy')
    }

    return pageParams;
  }

  getPageParamsForDashboard(status) {

    let pageParams = {
      'orderColumn': 'createdAt',
      'orderDir': FilterConstants.page.orderDir,
      'pageNumber': FilterConstants.page.offset,
      'pageSize': FilterConstants.page.limit,
      'assignedTo': 'NO',
      'status': status,
      'project': '',
      'leadInventorySize': '',
      'deactivationReason': '',
      'type': 'NO',
      'budget': FilterConstants.budget,
      'searchText': '',
      startDate: moment(new Date("01/01/2022")).format('DD/MM/yyyy'),
      endDate: moment(new Date()).format('DD/MM/yyyy')
    }

    return pageParams;
  }


}
