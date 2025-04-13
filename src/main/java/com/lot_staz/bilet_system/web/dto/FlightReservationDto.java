package com.lot_staz.bilet_system.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record FlightReservationDto(
        Long reservationId,

        @NotEmpty(message = "Reservation number is required")
        String reservationNumber,

        @NotNull(message = "Flight can not be null")
        @Valid
        FlightDto flight,

        @NotEmpty(message = "Seat number is required")
        String seatNumber,

        @NotNull(message = "Passenger can not be null")
        @Valid
        PassengerDto passenger,

        boolean hasDeparted
) {
}