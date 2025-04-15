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
        FlightDto correctDto = new FlightDto(1L, "Berlin", "London", 100,
                "123", departureTime, true);

        Flight flight = new Flight(1L, "Berlin", "London",
                100, "123", departureTime, true);

        FlightDto flightDto = flightMapper.entityToDto(flight);

        assertEquals(correctDto, flightDto);
    }

    @Test
    void dtoToEntityTest() {
        LocalDateTime departureTime = LocalDateTime.now();
        Flight correctFlight = new Flight(1L, "Berlin", "London",
                100, "123", departureTime, true);

        FlightDto flightDto = new FlightDto(1L, "Berlin", "London", 100,
                "123", departureTime, true);

        Flight flight = flightMapper.dtoToEntity(flightDto);


        assertEquals(correctFlight, flight);
    }
}
