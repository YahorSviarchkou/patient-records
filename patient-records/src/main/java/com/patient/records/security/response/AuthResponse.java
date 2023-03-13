package com.patient.records.security.response;

public record AuthResponse(String accessToken) {
    private static String tokenType = "Bearer ";
}
