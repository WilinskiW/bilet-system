package com.lot_staz.bilet_system.web.mapper;

import com.lot_staz.bilet_system.data.model.Flight;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import org.springframework.stereotype.Component;

@Component
public class FlightMapper implements Mapper<Flight, FlightDto> {
    @Override
    public FlightDto entityToDto(Flight flight) {
        return new FlightDto(
                flight.getId(),
                flight.getDeparturePlace(),
                flight.getArrivalPlace(),
                flight.getDuration(),
                flight.getFlightNumber(),
                flight.getDepartureTime(),
                flight.isRoundTrip()
        );
    }

    @Override
    public Flight dtoToEntity(FlightDto dto) {
        return new Flight(
                dto.id(),
                dto.departurePlace(),
                dto.arrivalPlace(),
                dto.duration(),
                dto.flightNumber(),
                dto.departureTime(),
                dto.roundTrip()
        );
    }
}
