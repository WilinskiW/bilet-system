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

import java.util.List;

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
        when(passengerService.create(validPassengerDto)).thenReturn(1L);

        ResponseEntity<OkResponseDto> response = passengerController.addPassenger(validPassengerDto, bindingResult);

        assertAll(
                () -> assertEquals(1L, response.getBody().id()),
                () -> assertEquals("Passenger was added!", response.getBody().message()),
                () -> assertTrue(response.getStatusCode().is2xxSuccessful())
        );

        verify(passengerService, atMostOnce()).create(validPassengerDto);
    }

    @Test
    void addPassengerShouldReturnErrorWhenPassengerIsInvalid() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(
                new FieldError("passengerDto", "firstname", "Firstname is required")
        );

        assertEquals("Firstname is required", assertThrows(ValidationException.class,
                () -> passengerController.addPassenger(null, bindingResult)).getMessage()
        );

        verify(passengerService, never()).create(validPassengerDto);
    }


    @Test
    void getAllPassengersShouldReturnListOfPassengerDtos() {
        when(passengerService.getAllPassengers()).thenReturn(List.of(validPassengerDto, validPassengerDto));

        ResponseEntity<List<PassengerDto>> response = passengerController.getAllPassengers();
        assertAll(
                () -> assertEquals(2, response.getBody().size()),
                () -> assertTrue(response.getStatusCode().is2xxSuccessful())
        );
    }

    @Test
    void getPassengerByIdShouldReturnPassengerDto() {
        when(passengerService.getPassenger(1L)).thenReturn(validPassengerDto);

        ResponseEntity<PassengerDto> response = passengerController.getPassengerById(1L);

        assertAll(
                () -> assertEquals(validPassengerDto, response.getBody()),
                () -> assertTrue(response.getStatusCode().is2xxSuccessful())
        );
    }

    @Test
    void updatePassengerShouldReturnOkWhenPassengerIsUpdated() {
        Long passengerId = 1L;
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<OkResponseDto> response = passengerController.updatePassenger(passengerId, validPassengerDto, bindingResult);

        assertAll(
                () -> assertEquals(1L, response.getBody().id()),
                () -> assertEquals("Passenger with id: 1 was updated!", response.getBody().message()),
                () -> assertTrue(response.getStatusCode().is2xxSuccessful())
        );

        verify(passengerService, atMostOnce()).update(passengerId, validPassengerDto);
    }

    @Test
    void updatePassengerShouldReturnErrorWhenPassengerIsInvalid() {
        Long passengerId = 1L;
        PassengerDto mockPassenger = new PassengerDto(1L, "Joe", "Doe", "eawe", "123456789");

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(
                new FieldError("passengerDto", "email", "Email is incorrect")
        );

        assertEquals("Email is incorrect", assertThrows(ValidationException.class,
                () -> passengerController.updatePassenger(passengerId, mockPassenger, bindingResult)).getMessage()
        );

        verify(passengerService, never()).update(passengerId, validPassengerDto);
    }

    @Test
    void deletePassengerShouldReturnOkWhenPassengerIsDeleted() {
        Long passengerId = 1L;

        ResponseEntity<OkResponseDto> response = passengerController.deletePassenger(passengerId);

        assertAll(
                () -> assertEquals(1L, response.getBody().id()),
                () -> assertEquals("Passenger with id: 1 was deleted!", response.getBody().message()),
                () -> assertTrue(response.getStatusCode().is2xxSuccessful())
        );

        verify(passengerService, atMostOnce()).delete(passengerId);
    }
}
