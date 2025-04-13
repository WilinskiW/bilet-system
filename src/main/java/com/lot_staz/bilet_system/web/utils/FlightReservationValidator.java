package com.lot_staz.bilet_system.web.utils;

import com.lot_staz.bilet_system.data.repository.DbCataloger;
import com.lot_staz.bilet_system.data.repository.FlightRepository;
import com.lot_staz.bilet_system.data.repository.FlightReservationRepository;
import com.lot_staz.bilet_system.data.repository.PassengerRepository;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.exception.SeatAlreadyTakenException;
import org.springframework.stereotype.Component;

@Component
public class FlightReservationValidator {
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;
    private final FlightReservationRepository reservationRepository;

    public FlightReservationValidator(DbCataloger dbCataloger) {
        this.flightRepository = dbCataloger.getFlightRepository();
        this.passengerRepository = dbCataloger.getPassengerRepository();
        this.reservationRepository = dbCataloger.getFlightReservationRepository();
    }

    public void checkIfValid(FlightReservationDto reservationDto) {
        checkExistence(reservationDto);

        // Check If seat number is taken in this flight
        if(reservationRepository.existsBySeatNumberAndFlightId(reservationDto.seatNumber(), reservationDto.flight().flightId())) {
            throw new SeatAlreadyTakenException("Seat number already in use");
        }
    }

    private void checkExistence(FlightReservationDto reservationDto) {
        // Check If flight exist in database
        if (!flightRepository.existsById(reservationDto.flight().flightId())) {
            throw new DataNotFoundException("Flight not found");
        }

        // Check If passenger exist in database
        if (!passengerRepository.existsById(reservationDto.passenger().passengerId())) {
            throw new DataNotFoundException("Passenger not found");
        }
    }

    public void checkIfValid(FlightReservationDto reservationDto, Long existingReservationId) {
        checkIfValid(reservationDto);

        // Check If seat number is taken in this flight by different reservation
        var existing = reservationRepository
                .findBySeatNumberAndFlightId(reservationDto.seatNumber(), reservationDto.flight().flightId());
        if (existing.isPresent() && !existing.get().getId().equals(existingReservationId)) {
            throw new SeatAlreadyTakenException("Seat number already in use");
        }
    }
}
