package com.lot_staz.bilet_system.mapper;

import com.lot_staz.bilet_system.data.model.Flight;
import com.lot_staz.bilet_system.data.model.FlightReservation;
import com.lot_staz.bilet_system.data.model.Passenger;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.mapper.FlightMapper;
import com.lot_staz.bilet_system.web.mapper.FlightReservationMapper;
import com.lot_staz.bilet_system.web.mapper.PassengerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FlightReservationMapperTest {
    @Mock
    private FlightMapper flightMapper;

    @Mock
    private PassengerMapper passengerMapper;

    @InjectMocks
    private FlightReservationMapper flightReservationMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void entityToDtoTest() {
        Flight flight = new Flight();
        Passenger passenger = new Passenger();
        FlightDto flightDto = new FlightDto(1L, "TEST dep", "TEST arr",
                100, "TSET", LocalDateTime.now(), false);
        PassengerDto passengerDto = new PassengerDto(1L, "Joe", "Doe", "joedoe@gmail.com",
                "123456789");

        FlightReservation flightReservation = new FlightReservation(1L,
                "TEST RESERVATION", flight, "TEST SEAT NUMBER", passenger, false );

        when(flightMapper.entityToDto(flight)).thenReturn(flightDto);
        when(passengerMapper.entityToDto(passenger)).thenReturn(passengerDto);

        FlightReservationDto dto = flightReservationMapper.entityToDto(flightReservation);

        assertAll(
                () -> assertEquals("TEST RESERVATION", dto.reservationNumber()),
                () -> assertEquals("TEST SEAT NUMBER", dto.seatNumber()),
                () -> assertEquals(flightDto, dto.flight()),
                () -> assertEquals(passengerDto, dto.passenger()),
                () -> assertFalse(dto.hasDeparted())
        );
    }

    @Test
    void dtoToEntityTest() {
        FlightDto flightDto = new FlightDto(1L, "TEST dep", "TEST arr", 100,
                "TSET", LocalDateTime.now(), false);
        PassengerDto passengerDto = new PassengerDto(1L, "Joe", "Doe", "joedoe@gmail.com", "123456789");
        FlightReservationDto dto = new FlightReservationDto(
                1L,
                "TEST RESERVATION",
                flightDto,
                "TEST SEAT NUMBER",
                passengerDto,
                true
        );

        Flight flight = new Flight(1L, "TEST dep", "TEST arr", 100,
                "TSET", LocalDateTime.now(), false);

        Passenger passenger = new Passenger(1L, "Joe", "Doe", "joedoe@gmail.com", "123456789");

        when(flightMapper.dtoToEntity(flightDto)).thenReturn(flight);
        when(passengerMapper.dtoToEntity(passengerDto)).thenReturn(passenger);

        FlightReservation reservation = flightReservationMapper.dtoToEntity(dto);

        assertAll(
                () -> assertEquals("TEST RESERVATION", reservation.getReservationNumber()),
                () -> assertEquals("TEST SEAT NUMBER", reservation.getSeatNumber()),
                () -> assertEquals(flight, reservation.getFlight()),
                () -> assertEquals(passenger, reservation.getPassenger()),
                () -> assertTrue(reservation.isHasDeparted())
        );
    }
}
