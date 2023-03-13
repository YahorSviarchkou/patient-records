package com.patient.records.security.request;

public record RegisterRequest(String login, String password, String roleTitle) {
}
