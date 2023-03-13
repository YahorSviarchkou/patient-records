package com.patient.records.controller.handler;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime date, String message, String details) {
}
