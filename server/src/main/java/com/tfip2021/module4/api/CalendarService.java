package com.tfip2021.module4.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.tfip2021.module4.models.DatabaseEvent;
import com.tfip2021.module4.models.DatabaseUser;

import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class CalendarService {
    private String provider;
    private DatabaseUser authenticatedUser;
    private String accessToken;

    public abstract List<DatabaseEvent> getRange(
        DateTime startDateTime,
        DateTime endDateTime
    ) throws IOException, GeneralSecurityException;

    public abstract String getCalendarTimezone() 
        throws GeneralSecurityException, IOException;

    public abstract DatabaseEvent quickAddEvent(String eventAsString) 
        throws GeneralSecurityException, IOException;

    public abstract DatabaseEvent toDatabaseEvent(
        Object eventObject,
        String calendarTimezone
    );

}
