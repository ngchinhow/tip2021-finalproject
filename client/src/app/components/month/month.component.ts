import { Component, OnInit } from '@angular/core';
import { DateTime } from 'luxon';
import { DateTimeService } from 'src/app/services/datetime.service';

@Component({
  selector: 'app-month',
  templateUrl: './month.component.html',
  styleUrls: ['./month.component.css']
})
export class MonthComponent implements OnInit {
  weekdays = DateTimeService.weekdays;
  startOfMonthBuffer!: number[];
  month!: number[];

  constructor(private dtSvc: DateTimeService) { }

  ngOnInit(): void {
    const today = DateTime.local();
    this.startOfMonthBuffer = this.dtSvc.getStartOfMonthBuffer(today);
    this.month = this.dtSvc.getNumDaysInMonthAsArray(today);
  }
}
