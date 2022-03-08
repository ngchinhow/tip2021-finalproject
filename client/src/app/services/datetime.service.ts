import { Injectable } from "@angular/core";
import { DateTime, Info } from "luxon";

@Injectable()
export class DateTimeService {
  static weekdays = Info.weekdays('short');

  getNumDaysInMonthAsArray(today: DateTime): number[][] {
    console.info('>>> today ', today.daysInMonth);
    return this.makeNumArray(today.daysInMonth + 1);
  }

  getStartOfMonthBuffer(today: DateTime): number[][] {
    let numToMake = today.startOf('month').weekday - 2;
    if (numToMake < 0)
      numToMake = 0;
    return this.makeNumArray(numToMake);
  }

  private makeNumArray(n: number): number[][] {
    return Array.from(Array(n), _ => []);
  }
}
