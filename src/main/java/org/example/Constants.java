package org.example;

public class Constants {

    public static final String LOGIN_URL = "/api/login";
    public static final String ME_URL = "/api/me";
    public static final String TEST = "/api/test";
    public static final String TEST_STRING = "test1234";
    public final static String[] ALLOWED_URLS = {LOGIN_URL, ME_URL};

    public static final String MEETUPS_IN_THE_GROUP = "/api/meetups/{groupId}/groups/";
    public static final String ALL_MEETUPS_IN_LOCATION = "/api/locations/{locationId}/meetups";
    public static final String ALL_MEETUP_ATTENDEES = "/api/meetup/{id}/attendees";
    public static final String ATTEND_MEETUP = "/attend/{id}";
    public static final String UNATTEND_MEETUP = "/unattend/{id}";
    public static final String JOIN_GROUP = "/join/{id}";
    public static final String LEAVE_GROUP = "/leave/{id}";
    public static final String JOINED_MEETUPS = "/api/meetups/joined";
    public static final String JOINED_GROUPS = "/api/groups/joined";
    public static final String ORGANIZED_MEETUPS = "/api/meetups/organized";
    public static final String OWNED_GROUPS = "/api/groups/owned";
    public static final String GROUP_MEMBERS = "/api/members/{groupId}";

    public static final String GET_LOCATION = "/api/location";
    public static final String GET_LOCATIONS = "/api/locations";
    public static final String GET_MEETUPS_IN_LOCATION = "/api/location/meetups";

    public static final String GET_MEETUP = "/api/meetup/{id}";
    public static final String GET_MEETUPS = "/api/meetups";
    public static final String CREATE_MEETUP = "/api/meetup";
    public static final String UPDATE_MEETUP = "/api/meetup/{id}";
    public static final String DELETE_MEETUP = "/api/meetup/{id}";

    public static final String GET_GROUP = "/api/group/{id}";
    public static final String GET_GROUPS = "/api/groups";
    public static final String CREATE_GROUP = "/api/group";
    public static final String UPDATE_GROUP = "/api/group/{id}";
    public static final String DELETE_GROUP = "/api/group/{id}";

    public static final String REGISTER = "/api/register";
    public static final String GET_USER = "/api/user/{id}";
    public static final String GET_USER_BY_USERNAME = "/api/userbyusername/{username}";
    public static final String UPDATE_USER = "/api/user/{id}";

}
