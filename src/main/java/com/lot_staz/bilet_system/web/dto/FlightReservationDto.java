package com.lot_staz.bilet_system.web.dto;

public record FlightReservationDto(
        Long reservationId,
        String reservationNumber,
        FlightDto flight,
        String seatNumber,
        PassengerDto passenger,
        boolean hasDeparted
) { }