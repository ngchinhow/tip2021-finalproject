package com.tfip2021.module4.api;

import static com.tfip2021.module4.models.Constants.APPLICATION_NAME;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.tfip2021.module4.models.DatabaseEvent;
import com.tfip2021.module4.models.DatabaseUser;
import com.tfip2021.module4.utils.DateTimeUtils;

import org.joda.time.DateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleCalendarSerivce extends CalendarService {
    private Credential credential;
    private static final String CALENDAR_OWNER = "primary";

    public GoogleCalendarSerivce(
        String provider,
        DatabaseUser authenticatedUser,
        String accessToken
    ) {
        super(provider, authenticatedUser, accessToken);
        this.credential = new Credential(
            BearerToken.authorizationHeaderAccessMethod()
        )
        .setAccessToken(accessToken);
    }

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Override
    public List<DatabaseEvent> getRange(
        DateTime startDateTime,
        DateTime endDateTime
    ) throws IOException, GeneralSecurityException {
        Calendar service = this.makeCalendarService();
        Events events = service.events().list(CALENDAR_OWNER)
            .setTimeMin(DateTimeUtils.toGoogleDateTime(startDateTime))
            .setTimeMax(DateTimeUtils.toGoogleDateTime(endDateTime))
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute();
        String ownerTimeZone = events.getTimeZone();
        return events.getItems()
            .stream()
            .map(e -> toDatabaseEvent(e, ownerTimeZone))
            .toList();
    }

    @Override
    public String getCalendarTimezone() throws GeneralSecurityException, IOException {
        return makeCalendarService().calendars()
            .get(CALENDAR_OWNER)
            .execute()
            .getTimeZone();
    }

    public DatabaseEvent quickAddEvent(String eventAsString) throws IOException, GeneralSecurityException {
        Event googleEvent = makeCalendarService().events()
            .quickAdd(CALENDAR_OWNER,eventAsString)
            .execute();
        String calendarTimezone = this.getCalendarTimezone();
        return toDatabaseEvent(googleEvent, calendarTimezone);
    }

    @Override
    public DatabaseEvent toDatabaseEvent(Object eventObject, String calendarTimezone) {
        Event googleEvent = (Event) eventObject;
        return DatabaseEvent.builder()
            .owner(this.getAuthenticatedUser())
            .provider(this.getProvider())
            .providerEventId(googleEvent.getId())
            .agenda(googleEvent.getSummary())
            .location(googleEvent.getLocation())
            .startDate(DateTimeUtils.fromGoogleDateTime(
                googleEvent.getStart(), calendarTimezone
            ))
            .endDate(DateTimeUtils.fromGoogleDateTime(
                googleEvent.getEnd(), calendarTimezone
            ))
            .build();
    }

    private Calendar makeCalendarService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(
            httpTransport,
            JSON_FACTORY,
            this.getCredential()
        )
        .setApplicationName(APPLICATION_NAME)
        .build();
    }
}
