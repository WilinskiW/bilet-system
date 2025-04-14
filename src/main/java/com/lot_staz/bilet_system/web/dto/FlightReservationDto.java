package com.lot_staz.bilet_system.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record FlightReservationDto(
        Long id,

        @NotEmpty(message = "Reservation number is required")
        String reservationNumber,

        @NotNull(message = "Flight can not be null")
        @Valid
        FlightDto flight,

        @NotEmpty(message = "Seat number is required")
        @Pattern(regexp = "^[1-9][0-9]*[A-F]$")
        String seatNumber,

        @NotNull(message = "Passenger can not be null")
        @Valid
        PassengerDto passenger,

        boolean hasDeparted
) {
}