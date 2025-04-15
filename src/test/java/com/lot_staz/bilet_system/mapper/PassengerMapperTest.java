package com.lot_staz.bilet_system.mapper;

import com.lot_staz.bilet_system.data.model.Passenger;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.mapper.PassengerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PassengerMapperTest {
    private PassengerMapper passengerMapper;

    @BeforeEach
    void setUp() {
        this.passengerMapper = new PassengerMapper();
    }

    @Test
    void entityToDtoTest() {
        Passenger entityPassenger =
                new Passenger(1L, "John", "Doe", "joedoe@gmail.com", "123456789");

        PassengerDto dto = passengerMapper.entityToDto(entityPassenger);
        PassengerDto correctDto = new PassengerDto(1L, "John", "Doe", "john@doe.com", "1234567890");

        assertEquals(correctDto, dto);
    }

    @Test
    void dtoToEntityTest() {
        PassengerDto dto = new PassengerDto(1L, "John", "Doe", "john@doe.com", "1234567890");
        Passenger passenger = passengerMapper.dtoToEntity(dto);

        Passenger correctPassenger = new Passenger(1L, "John", "Doe", "john@doe.com", "1234567890");

        assertEquals(correctPassenger, passenger);
    }
}
