package com.lot_staz.bilet_system.web.dto;

import jakarta.validation.constraints.NotEmpty;

public record FlightReservationDto(
        Long reservationId,

        @NotEmpty(message = "Reservation number is required")
        String reservationNumber,

        @NotEmpty(message = "Flight can not be null")
        FlightDto flight,

        @NotEmpty(message = "Seat number is required")
        String seatNumber,

        @NotEmpty(message = "Passenger can not be null")
        PassengerDto passenger,

        boolean hasDeparted
) { }