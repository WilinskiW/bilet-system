package com.lot_staz.bilet_system.service;

import com.lot_staz.bilet_system.data.model.Passenger;
import com.lot_staz.bilet_system.data.repository.FlightReservationRepository;
import com.lot_staz.bilet_system.data.repository.PassengerRepository;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.mapper.PassengerMapper;
import com.lot_staz.bilet_system.web.service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private FlightReservationRepository reservationRepository;

    @Mock
    private PassengerMapper mapper;

    @InjectMocks
    private PassengerService passengerService;

    private PassengerDto passengerDto;
    private Passenger passenger;

    @BeforeEach
    void setUp() {
        this.passengerDto = new PassengerDto(1L, "Joe", "Doe", "joe.doe@example.com", "123456789");
        this.passenger = new Passenger(1L, "Joe", "Doe", "joe.doe@example.com", "123456789");
    }

    @Test
    void createShouldThrowExceptionWhenPassengerIdIsNotNull() {
        assertThrows(IllegalArgumentException.class, () -> passengerService.create(passengerDto));
        verify(passengerRepository, never()).save(passenger);
    }

    @Test
    void createShouldSavePassengerWhenPassengerIsNull() {
        PassengerDto passengerDtoWithoutId = new PassengerDto(null, "Joe", "Doe",
                "joe.doe@example.com", "123456789");

        Passenger mappedPassenger = new Passenger(1L, "Joe", "Doe",
                "joe.doe@example.com", "123456789");

        when(passengerRepository.save(mapper.dtoToEntity(passengerDto))).thenReturn(mappedPassenger);

        Long addedId = passengerService.create(passengerDtoWithoutId);

        assertEquals(1L, addedId);

        verify(passengerRepository, atMostOnce()).save(passenger);
    }

    @Test
    void getAllPassengersShouldReturnListOfPassengerDtos() {
        List<Passenger> passengers = List.of(passenger);
        List<PassengerDto> passengerDtos = List.of(passengerDto);

        when(passengerRepository.findAll()).thenReturn(passengers);
        when(mapper.entityListToDtoList(passengers)).thenReturn(passengerDtos);

        List<PassengerDto> result = passengerService.getAllPassengers();

        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("Joe", result.getFirst().firstname()),
                () -> assertEquals("Doe", result.getFirst().lastname()),
                () -> assertEquals("joe.doe@example.com", result.getFirst().email()),
                () -> assertEquals("123456789", result.getFirst().phone())
        );
    }

    @Test
    void getPassengerShouldReturnPassengerDtoWhenPassengerExist() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(this.passenger));
        when(mapper.entityToDto(this.passenger)).thenReturn(this.passengerDto);

        PassengerDto testedDto = passengerService.getPassenger(1L);
        assertEquals(passengerDto, testedDto);
    }

    @Test
    void getPassengerShouldThrowExceptionWhenPassengerDoesNotExist() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> passengerService.getPassenger(1L));
    }

    @Test
    void updateShouldThrowExceptionWhenPassengerDoesNotExist() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> passengerService.update(1L, passengerDto));
    }

    @Test
    void updateShouldSavePassengerWhenPassengerDoesExist() {
        PassengerDto updatedDto = new PassengerDto(1L, "Bill", "Hill",
                "bill.hill@example.com", "123456789");

        Passenger updatedPassenger = new Passenger(1L, "Bill", "Hill",
                "bill.hill@example.com", "123456789");

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        passengerService.update(1L, updatedDto);

        assertAll(
                () -> assertEquals("Bill", updatedDto.firstname()),
                () -> assertEquals("Hill", updatedDto.lastname()),
                () -> assertEquals("bill.hill@example.com", updatedDto.email()),
                () -> assertEquals("123456789", updatedDto.phone())
        );

        verify(passengerRepository).save(updatedPassenger);
    }

    @Test
    void deleteShouldThrowExceptionWhenPassengerDoesNotExist() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> passengerService.delete(1L));
    }

    @Test
    void deleteShouldDeletePassengerWhenPassengerDoesExist() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        passengerService.delete(1L);

        verify(reservationRepository, atMostOnce()).findPassengerById(1L);
        verify(passengerRepository, atMostOnce()).delete(passenger);
    }
}