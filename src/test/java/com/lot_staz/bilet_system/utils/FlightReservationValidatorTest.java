package com.lot_staz.bilet_system.utils;

import com.lot_staz.bilet_system.data.model.FlightReservation;
import com.lot_staz.bilet_system.data.repository.FlightRepository;
import com.lot_staz.bilet_system.data.repository.FlightReservationRepository;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.exception.SeatAlreadyTakenException;
import com.lot_staz.bilet_system.web.utils.FlightReservationValidator;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightReservationValidatorTest {
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightReservationRepository reservationRepository;

    @InjectMocks
    private FlightReservationValidator validator;

    private FlightDto flightDto;
    private PassengerDto passengerDto;
    private FlightReservationDto reservationDto;

    @BeforeEach
    void setUp() {
        this.flightDto = new FlightDto(1L, "AA101", "WAW", 100, "NYC", LocalDateTime.now(), false);
        this.passengerDto = new PassengerDto(1L, "Joe", "Doe", "joe.doe@example.com", "123456789");
        this.reservationDto = new FlightReservationDto(1L, "RES123", flightDto, "A1", passengerDto, false);
    }

    @Test
    void shouldPassValidationForCreate() {
        when(flightRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.findByReservationNumber("RES123")).thenReturn(null);
        when(reservationRepository.findBySeatNumberAndFlightId("A1", 1L)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validator.validateForCreate(reservationDto));
    }

    @Test
    void shouldThrowIfFlightDoesNotExist() {
        when(flightRepository.existsById(1L)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> validator.validateForCreate(reservationDto));
    }

    @Test
    void shouldThrowIfReservationNumberExistsForAnotherId() {
        FlightReservation existing = new FlightReservation();
        existing.setId(99L);

        when(flightRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.findByReservationNumber("RES123")).thenReturn(existing);

        assertThrows(EntityExistsException.class, () -> validator.validateForCreate(reservationDto));
    }

    @Test
    void shouldThrowIfSeatIsAlreadyTakenForCreate() {
        when(flightRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.findByReservationNumber("RES123")).thenReturn(null);

        FlightReservation seatConflict = new FlightReservation();
        seatConflict.setId(2L);

        when(reservationRepository.findBySeatNumberAndFlightId("A1", 1L)).thenReturn(Optional.of(seatConflict));

        assertThrows(SeatAlreadyTakenException.class, () -> validator.validateForCreate(reservationDto));
    }

    @Test
    void shouldPassValidationForUpdateWhenSameSeatIsUsedBySameReservation() {
        when(flightRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.findByReservationNumber("RES123")).thenReturn(null);

        FlightReservation existing = new FlightReservation();
        existing.setId(1L);

        when(reservationRepository.findBySeatNumberAndFlightId("A1", 1L)).thenReturn(Optional.of(existing));

        assertDoesNotThrow(() -> validator.validateForUpdate(reservationDto, 1L));
    }

    @Test
    void shouldThrowForUpdateWhenSeatTakenByAnotherReservation() {
        when(flightRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.findByReservationNumber("RES123")).thenReturn(null);

        FlightReservation otherReservation = new FlightReservation();
        otherReservation.setId(99L);

        when(reservationRepository.findBySeatNumberAndFlightId("A1", 1L)).thenReturn(Optional.of(otherReservation));

        assertThrows(SeatAlreadyTakenException.class, () -> validator.validateForUpdate(reservationDto, 1L));
    }
}
