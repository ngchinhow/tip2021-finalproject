package com.tfip2021.module4.controllers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.tfip2021.module4.dto.QuickAddRequest;
import com.tfip2021.module4.models.DatabaseEvent;
import com.tfip2021.module4.services.model.DatabaseEventService;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/calendar")
public class CalendarResources {
    @Autowired
    private DatabaseEventService eventService;

    @GetMapping(path = "/month/{year}/{month}")
    public ResponseEntity<List<DatabaseEvent>> getMonth(
        @PathVariable int year,
        @PathVariable int month
    ) throws IOException, GeneralSecurityException {
        final String calendarTimezone = eventService.getCalendarTimezone();
        DateTime startDateTime = new DateTime(
            year, month, 1, 0, 0, 0, 0, DateTimeZone.forID(calendarTimezone)
        );
        DateTime endDateTime = startDateTime.plusMonths(1)
            .withDayOfMonth(1)
            .minusDays(1)
            .plusSeconds(1);
        log.info(endDateTime.toString());
        return ResponseEntity.ok(
            eventService.getEventsInRange(startDateTime, endDateTime)
        );
    }

    @GetMapping(path = "/day/{year}/{month}/{day}")
    public ResponseEntity<List<DatabaseEvent>> getDay(
        @PathVariable int year,
        @PathVariable int month,
        @PathVariable int day
    ) throws IOException, GeneralSecurityException {
        final String calendarTimezone = eventService.getCalendarTimezone();
        DateTime startDateTime = new DateTime(
            year, month, day, 0, 0, 0, 0, DateTimeZone.forID(calendarTimezone)
        );
        DateTime endDateTime = startDateTime.plusDays(1);
        log.info(endDateTime.toString());
        return ResponseEntity.ok(
            eventService.getEventsInRange(startDateTime, endDateTime)
        );
    }

    @PostMapping(path = "/quickAdd")
    public ResponseEntity<DatabaseEvent> quickAddEvent(
        @RequestBody QuickAddRequest request
    )  throws IOException, GeneralSecurityException {
         return new ResponseEntity<>(
             eventService.quickAddEvent(request.getEventAsString()),
             HttpStatus.CREATED
         );
    }
}
