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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    private FlightReservation existingReservation;

    @BeforeEach
    void setUp() {
        FlightDto flightDto = new FlightDto(1L, "Berlin", "London", 100, "RX212",
                LocalDateTime.now(), false);
        PassengerDto passengerDto = new PassengerDto(1L, "Joe", "Doe",
                "joe.doe@example.com", "123456789");

        this.validReservationDto = new FlightReservationDto(1L, "RD12", flightDto, "10A",
                passengerDto, false);

        this.existingReservation = new FlightReservation(1L, "RD12", null, "10A", null, false);
    }

    @Test
    void createShouldSaveReservationWhenEverythingIsOk() {
        service.create(validReservationDto);

        verify(validator, atMostOnce()).checkIfValidForCreate(validReservationDto);
        verify(reservationRepository, atMostOnce()).save(mapper.dtoToEntity(validReservationDto));
        verify(emailService, atMostOnce()).sendEmail(validReservationDto);
    }

    @Test
    void createShouldStopExecutingWhenValidationFails() {
        doThrow(new SeatAlreadyTakenException("Seat already in use")).when(validator).checkIfValidForCreate(validReservationDto);

        try {
            service.create(validReservationDto);
        } catch (SeatAlreadyTakenException ex) {
            assertEquals("Seat already in use", ex.getMessage());
            verify(validator, atMostOnce()).checkIfValidForCreate(validReservationDto);
            verify(reservationRepository, never()).save(mapper.dtoToEntity(validReservationDto));
            verify(emailService, never()).sendEmail(validReservationDto);
        }

        Mockito.clearInvocations(validator);
        doThrow(new DataNotFoundException("Flight not found")).when(validator).checkIfValidForCreate(validReservationDto);
        try {
            service.create(validReservationDto);
        } catch (DataNotFoundException ex) {
            assertEquals("Flight not found", ex.getMessage());
            verify(validator, atMostOnce()).checkIfValidForCreate(validReservationDto);
            verify(reservationRepository, never()).save(mapper.dtoToEntity(validReservationDto));
            verify(emailService, never()).sendEmail(validReservationDto);
        }
    }

    @Test
    void getAllReservationsShouldReturnListOfReservationsDtos() {
        List<FlightReservation> reservations = List.of(new FlightReservation(1L, "RD12", null,
                "10A", null, false));

        List<FlightReservationDto> reservationDtos = List.of(validReservationDto);

        when(reservationRepository.findAll()).thenReturn(reservations);
        when(mapper.entityListToDtoList(reservations)).thenReturn(reservationDtos);

        List<FlightReservationDto> result = service.getAllReservations();
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals("RD12", result.getFirst().reservationNumber());
        assertEquals("10A", result.getLast().seatNumber());
        assertFalse(result.getFirst().hasDeparted());
    }

    @Test
    void updateShouldUpdateReservationWhenEverythingIsOk() {
        FlightReservation updatedReservation = new FlightReservation();
        updatedReservation.setReservationNumber(validReservationDto.reservationNumber());
        updatedReservation.setSeatNumber(validReservationDto.seatNumber());
        updatedReservation.setHasDeparted(validReservationDto.hasDeparted());

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existingReservation));

        service.update(1L, validReservationDto);

        verify(reservationRepository, atMostOnce()).save(updatedReservation);
        verify(validator, atMostOnce()).checkIfValidForUpdate(validReservationDto, 1L);
    }

    @Test
    void updateShouldThrowExceptionWhenReservationNotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            service.update(1L, validReservationDto);
        } catch (DataNotFoundException ex) {
            assertEquals("Flight reservation not found", ex.getMessage());
        }

        verify(reservationRepository, atMostOnce()).findById(1L);
    }

    @Test
    void deleteShouldDeleteReservationWhenExists() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existingReservation));

        service.delete(1L);

        verify(reservationRepository, atMostOnce()).delete(existingReservation);
    }

    @Test
    void deleteShouldThrowExceptionWhenReservationNotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            service.delete(1L);
        } catch (DataNotFoundException ex) {
            assertEquals("Flight reservation not found", ex.getMessage());
        }

        verify(reservationRepository, atMostOnce()).findById(1L);
        verify(reservationRepository, never()).delete(existingReservation);
    }

}