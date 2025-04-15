package com.lot_staz.bilet_system.controller;

import com.lot_staz.bilet_system.web.controller.FlightController;
import com.lot_staz.bilet_system.web.dto.OkResponseDto;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.service.FlightService;
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

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FlightControllerTest {
    @Mock
    private FlightService flightService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private FlightController flightController;

    private FlightDto validFlightDto;

    @BeforeEach
    void setUp() {
        this.validFlightDto = new FlightDto(
                1L, "Berlin", "London", 120, "RX212",
                LocalDateTime.now(), true);
    }

    @Test
    void addFlightShouldReturnOkWhenFlightIsAdded() {
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<OkResponseDto> response = flightController.addFlight(validFlightDto, bindingResult);

        verify(flightService, atMostOnce()).create(validFlightDto);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void addFlightShouldThrowValidationExceptionWhenFlightIsInvalid() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(
                new FieldError("flightDto", "departurePlace", "Departure place is required")
        );

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> flightController.addFlight(validFlightDto, bindingResult)
        );
        assertEquals("Departure place is required", thrown.getMessage());
        verify(flightService, never()).create(validFlightDto);
    }

    @Test
    void updateFlightShouldReturnOkWhenFlightIsUpdated() {
        Long flightId = 1L;
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<OkResponseDto> response = flightController.updateFlight(flightId, validFlightDto, bindingResult);

        verify(flightService, atMostOnce()).update(flightId, validFlightDto);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void updateFlightShouldThrowValidationExceptionWhenFlightIsInvalid() {
        Long flightId = 1L;
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(
                new FieldError("flightDto", "arrivalPlace", "Arrival place is required")
        );

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> flightController.updateFlight(flightId, validFlightDto, bindingResult)
        );
        assertEquals("Arrival place is required", thrown.getMessage());
        verify(flightService, never()).update(flightId, validFlightDto);
    }

    @Test
    void deleteFlightShouldReturnOkWhenFlightExists() {
        Long flightId = 1L;

        ResponseEntity<OkResponseDto> response = flightController.deleteFlight(flightId);

        verify(flightService, atMostOnce()).delete(flightId);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
