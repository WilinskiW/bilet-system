package com.lot_staz.bilet_system.web.utils;

import com.lot_staz.bilet_system.data.repository.FlightRepository;
import com.lot_staz.bilet_system.data.repository.FlightReservationRepository;
import com.lot_staz.bilet_system.data.repository.PassengerRepository;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.exception.SeatAlreadyTakenException;
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
    private final PassengerRepository passengerRepository;
    private final FlightReservationRepository reservationRepository;

    /**
     * Checks if the reservation data is valid for a new reservation.
     * This method validates if the flight exists, the passenger exists, and if the seat number is available.
     *
     * @param reservationDto The flight reservation data to be validated.
     * @throws DataNotFoundException If the flight or passenger cannot be found.
     * @throws SeatAlreadyTakenException If the requested seat is already taken.
     */
    public void checkIfValid(FlightReservationDto reservationDto) {
        checkExistence(reservationDto);

        // Check if the requested seat is already taken for this flight
        if (reservationRepository.existsBySeatNumberAndFlightId(reservationDto.seatNumber(), reservationDto.flight().id())) {
            throw new SeatAlreadyTakenException("Seat number already in use");
        }
    }

    /**
     * Checks if the flight and passenger referenced in the reservation data exist in the database.
     *
     * @param reservationDto The flight reservation data containing the flight and passenger details.
     * @throws DataNotFoundException If no matching flight or passenger is found in the database.
     */
    private void checkExistence(FlightReservationDto reservationDto) {
        // Check if the flight exists in the database
        if (!flightRepository.existsById(reservationDto.flight().id())) {
            throw new DataNotFoundException("Flight not found");
        }

        // Check if the passenger exists in the database
        if (!passengerRepository.existsById(reservationDto.passenger().id())) {
            throw new DataNotFoundException("Passenger not found");
        }
    }

    /**
     * Checks if the reservation data is valid for updating an existing reservation.
     * This method performs the same checks as {@link #checkIfValid(FlightReservationDto)}, but also
     * ensures that the seat is not taken by another reservation.
     *
     * @param reservationDto The updated flight reservation data.
     * @param existingReservationId The ID of the existing reservation being updated.
     * @throws DataNotFoundException If the flight or passenger cannot be found.
     * @throws SeatAlreadyTakenException If the requested seat is taken by another reservation.
     */
    public void checkIfValid(FlightReservationDto reservationDto, Long existingReservationId) {
        checkIfValid(reservationDto);

        // Find if there is an existing reservation for the same seat and flight
        var existing = reservationRepository
                .findBySeatNumberAndFlightId(reservationDto.seatNumber(), reservationDto.flight().id());

        // If a reservation exists, and it is not the current one, throw an exception
        if (existing.isPresent() && !existing.get().getId().equals(existingReservationId)) {
            throw new SeatAlreadyTakenException("Seat number already in use");
        }
    }
}
