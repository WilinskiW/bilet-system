package com.lot_staz.bilet_system.controller;

import com.lot_staz.bilet_system.web.controller.FlightController;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.dto.OkResponseDto;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightControllerTest {
    @Mock
    private FlightService flightService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private FlightController flightController;

    private FlightDto flightDtoNoId;
    private FlightDto validFlightDto;

    @BeforeEach
    void setUp() {
        this.validFlightDto = new FlightDto(
                1L, "Berlin", "London", 120, "RX212",
                LocalDateTime.now(), true);
        this.flightDtoNoId = new FlightDto(
                null, "Berlin", "London", 120, "RX212",
                LocalDateTime.now(), true);
    }

    @Test
    void addFlightShouldReturnOkResponseDtoWhenFlightIsAdded() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(flightService.create(flightDtoNoId)).thenReturn(1L);

        ResponseEntity<OkResponseDto> response = flightController.addFlight(flightDtoNoId, bindingResult);

        verify(flightService, atMostOnce()).create(flightDtoNoId);
        assertAll(
                () -> assertEquals(1L, response.getBody().id()),
                () -> assertEquals("Flight was added!", response.getBody().message()),
                () -> assertTrue(response.getStatusCode().is2xxSuccessful())
        );
    }

    @Test
    void addFlightShouldThrowValidationExceptionWhenFlightIsInvalid() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(
                new FieldError("flightDto", "departurePlace", "Departure place is required")
        );

        assertEquals("Departure place is required", assertThrows(ValidationException.class,
                () -> flightController.addFlight(validFlightDto, bindingResult)).getMessage()
        );

        verify(flightService, never()).create(validFlightDto);
    }

    @Test
    void getAllFlightsShouldReturnListOfFlightDtos() {
        when(flightService.getAllFlights()).thenReturn(List.of(validFlightDto, validFlightDto));
        ResponseEntity<List<FlightDto>> response = flightController.getAllFlights();

        FlightDto flightDto = response.getBody().getFirst();
        assertAll(
                () -> assertEquals(2, response.getBody().size()),
                () -> assertEquals(validFlightDto, flightDto),
                () -> assertTrue(response.getStatusCode().is2xxSuccessful())
        );

        verify(flightService, atMostOnce()).getAllFlights();
    }

    @Test
    void getFlightByIdShouldReturnFlightDtoWhenFlightExistById() {
        when(flightService.getFlight(1L)).thenReturn(validFlightDto);

        ResponseEntity<FlightDto> response = flightController.getFlightById(1L);

        assertEquals(validFlightDto, response.getBody());
    }

    @Test
    void updateFlightShouldReturnOkWhenFlightIsUpdated() {
        Long flightId = 1L;
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<OkResponseDto> response = flightController.updateFlight(flightId, validFlightDto, bindingResult);

        assertAll(
                () -> assertEquals(1L, response.getBody().id()),
                () -> assertEquals("Flight with id: 1 was updated!", response.getBody().message()),
                () -> assertTrue(response.getStatusCode().is2xxSuccessful())
        );

        verify(flightService, atMostOnce()).update(flightId, validFlightDto);
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

        assertAll(
                () -> assertEquals(1L, response.getBody().id()),
                () -> assertEquals("Flight with id: 1 was deleted!", response.getBody().message()),
                () -> assertTrue(response.getStatusCode().is2xxSuccessful())
        );

        verify(flightService, atMostOnce()).delete(flightId);
    }
}
