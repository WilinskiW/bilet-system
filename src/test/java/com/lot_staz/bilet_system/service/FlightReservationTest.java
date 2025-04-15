package com.lot_staz.bilet_system.service;

import com.lot_staz.bilet_system.data.model.FlightReservation;
import com.lot_staz.bilet_system.data.repository.FlightReservationRepository;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.exception.SeatAlreadyTakenException;
import com.lot_staz.bilet_system.web.mapper.FlightReservationMapper;
import com.lot_staz.bilet_system.web.service.EmailService;
import com.lot_staz.bilet_system.web.service.FlightReservationService;
import com.lot_staz.bilet_system.web.utils.FlightReservationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightReservationTest {
    @Mock
    private FlightReservationRepository reservationRepository;

    @Mock
    private FlightReservationValidator validator;

    @Mock
    private FlightReservationMapper mapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private FlightReservationService service;

    private FlightReservationDto validReservationDto;
    private FlightReservation reservation;

    @BeforeEach
    void setUp() {
        FlightDto flightDto = new FlightDto(1L, "Berlin", "London", 100, "RX212",
                LocalDateTime.now(), false);
        PassengerDto passengerDto = new PassengerDto(1L, "Joe", "Doe",
                "joe.doe@example.com", "123456789");

        validReservationDto = new FlightReservationDto(1L, "RD12", flightDto, "10A", passengerDto, false);
        reservation = new FlightReservation(1L, "RD12", null, "10A", null, false);
    }

    @Test
    void createShouldSaveReservationWhenEverythingIsOk() {
        when(mapper.dtoToEntity(validReservationDto)).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Long addedId = service.create(validReservationDto);

        assertEquals(1L, addedId);
        verify(validator).validateForCreate(validReservationDto);
        verify(reservationRepository).save(reservation);
        verify(emailService).sendEmail(validReservationDto);
    }

    @Test
    void createShouldThrowSeatAlreadyTakenException() {
        doThrow(new SeatAlreadyTakenException("Seat already in use"))
                .when(validator).validateForCreate(validReservationDto);

        SeatAlreadyTakenException exception = assertThrows(SeatAlreadyTakenException.class,
                () -> service.create(validReservationDto));

        assertEquals("Seat already in use", exception.getMessage());
        verify(reservationRepository, never()).save(any());
        verify(emailService, never()).sendEmail(any());
    }

    @Test
    void createShouldThrowDataNotFoundException() {
        doThrow(new DataNotFoundException("Flight not found"))
                .when(validator).validateForCreate(validReservationDto);

        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> service.create(validReservationDto));

        assertEquals("Flight not found", exception.getMessage());
        verify(reservationRepository, never()).save(any());
        verify(emailService, never()).sendEmail(any());
    }

    @Test
    void getAllReservationsShouldReturnListOfReservationsDtos() {
        List<FlightReservation> reservations = List.of(reservation);
        List<FlightReservationDto> reservationDtos = List.of(validReservationDto);

        when(reservationRepository.findAll()).thenReturn(reservations);
        when(mapper.entityListToDtoList(reservations)).thenReturn(reservationDtos);

        List<FlightReservationDto> result = service.getAllReservations();

        assertEquals(1, result.size());
        FlightReservationDto dto = result.getFirst();
        assertEquals("RD12", dto.reservationNumber());
        assertEquals("10A", dto.seatNumber());
        assertFalse(dto.hasDeparted());
    }

    @Test
    void updateShouldUpdateReservationWhenExists() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        service.update(1L, validReservationDto);

        assertAll(
                () -> assertEquals("RD12", reservation.getReservationNumber()),
                () -> assertEquals("10A", reservation.getSeatNumber()),
                () -> assertFalse(reservation.isHasDeparted())
        );

        verify(validator).validateForUpdate(validReservationDto, 1L);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void updateShouldThrowWhenReservationNotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        DataNotFoundException ex = assertThrows(DataNotFoundException.class,
                () -> service.update(1L, validReservationDto));

        assertEquals("Flight reservation not found", ex.getMessage());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void deleteShouldDeleteReservationWhenExists() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        service.delete(1L);

        verify(reservationRepository).delete(reservation);
    }

    @Test
    void deleteShouldThrowWhenReservationNotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        DataNotFoundException ex = assertThrows(DataNotFoundException.class,
                () -> service.delete(1L));

        assertEquals("Flight reservation not found", ex.getMessage());
        verify(reservationRepository, never()).delete(any());
    }
}