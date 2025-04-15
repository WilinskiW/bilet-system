package com.lot_staz.bilet_system.controller;

import com.lot_staz.bilet_system.web.controller.FlightReservationController;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.dto.OkResponseDto;
import com.lot_staz.bilet_system.web.service.FlightReservationService;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightReservationControllerTest {
    @Mock
    private FlightReservationService reservationService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private FlightReservationController flightReservationController;

    private FlightReservationDto validFlightReservationDto;

    @BeforeEach
    void setUp() {
        this.validFlightReservationDto = new FlightReservationDto(
                1L,
                "TEST RESERVATION",
                null,
                "10A",
                null,
                false
        );
    }

    @Test
    void addFlightReservationShouldReturnOkWhenReservationIsAdded() {
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<OkResponseDto> response = flightReservationController.addFlightReservation(validFlightReservationDto, bindingResult);

        verify(reservationService, atMostOnce()).create(validFlightReservationDto);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void addFlightReservationShouldReturnErrorWhenReservationIsInvalid() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(
                new FieldError("flightReservationDto", "reservationNumber", "Reservation number is required")
        );

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> flightReservationController.addFlightReservation(validFlightReservationDto, bindingResult)
        );

        assertEquals("Reservation number is required", thrown.getMessage());
        verify(reservationService, never()).create(validFlightReservationDto);
    }

    @Test
    void updateFlightReservationShouldReturnOkWhenReservationIsUpdated() {
        Long reservationId = 1L;
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<OkResponseDto> response = flightReservationController.updateReservation(reservationId, validFlightReservationDto, bindingResult);

        verify(reservationService, atMostOnce()).update(reservationId, validFlightReservationDto);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void updateFlightReservationShouldReturnErrorWhenReservationIsInvalid() {
        Long reservationId = 1L;
        FlightReservationDto mockReservation = new FlightReservationDto(
                1L,
                "TEST RESERVATION",
                null,
                "10B",
                null,
                true
        );

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(
                new FieldError("flightReservationDto", "seatNumber", "Seat number is required")
        );

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> flightReservationController.updateReservation(reservationId, mockReservation, bindingResult)
        );

        assertEquals("Seat number is required", thrown.getMessage());
        verify(reservationService, never()).update(reservationId, validFlightReservationDto);
    }

    @Test
    void deleteFlightReservationShouldReturnOkWhenReservationIsDeleted() {
        Long reservationId = 1L;

        ResponseEntity<OkResponseDto> response = flightReservationController.deleteReservation(reservationId);

        verify(reservationService, atMostOnce()).delete(reservationId);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
