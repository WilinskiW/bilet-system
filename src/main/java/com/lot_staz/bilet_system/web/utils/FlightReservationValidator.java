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

import java.util.Optional;

/**
 * A utility class responsible for validating flight reservation data.
 * Provides validation logic for creating and updating reservations.
 */
@Component
@RequiredArgsConstructor
public class FlightReservationValidator {

    private final FlightRepository flightRepository;
    private final FlightReservationRepository reservationRepository;

    /**
     * Validates whether the provided reservation data is valid for creating a new reservation.
     * <ol>
     *     <li>Checks if the flight exists.</li>
     *     <li>Checks if the reservation number is unique.</li>
     *     <li>Checks if the seat number is not already taken.</li>
     * </ol>
     *
     * @param dto The reservation data to validate.
     * @throws DataNotFoundException if the flight does not exist.
     * @throws EntityExistsException if the reservation number is already used.
     * @throws SeatAlreadyTakenException if the seat is already taken on the flight.
     */
    public void validateForCreate(FlightReservationDto dto) {
        validateCommon(dto);
        checkSeatAvailability(dto.seatNumber(), dto.flight().id(), null);
        validatePassengerForCreate(dto);
    }

    /**
     * Validates whether the provided reservation data is valid for updating an existing reservation.
     * <ul>
     *     <li>Checks if the flight exists.</li>
     *     <li>Checks if the reservation number is unique (excluding current one).</li>
     *     <li>Checks if the seat number is not already taken by another reservation.</li>
     * </ul>
     *
     * @param dto The reservation data to validate.
     * @param reservationId The ID of the reservation being updated.
     * @throws DataNotFoundException if the flight does not exist.
     * @throws EntityExistsException if the reservation number is already used by another reservation.
     * @throws SeatAlreadyTakenException if the seat is taken by a different reservation.
     */
    public void validateForUpdate(FlightReservationDto dto, Long reservationId) {
        validateCommon(dto);
        checkSeatAvailability(dto.seatNumber(), dto.flight().id(), reservationId);
    }

    /**
     * Performs common validation used in both create and update operations:
     * <ul>
     *     <li>Checks if the flight exists.</li>
     *     <li>Ensures the reservation number is not duplicated.</li>
     * </ul>
     *
     * @param dto The reservation data to validate.
     * @throws DataNotFoundException if the flight is not found.
     * @throws EntityExistsException if the reservation number is duplicated.
     */
    private void validateCommon(FlightReservationDto dto) {
        checkFlightExists(dto.flight().id());
        checkReservationNumberUnique(dto);
    }


    /**
     * Validates if the flight with the given ID exists in the database.
     *
     * @param flightId The ID of the flight.
     * @throws DataNotFoundException if the flight does not exist.
     */
    private void checkFlightExists(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new DataNotFoundException("Flight not found");
        }
    }

    /**
     * Ensures that the reservation number is unique in database.
     *
     * @param dto The reservation DTO with the reservation number.
     * @throws EntityExistsException if a reservation with the same number already exists and has a different ID.
     */
    private void checkReservationNumberUnique(FlightReservationDto dto) {
        FlightReservation existing = reservationRepository.findByReservationNumber(dto.reservationNumber());
        if (existing != null && !existing.getId().equals(dto.id())) {
            throw new EntityExistsException("Reservation number already exists");
        }
    }

    /**
     * Validates if the seat is available for the given flight.
     * Skips the check if the found reservation belongs to the same ID passed (for update cases).
     *
     * @param seatNumber The seat number to check.
     * @param flightId The flight ID.
     * @param excludeId ID to exclude from seat conflict check (can be null for creation).
     * @throws SeatAlreadyTakenException if the seat is already taken by a different reservation.
     */
    private void checkSeatAvailability(String seatNumber, Long flightId, Long excludeId) {
        Optional<FlightReservation> existing = reservationRepository.findBySeatNumberAndFlightId(seatNumber, flightId);
        if (existing.isPresent() && !existing.get().getId().equals(excludeId)) {
            throw new SeatAlreadyTakenException("Seat number already in use");
        }
    }

    /**
     * Ensures that the passenger ID is not set
     * @param dto The reservation DTO with the passenger.
     * @throws IllegalArgumentException if the passenger object has ID
     */
    private void validatePassengerForCreate(FlightReservationDto dto) {
        if (dto.passenger().id() != null) {
            throw new IllegalArgumentException("New passenger should not have an ID set.");
        }
    }
}