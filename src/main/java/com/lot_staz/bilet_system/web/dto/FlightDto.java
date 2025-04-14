package com.lot_staz.bilet_system.web.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record FlightDto(
        Long id,

        @NotEmpty(message = "Departure place is required")
        String departurePlace,

        @NotEmpty(message = "Departure place is required")
        String arrivalPlace,

        @Min(value = 0, message = "Duration of flight must be higher than 0!")
        Integer duration,

        @NotEmpty(message = "Flight number is required")
        String flightNumber,

        @FutureOrPresent
        LocalDateTime departureTime,

        boolean roundTrip
) { }