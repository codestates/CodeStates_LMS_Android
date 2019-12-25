package com.example.codestates;

import okhttp3.MediaType;

public class Constants {
    public static final String IP = "http://110.76.125.70:3000/";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String SIGNUP_URL = IP + "signup";
    public static final String SIGNIN_URL = IP + "signin";
    public static final String GET_COURSES = IP + "get_courses";
    public static final String GET_INVITECODE = IP + "invite_user";
    public static final String ACCEPT_INVITE = IP + "accept_invite";

    public static final String GET_USERS = IP + "get_users";
    public static final String CREATE_COURSE = IP + "add_course";
    public static final String GET_CONTAINERS_AND_CONTENTS = IP + "get_containers_and_contents";
}
