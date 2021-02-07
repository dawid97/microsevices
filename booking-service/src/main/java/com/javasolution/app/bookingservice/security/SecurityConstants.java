package com.javasolution.app.bookingservice.security;

public class SecurityConstants {

    public static final String HEADER_AUTHORIZATION="Authorization";
    public static final String TOKEN_PREFIX="Bearer ";
    public static final long EXPIRATION_TIME=24*60*60;
    public static final String SECRET_KEY = "JwtSecretKey";
    public static final String H2_CONSOLE="/h2-console/**";
    public static final String ADMIN_EMAIL="dan.kow94@gmail.com";
}
