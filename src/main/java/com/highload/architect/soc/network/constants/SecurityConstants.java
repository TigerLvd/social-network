package com.highload.architect.soc.network.constants;

public final class SecurityConstants {
    
    private SecurityConstants() {
        // Utility class
    }
    
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final int TOKEN_EXPIRATION_DAYS = 1;
    
    // API Endpoints
    public static final String SIGNIN_ENTRY_POINT = "/login";
    public static final String SIGNUP_ENTRY_POINT = "/user/register";
    public static final String SWAGGER_ENTRY_POINT = "/swagger-ui/**";
    public static final String API_DOCS_ENTRY_POINT = "/v3/api-docs/**";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/auth/refreshToken";
}
