package com.tfip2021.module4.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTimeUtils {

    private DateTimeUtils() {
        // Private constructor to prevent instantiation
    }

    // Used by the Google Calendar API
    public static DateTime toGoogleDateTime(org.joda.time.DateTime jodaDate) {
        return new DateTime(jodaDate.toDate());
    }
    // Used by DatabaseEvent
    public static Date fromGoogleDateTime(EventDateTime googleDateTime, String calendarTimezone) {
        if (googleDateTime.getDateTime() != null)
            return new Date(googleDateTime.getDateTime().getValue());
        else {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            isoFormat.setTimeZone(TimeZone.getTimeZone(calendarTimezone));
            try {
                return isoFormat.parse(googleDateTime.getDate().toString() + "T00:00:00"); 
            } catch (ParseException e) {
                log.error(e.getMessage());
            }
            return null;
        }
    }
    
}
