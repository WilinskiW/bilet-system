package com.lot_staz.bilet_system.web.mapper;

import com.lot_staz.bilet_system.data.model.FlightReservation;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class FlightReservationMapper implements Mapper<FlightReservation, FlightReservationDto> {
    private final FlightMapper flightMapper;
    private final PassengerMapper passengerMapper;

    @Override
    public FlightReservationDto entityToDto(FlightReservation flightReservation) {
        return new FlightReservationDto(
                flightReservation.getId(),
                flightReservation.getReservationNumber(),
                flightMapper.entityToDto(flightReservation.getFlight()),
                flightReservation.getSeatNumber(),
                passengerMapper.entityToDto(flightReservation.getPassenger()),
                flightReservation.isHasDeparted()
        );
    }

    @Override
    public FlightReservation dtoToEntity(FlightReservationDto dto) {
        return new FlightReservation(
                dto.id(),
                dto.reservationNumber(),
                flightMapper.dtoToEntity(dto.flight()),
                dto.seatNumber(),
                passengerMapper.dtoToEntity(dto.passenger()),
                dto.hasDeparted()
        );
    }

}
