import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DateTime } from 'luxon';
import { Event } from 'src/app/models/event';
import { CalendarService } from 'src/app/services/calendar.service';
import { DateTimeService } from 'src/app/services/datetime.service';

@Component({
  selector: 'app-month',
  templateUrl: './month.component.html',
  styleUrls: ['./month.component.css']
})
export class MonthComponent implements OnInit {
  currentMonth!: DateTime;
  weekdays = DateTimeService.weekdays;
  startOfMonthBuffer!: number[][];
  month!: number[][];
  events!: Event[];

  constructor(
    private dtSvc: DateTimeService,
    private calendarSvc: CalendarService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit(): void {
    if (Object.keys(this.activatedRoute.snapshot.params).length === 0) {
      let today = DateTime.local();
      this.router.navigate(['/month', today.year, today.month]);
    }
    let year = +this.activatedRoute.snapshot.params['year'];
    let month = +this.activatedRoute.snapshot.params['month'];
    this.currentMonth = DateTime.local(year, month);
    this.startOfMonthBuffer = this.dtSvc.getStartOfMonthBuffer(this.currentMonth);
    this.month = this.dtSvc.getNumDaysInMonthAsArray(this.currentMonth);

    this.events = this.activatedRoute.snapshot.data['events'];
    for (let index in this.events) {
      let event = this.events[index];
      this.month[event.startDate.day].push(+index);
    }
  }

  async prevMonth() {
    let prevMonth = this.currentMonth.minus({ month: 1 });
    console.info(prevMonth);
    await this.router.navigate(['/month', prevMonth.year, prevMonth.month ]);
    window.location.reload();
  }

  async nextMonth() {
    let nextMonth = this.currentMonth.plus({ month: 1 });
    await this.router.navigate(['/month', nextMonth.year, nextMonth.month]);
    window.location.reload();
  }
}
