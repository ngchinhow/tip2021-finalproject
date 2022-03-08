import { Injectable } from '@angular/core';
import {
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import { Event } from '../models/event';
import { CalendarService } from '../services/calendar.service';

@Injectable({
  providedIn: 'root'
})
export class MonthResolver implements Resolve<Event[]> {

  constructor(private calendarService: CalendarService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    let year = +route.params['year'];
    let month = +route.params['month'];
    return this.calendarService.getMonth(year, month);
  }
}
