package com.lot_staz.bilet_system.service;

import com.lot_staz.bilet_system.data.model.Flight;
import com.lot_staz.bilet_system.data.repository.FlightRepository;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.mapper.FlightMapper;
import com.lot_staz.bilet_system.web.service.FlightService;
import jakarta.persistence.EntityExistsException;
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
public class FlightServiceTest {
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightMapper mapper;

    @InjectMocks
    private FlightService flightService;

    private Flight flight;
    private FlightDto flightDto;

    @BeforeEach
    void setUp() {
        this.flight = new Flight(1L, "Berlin", "London", 100, "RX212",
                LocalDateTime.now(), false);
        this.flightDto = new FlightDto(1L, "Berlin", "London", 100, "RX212",
                LocalDateTime.now(), false);
    }

    @Test
    void createShouldSaveFlightWhenFlightIdIsNull() {
        FlightDto flightDtoWithoutId = new FlightDto(null, "Berlin", "London", 100,
                "RX212", LocalDateTime.of(1999, 12, 12, 12, 12), true);

        Flight mappedFlight = new Flight(1L, "Berlin", "London", 100,
                "RX212", LocalDateTime.of(1999, 12, 12, 12, 12), true);

        when(flightRepository.existsFlightByFlightNumber(any())).thenReturn(false);
        when(flightRepository.save(mapper.dtoToEntity(flightDtoWithoutId))).thenReturn(mappedFlight);

        Long addedId = flightService.create(flightDtoWithoutId);

        assertEquals(1L, addedId);
    }

    @Test
    void createShouldThrowExceptionWhenFlightIdIsNotNull() {
        assertThrows(IllegalArgumentException.class, () -> flightService.create(flightDto));
        verify(flightRepository, never()).save(flight);
    }

    @Test
    void getAllFlightsShouldReturnListOfFlightsDtos() {
        List<Flight> flights = List.of(flight);
        List<FlightDto> flightDtos = List.of(flightDto);

        when(flightRepository.findAll()).thenReturn(flights);
        when(mapper.entityListToDtoList(flights)).thenReturn(flightDtos);

        List<FlightDto> result = flightService.getAllFlights();
        assertEquals(1, result.size());
        assertEquals("Berlin", result.getFirst().departurePlace());
        assertEquals("London", result.getFirst().arrivalPlace());
        assertEquals(100, result.getFirst().duration());
        assertEquals("RX212", result.getFirst().flightNumber());
        assertFalse(result.getFirst().roundTrip());
    }

    @Test
    void getFlightByIdShouldReturnFlightDtoWhenFlightExist() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(mapper.entityToDto(flight)).thenReturn(flightDto);

        FlightDto outputDto = flightService.getFlight(1L);

        assertEquals(flightDto, outputDto);
    }

    @Test
    void getFlightIdShouldThrowExceptionWhenFlightDoesNotExist() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> flightService.getFlight(1L));
    }

    @Test
    void updateShouldThrowExceptionWhenFlightDoesNotExist() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> flightService.update(1L, flightDto));
    }

    @Test
    void updateShouldThrowExceptionWhenFlightNumberIsAlreadyTaken() {
        Flight flightWithFlightNumber = new Flight(2L, "New York", "Honk Kong", 100, "RX212",
                LocalDateTime.now(), false);

        when(flightRepository.findById(any())).thenReturn(Optional.of(flight));
        when(flightRepository.findByFlightNumber(flightDto.flightNumber())).thenReturn(flightWithFlightNumber);

        assertThrows(EntityExistsException.class, () -> flightService.update(1L, flightDto));
    }

    @Test
    void updateShouldUpdateFieldsWhenFlightExists() {
        Flight existingFlight = new Flight(1L, "Old", "Old", 60, "OLD100",
                LocalDateTime.of(2024, 1, 1, 10, 0), false);

        FlightDto newFlightDto = new FlightDto(1L, "Warszawa", "Londyn", 120,
                "RX100", LocalDateTime.of(2025, 1, 1, 15, 0), true);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));

        flightService.update(1L, newFlightDto);

        assertAll(
                () -> assertEquals("Warszawa", existingFlight.getDeparturePlace()),
                () -> assertEquals("Londyn", existingFlight.getArrivalPlace()),
                () -> assertEquals(120, existingFlight.getDuration()),
                () -> assertEquals("RX100", existingFlight.getFlightNumber()),
                () -> assertEquals(LocalDateTime.of(2025, 1, 1, 15, 0), existingFlight.getDepartureTime()),
                () -> assertTrue(existingFlight.isRoundTrip()),
                () -> verify(flightRepository).save(existingFlight)
        );
    }


    @Test
    void deleteShouldThrowExceptionWhenFlightDoesNotExist() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> flightService.delete(1L));
    }

    @Test
    void deleteShouldDeleteFlightWhenFlightExists() {
        Flight flight = new Flight();
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        flightService.delete(1L);

        verify(flightRepository).delete(flight);
    }
}
