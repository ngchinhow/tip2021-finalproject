package com.tfip2021.module4.services.model;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.tfip2021.module4.api.CalendarServiceFactory;
import com.tfip2021.module4.models.DatabaseEvent;
import com.tfip2021.module4.repositories.DatabaseEventRepository;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseEventService {
    @Autowired
    private DatabaseEventRepository repo;
 
    public List<DatabaseEvent> getEventsInRange(
        DateTime startDateTime,
        DateTime endDateTime
    ) throws IOException, GeneralSecurityException {
        return CalendarServiceFactory.getRange(startDateTime, endDateTime)
            .stream()
            .map(this::upsert)
            .toList();
    }

    public DatabaseEvent quickAddEvent(String eventAsString)  throws IOException, GeneralSecurityException {
        return CalendarServiceFactory.quickAddEvent(eventAsString);
    }

    public String getCalendarTimezone() throws IOException, GeneralSecurityException {
        return CalendarServiceFactory.getCalendarTimezone();
    }

    public DatabaseEvent upsert(DatabaseEvent apiEvent) {
        DatabaseEvent dbEvent = repo.findByProviderAndProviderEventId(
            apiEvent.getProvider(), apiEvent.getProviderEventId()
        );
        if (dbEvent != null)
            dbEvent = apiEvent.toBuilder()
                .eventId(dbEvent.getEventId())
                .createdAt(dbEvent.getCreatedAt())
                .build();
        else
            dbEvent = apiEvent;
        dbEvent = repo.save(dbEvent);
        return dbEvent;
    }
}
