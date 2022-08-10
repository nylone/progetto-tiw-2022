package com.rampeo.tiw.progetto2022.Constants;

public class URLs {
    public static final String AUTHENTICATED_PATH = "/a";
    public static final String AUTHENTICATED_PATTERN = AUTHENTICATED_PATH + "/*";
    public static final String LOGOUT = AUTHENTICATED_PATH + "/logout";
    public static final String CREATE_MEETING = AUTHENTICATED_PATH + "/create";
    public static final String COMPLETE_MEETING = AUTHENTICATED_PATH + "/complete";
    public static final String HOME_PAGE = AUTHENTICATED_PATH + "/home";
    public static final String LINK_TO_MEETING_PAGE = AUTHENTICATED_PATH + "/meeting";
    // CONTROLLER URLS
    public static final String LOGIN = "/login";
    public static final String SIGNUP = "/signup";
    // VIEW URLS
    public static final String AUTH_PAGE = "";
    public static final String ERROR_PAGE = "/error";
}
