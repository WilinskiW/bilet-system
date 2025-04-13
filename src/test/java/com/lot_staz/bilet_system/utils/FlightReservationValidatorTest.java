package com.lot_staz.bilet_system.utils;

import com.lot_staz.bilet_system.data.model.FlightReservation;
import com.lot_staz.bilet_system.data.repository.FlightRepository;
import com.lot_staz.bilet_system.data.repository.FlightReservationRepository;
import com.lot_staz.bilet_system.data.repository.PassengerRepository;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.exception.SeatAlreadyTakenException;
import com.lot_staz.bilet_system.web.utils.FlightReservationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightReservationValidatorTest {
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private FlightReservationRepository reservationRepository;

    @InjectMocks
    private FlightReservationValidator validator;

    private FlightReservationDto validReservationDto;

    @BeforeEach
    void setUp() {
        FlightDto flightDto = new FlightDto(1L, "Berlin", "London", 100, "RX212",
                LocalDateTime.now(), false);
        PassengerDto passengerDto = new PassengerDto(1L, "Joe", "Doe",
                "joe.doe@example.com", "123456789");

        validReservationDto = new FlightReservationDto(1L, "RD12", flightDto, "10A",
                passengerDto, false);
    }

    @Test
    void checkIfValidShouldThrowExceptionWhenFlightNotFound() {
        when(flightRepository.existsById(validReservationDto.flight().flightId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> validator.checkIfValid(validReservationDto));
    }

    @Test
    void checkIfValidShouldThrowExceptionWhenPassengerNotFound() {
        when(flightRepository.existsById(validReservationDto.flight().flightId())).thenReturn(true);
        when(passengerRepository.existsById(validReservationDto.passenger().passengerId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> validator.checkIfValid(validReservationDto));
    }

    @Test
    void checkIfValidShouldThrowExceptionWhenSeatIsAlreadyTaken() {
        when(flightRepository.existsById(validReservationDto.flight().flightId())).thenReturn(true);
        when(passengerRepository.existsById(validReservationDto.passenger().passengerId())).thenReturn(true);
        when(reservationRepository.existsBySeatNumberAndFlightId(validReservationDto.seatNumber(), validReservationDto.flight().flightId()))
                .thenReturn(true);

        assertThrows(SeatAlreadyTakenException.class, () -> validator.checkIfValid(validReservationDto));
    }

    @Test
    void checkIfValidShouldPassWhenDataIsValid() {
        when(flightRepository.existsById(validReservationDto.flight().flightId())).thenReturn(true);
        when(passengerRepository.existsById(validReservationDto.passenger().passengerId())).thenReturn(true);
        when(reservationRepository.existsBySeatNumberAndFlightId(validReservationDto.seatNumber(), validReservationDto.flight().flightId()))
                .thenReturn(false);

        validator.checkIfValid(validReservationDto);

        verify(flightRepository).existsById(validReservationDto.flight().flightId());
        verify(passengerRepository).existsById(validReservationDto.passenger().passengerId());
        verify(reservationRepository).existsBySeatNumberAndFlightId(validReservationDto.seatNumber(), validReservationDto.flight().flightId());
    }

    @Test
    void checkIfValidShouldThrowExceptionWhenSeatIsTakenByAnotherReservation() {
        FlightReservationDto updatedReservationDto = new FlightReservationDto(
                1L, "RD100", validReservationDto.flight(), "TEST D",
                validReservationDto.passenger(), true
        );

        when(flightRepository.existsById(updatedReservationDto.flight().flightId())).thenReturn(true);
        when(passengerRepository.existsById(updatedReservationDto.passenger().passengerId())).thenReturn(true);

        FlightReservation existingReservation = new FlightReservation();
        existingReservation.setId(2L);

        when(reservationRepository.findBySeatNumberAndFlightId(updatedReservationDto.seatNumber(), updatedReservationDto.flight().flightId()))
                .thenReturn(Optional.of(existingReservation));

        assertThrows(SeatAlreadyTakenException.class, () -> validator.checkIfValid(updatedReservationDto, 1L));
    }
}
