package com.lot_staz.bilet_system.web.utils;

import com.lot_staz.bilet_system.data.model.FlightReservation;
import com.lot_staz.bilet_system.data.repository.FlightRepository;
import com.lot_staz.bilet_system.data.repository.FlightReservationRepository;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.exception.SeatAlreadyTakenException;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * A utility class responsible for validating flight reservation data.
 * It checks if a flight, passenger, and seat are available and not taken.
 */
@Component
@RequiredArgsConstructor
public class FlightReservationValidator {

    private final FlightRepository flightRepository;
    private final FlightReservationRepository reservationRepository;

    /**
     * Checks if the reservation data is valid for a new reservation.
     * This method validates if the flight exists, the passenger exists, and if the seat number is available.
     *
     * @param reservationDto The flight reservation data to be validated.
     * @throws DataNotFoundException If the flight or passenger cannot be found.
     * @throws SeatAlreadyTakenException If the requested seat is already taken.
     */
    public void checkIfValidForCreate(FlightReservationDto reservationDto) {
        check(reservationDto);

        // Check if the requested seat is already taken for this flight
        if (reservationRepository.existsBySeatNumberAndFlightId(reservationDto.seatNumber(), reservationDto.flight().id())) {
            throw new SeatAlreadyTakenException("Seat number already in use");
        }
    }

    /**
     * Checks if the reservation data is valid for updating an existing reservation.
     * This method performs the same checks as {@link #checkIfValidForCreate(FlightReservationDto)}, but also
     * ensures that the seat is not taken by another reservation.
     *
     * @param reservationDto The updated flight reservation data.
     * @param existingReservationId The ID of the existing reservation being updated.
     * @throws DataNotFoundException If the flight or passenger cannot be found.
     * @throws SeatAlreadyTakenException If the requested seat is taken by another reservation.
     */
    public void checkIfValidForUpdate(FlightReservationDto reservationDto, Long existingReservationId) {
        check(reservationDto);

        // Find if there is an existing reservation for the same seat and flight
        var existing = reservationRepository
                .findBySeatNumberAndFlightId(reservationDto.seatNumber(), reservationDto.flight().id());

        // If a reservation exists, and it is not the current one, throw an exception
        if (existing.isPresent() && !existing.get().getId().equals(existingReservationId)) {
            throw new SeatAlreadyTakenException("Seat number already in use");
        }
    }

    private void check(FlightReservationDto reservationDto){
        // Check if the flight exists in the database
        if (!flightRepository.existsById(reservationDto.flight().id())) {
            throw new DataNotFoundException("Flight not found");
        }

        FlightReservation reservation = reservationRepository.findByReservationNumber(reservationDto.reservationNumber());
        if(reservation != null && !reservation.getId().equals(reservationDto.id())) {
            throw new EntityExistsException("Reservation number already exists");
        }
    }
}
