import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { DateTime } from "luxon";
import { lastValueFrom } from "rxjs";
import { AddEventRequest } from "../dto/addeventrequest";
import { EventResponse } from "../dto/eventresponse";
import { Constants } from "../models/constants";
import { Event } from "../models/event";

@Injectable()
export class CalendarService {
  constructor(private http: HttpClient) { }

  async getMonth(year: number, month: number): Promise<Event[]> {
    return lastValueFrom(
      this.http.get<EventResponse[]>(
        `${Constants.API_URL}/calendar/month/${year}/${month}`
      )
    )
    .then(response => {
      return response.map(event => this.parseResponse(event));
    });
  }

  async getDay(year: number, month: number, day: number): Promise<Event[]> {
    return lastValueFrom(
      this.http.get<EventResponse[]>(
        `${Constants.API_URL}/calendar/day/${year}/${month}/${day}`
      )
    )
    .then(response => {
      return response.map(event => this.parseResponse(event));
    });
  }

  async addEvent(eventRequest: AddEventRequest): Promise<Event> {
    return lastValueFrom(
      this.http.post<EventResponse>(
        `${Constants.API_URL}/calendar/quickAdd`,
        eventRequest
      )
    )
    .then(response => {
      return this.parseResponse(response);
    });
  }

  toString(event: Event): string {
    let returnStrArr = [event.agenda];
    if (event.location != null)
      returnStrArr.push(' at ', event.location.split(',')[0]);
    if (event.endDate.day > event.startDate.day)
      returnStrArr.push(
        ' from ',
        event.startDate.toLocaleString(DateTime.DATETIME_MED),
        ' to ',
        event.endDate.toLocaleString(DateTime.DATETIME_MED)
      );
    else if (event.endDate.day == event.startDate.day)
      returnStrArr.push(
        ' on ',
        event.startDate.toLocaleString(DateTime.DATE_MED),
        ' from ',
        event.startDate.toLocaleString(DateTime.TIME_SIMPLE),
        ' to ',
        event.endDate.toLocaleString(DateTime.TIME_SIMPLE)
      );

    return returnStrArr.join('');
  }

  parseResponse(event: EventResponse): Event {
    let e: Event = {
      eventId: event.eventId,
      provider: event.provider,
      providerEventId: event.providerEventId,
      agenda: event.agenda,
      location: event.location,
      startDate: DateTime.fromISO(event.startDate),
      endDate: DateTime.fromISO(event.endDate)
    };

    e.eventAsString = this.toString(e);
    return e;
  }
}
