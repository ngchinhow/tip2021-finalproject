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
export class DayResolver implements Resolve<Event[]> {

  constructor(private calendarService: CalendarService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<Event[]> {
    let year = +route.params['year'];
    let month = +route.params['month'];
    let day = +route.params['day'];
    return this.calendarService.getDay(year, month, day);
  }
}
