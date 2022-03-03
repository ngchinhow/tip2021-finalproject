import { Injectable } from "@angular/core";
import { DateTime, Info } from "luxon";

@Injectable()
export class DateTimeService {
  static weekdays = Info.weekdays('short');

  getNumDaysInMonthAsArray(today: DateTime): number[] {
    return this.makeNumArray(today.daysInMonth);
  }

  getStartOfMonthBuffer(today: DateTime): number[] {
    return this.makeNumArray(today.startOf('month').weekday - 1);
  }

  private makeNumArray(n: number): number[] {
    return Array.from(Array(n), (_, i) => i+1);
  }
}
