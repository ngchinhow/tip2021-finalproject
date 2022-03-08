package com.tfip2021.module4.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.tfip2021.module4.dto.SocialProvider;
import com.tfip2021.module4.models.DatabaseEvent;
import com.tfip2021.module4.utils.OAuth2Utils;

import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

public class CalendarServiceFactory {
    private static OAuth2Utils oauth2Utils = new OAuth2Utils();
    private static final String NO_SUPPORT_MESSAGE = 
        "Accessing the calendar service provided by " +
        StringUtils.capitalize(oauth2Utils.getProvider()) +
        " is currently not supported.";

    private CalendarServiceFactory() {
        // Private to prevent instantiation of factory class

    }

    public static List<DatabaseEvent> getRange(
        DateTime startDateTime,
        DateTime endDateTime
    ) throws IOException, GeneralSecurityException {
        CalendarService calendarService;
        
        if (oauth2Utils.getProvider().equals(SocialProvider.GOOGLE.getLoginProvider()))
            calendarService = new GoogleCalendarSerivce(
                oauth2Utils.getProvider(),
                oauth2Utils.getAuthenticatedUser(),
                oauth2Utils.getAccessToken()
            );
        // Add more providers
        else
            throw new IllegalArgumentException(NO_SUPPORT_MESSAGE);
        return calendarService.getRange(startDateTime, endDateTime);
    }
    
    public static String getCalendarTimezone() throws IOException, GeneralSecurityException {
        CalendarService calendarService;
        
        if (oauth2Utils.getProvider().equals(SocialProvider.GOOGLE.getLoginProvider()))
            calendarService = new GoogleCalendarSerivce(
                oauth2Utils.getProvider(),
                oauth2Utils.getAuthenticatedUser(),
                oauth2Utils.getAccessToken()
            );
        // Add more providers
        else
            throw new IllegalArgumentException(NO_SUPPORT_MESSAGE);
        return calendarService.getCalendarTimezone();
    }

    public static DatabaseEvent quickAddEvent(String eventAsString) throws IOException, GeneralSecurityException {
        CalendarService calendarService;
        
        if (oauth2Utils.getProvider().equals(SocialProvider.GOOGLE.getLoginProvider()))
            calendarService = new GoogleCalendarSerivce(
                oauth2Utils.getProvider(),
                oauth2Utils.getAuthenticatedUser(),
                oauth2Utils.getAccessToken()
            );
        // Add more providers
        else
            throw new IllegalArgumentException(NO_SUPPORT_MESSAGE);
        return calendarService.quickAddEvent(eventAsString);
    }
}
