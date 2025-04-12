package com.lot_staz.bilet_system.web.dto;

import java.time.LocalDateTime;

public record FlightDto(
        Long flightId,
        String departurePlace,
        String arrivalPlace,
        Integer duration,
        String flightNumber,
        LocalDateTime departureTime,
        boolean roundTrip
) {
}
