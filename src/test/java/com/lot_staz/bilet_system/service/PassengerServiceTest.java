package com.lot_staz.bilet_system.service;

import com.lot_staz.bilet_system.data.model.Passenger;
import com.lot_staz.bilet_system.data.repository.PassengerRepository;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.mapper.PassengerMapper;
import com.lot_staz.bilet_system.web.service.PassengerService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

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
    void createShouldThrowExceptionWhenPassengerAlreadyExists() {
        when(passengerRepository.existsById(passengerDto.passengerId())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> passengerService.create(this.passengerDto));
    }

    @Test
    void createShouldSavePassengerWhenPassengerDoesNotExist() {
        when(passengerRepository.existsById(passengerDto.passengerId())).thenReturn(false);
        when(mapper.dtoToEntity(passengerDto)).thenReturn(passenger);

        passengerService.create(this.passengerDto);

        verify(passengerRepository).save(passenger);
    }

    @Test
    void getAllPassengersShouldReturnListOfPassengerDtos() {
        List<Passenger> passengers = List.of(passenger);
        List<PassengerDto> passengerDtos = List.of(passengerDto);

        when(passengerRepository.findAll()).thenReturn(passengers);
        when(mapper.entityListToDtoList(passengers)).thenReturn(passengerDtos);

        List<PassengerDto> result = passengerService.getAllPassengers();
        assertEquals(1, result.size());
        assertEquals("Joe", result.getFirst().firstname());
        assertEquals("Doe", result.getFirst().lastname());
        assertEquals("joe.doe@example.com", result.getFirst().email());
        assertEquals("123456789", result.getFirst().phone());
    }

    @Test
    void updateShouldThrowExceptionWhenPassengerDoesNotExist() {
        when(passengerRepository.existsById(any())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> passengerService.update(1L, passengerDto));
    }

    @Test
    void updateShouldSavePassengerWhenPassengerDoesExist() {
        when(passengerRepository.existsById(any())).thenReturn(true);
        when(mapper.dtoToEntity(passengerDto)).thenReturn(passenger);

        passengerService.update(1L, passengerDto);

        verify(passengerRepository).save(passenger);
    }

    @Test
    void deleteShouldThrowExceptionWhenPassengerDoesNotExist() {
        when(passengerRepository.existsById(any())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> passengerService.delete(1L));
    }

    @Test
    void deleteShouldDeletePassengerWhenPassengerDoesExist() {
        when(passengerRepository.existsById(any())).thenReturn(true);
        passengerService.delete(1L);
        verify(passengerRepository).deleteById(any());
    }
}