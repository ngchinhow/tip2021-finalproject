import { DateTime } from "luxon";

export interface Event {
  eventId: string;
  provider: string;
  providerEventId: string;
  agenda: string;
  location: string;
  startDate: DateTime;
  endDate: DateTime;
  eventAsString?: string;
}
