package com.lot_staz.bilet_system.web.dto;

public record FlightDto(
        Long flightId,
        String departurePlace,
        String arrivalPlace,
        Integer duration,
        String flightNumber,
        boolean roundTrip
) {
}
