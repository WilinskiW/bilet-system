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
    void checkIfValidForUpdateShouldThrowExceptionWhenFlightNotFound() {
        when(flightRepository.existsById(validReservationDto.flight().id())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> validator.checkIfValidForCreate(validReservationDto));
    }

    @Test
    void checkIfValidForUpdateShouldThrowExceptionWhenPassengerNotFound() {
        when(flightRepository.existsById(validReservationDto.flight().id())).thenReturn(true);
        when(passengerRepository.existsById(validReservationDto.passenger().id())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> validator.checkIfValidForCreate(validReservationDto));
    }

    @Test
    void checkIfValidForUpdateShouldThrowExceptionWhenSeatIsAlreadyTaken() {
        when(flightRepository.existsById(validReservationDto.flight().id())).thenReturn(true);
        when(passengerRepository.existsById(validReservationDto.passenger().id())).thenReturn(true);
        when(reservationRepository.existsBySeatNumberAndFlightId(validReservationDto.seatNumber(), validReservationDto.flight().id()))
                .thenReturn(true);

        assertThrows(SeatAlreadyTakenException.class, () -> validator.checkIfValidForCreate(validReservationDto));
    }

    @Test
    void checkIfValidShouldPassWhenDataIsValidForCreate() {
        when(flightRepository.existsById(validReservationDto.flight().id())).thenReturn(true);
        when(passengerRepository.existsById(validReservationDto.passenger().id())).thenReturn(true);
        when(reservationRepository.existsBySeatNumberAndFlightId(validReservationDto.seatNumber(), validReservationDto.flight().id()))
                .thenReturn(false);

        validator.checkIfValidForCreate(validReservationDto);

        verify(flightRepository).existsById(validReservationDto.flight().id());
        verify(passengerRepository).existsById(validReservationDto.passenger().id());
        verify(reservationRepository).existsBySeatNumberAndFlightId(validReservationDto.seatNumber(), validReservationDto.flight().id());
    }

    @Test
    void checkIfValidForUpdateShouldThrowExceptionWhenSeatIsTakenByAnotherReservation() {
        FlightReservationDto updatedReservationDto = new FlightReservationDto(
                1L, "RD100", validReservationDto.flight(), "TEST D",
                validReservationDto.passenger(), true
        );

        when(flightRepository.existsById(updatedReservationDto.flight().id())).thenReturn(true);
        when(passengerRepository.existsById(updatedReservationDto.passenger().id())).thenReturn(true);

        FlightReservation existingReservation = new FlightReservation();
        existingReservation.setId(2L);

        when(reservationRepository.findBySeatNumberAndFlightId(updatedReservationDto.seatNumber(), updatedReservationDto.flight().id()))
                .thenReturn(Optional.of(existingReservation));

        assertThrows(SeatAlreadyTakenException.class, () -> validator.checkIfValidForUpdate(updatedReservationDto, 1L));
    }
}
