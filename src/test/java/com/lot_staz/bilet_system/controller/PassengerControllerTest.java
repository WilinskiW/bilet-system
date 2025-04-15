package com.lot_staz.bilet_system.controller;

import com.lot_staz.bilet_system.web.controller.PassengerController;
import com.lot_staz.bilet_system.web.dto.OkResponseDto;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.service.PassengerService;
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
public class PassengerControllerTest {
    @Mock
    private PassengerService passengerService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PassengerController passengerController;

    private PassengerDto validPassengerDto;

    @BeforeEach
    void setUp() {
        this.validPassengerDto = new PassengerDto(1L, "Joe", "Doe",
                "joe.doe@example.com", "123456789");
    }

    @Test
    void addPassengerShouldReturnOkWhenPassengerIsAdded() {
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<OkResponseDto> response = passengerController.addPassenger(validPassengerDto, bindingResult);

        verify(passengerService, atMostOnce()).create(validPassengerDto);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void addPassengerShouldReturnErrorWhenPassengerIsInvalid() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(
                new FieldError("passengerDto", "firstname", "Firstname is required")
        );

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> passengerController.addPassenger(null, bindingResult)
        );

        assertEquals("Firstname is required", thrown.getMessage());
        verify(passengerService, never()).create(validPassengerDto);
    }

    @Test
    void updatePassengerShouldReturnOkWhenPassengerIsUpdated() {
        Long passengerId = 1L;
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<OkResponseDto> response = passengerController.updatePassenger(passengerId, validPassengerDto, bindingResult);

        verify(passengerService, atMostOnce()).update(passengerId, validPassengerDto);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void updatePassengerShouldReturnErrorWhenPassengerIsInvalid() {
        Long passengerId = 1L;
        PassengerDto mockPassenger = new PassengerDto(1L, "Joe", "Doe", "eawe", "123456789");

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(
                new FieldError("passengerDto", "email", "Email is incorrect")
        );

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> passengerController.updatePassenger(passengerId, mockPassenger, bindingResult)
        );

        assertEquals("Email is incorrect", thrown.getMessage());
        verify(passengerService, never()).update(passengerId, validPassengerDto);
    }

    @Test
    void deletePassengerShouldReturnOkWhenPassengerIsDeleted() {
        Long passengerId = 1L;

        ResponseEntity<OkResponseDto> response = passengerController.deletePassenger(passengerId);

        verify(passengerService, atMostOnce()).delete(passengerId);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
