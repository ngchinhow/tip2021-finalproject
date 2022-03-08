import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DateTime } from 'luxon';
import { Event } from 'src/app/models/event';

@Component({
  selector: 'app-day',
  templateUrl: './day.component.html',
  styleUrls: ['./day.component.css']
})
export class DayComponent implements OnInit {
  day!: DateTime;
  events!: Event[];

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.events = this.activatedRoute.snapshot.data['events'];
    let year = +this.activatedRoute.snapshot.params['year'];
    let month = +this.activatedRoute.snapshot.params['month'];
    let day = +this.activatedRoute.snapshot.params['day'];
    this.day = DateTime.local(year, month, day);
  }

  displayDay() {
    return this.day.toLocaleString(DateTime.DATE_MED_WITH_WEEKDAY);
  }

  async backToMonth() {
    await this.router.navigate(['/month', this.day.year, this.day.month]);
    window.location.reload();
  }
}
