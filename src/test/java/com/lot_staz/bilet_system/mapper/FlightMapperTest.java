package com.lot_staz.bilet_system.mapper;

import com.lot_staz.bilet_system.data.model.Flight;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.mapper.FlightMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlightMapperTest {
    private FlightMapper flightMapper;

    @BeforeEach
    void setUp() {
        this.flightMapper = new FlightMapper();
    }

    @Test
    void entityToDtoTest() {
        LocalDateTime departureTime = LocalDateTime.now();

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setDeparturePlace("Berlin");
        flight.setArrivalPlace("London");
        flight.setDuration(100);
        flight.setFlightNumber("123");
        flight.setDepartureTime(departureTime);
        flight.setRoundTrip(true);

        FlightDto flightDto = flightMapper.entityToDto(flight);
        FlightDto correctDto = new FlightDto(1L, "Berlin", "London", 100,
                "123", departureTime, true);

        assertEquals(correctDto, flightDto);
    }

    @Test
    void dtoToEntityTest() {
        LocalDateTime departureTime = LocalDateTime.now();

        FlightDto flightDto = new FlightDto(1L, "Berlin", "London", 100,
                "123", departureTime, true);
        Flight flight = flightMapper.dtoToEntity(flightDto);

        Flight correctFlight = new Flight();
        correctFlight.setId(1L);
        correctFlight.setDeparturePlace("Berlin");
        correctFlight.setArrivalPlace("London");
        correctFlight.setDuration(100);
        correctFlight.setFlightNumber("123");
        correctFlight.setDepartureTime(departureTime);
        correctFlight.setRoundTrip(true);

        assertEquals(correctFlight, flight);
    }
}
