package com.rampeo.tiw.progetto2022.Constants.Attributes;

public enum ErrorParameter {
    UNKNOWN,
    UNREACHABLE,
    AUTH_EMPTY_FIELDS,
    AUTH_INVALID_EMAIL,
    AUTH_INVALID_UNAME,
    AUTH_INVALID_PASSWORD,
    AUTH_USER_EXISTS,
    AUTH_FAILED_AUTHENTICATION,
    AUTH_NOT_LOGGED_IN,
    AUTH_ALREADY_LOGGED_IN,
    CREATION_INVALID_PARAMETERS,
    CREATION_NO_MEETING_PENDING,
    CREATION_MAX_PARTICIPANTS,
    CREATION_MAX_ATTEMPTS,
    CREATION_ADMIN_INVITED,
    CREATION_NO_PARTICIPANTS,
    CREATION_NO_USERS_TO_INVITE,
}
