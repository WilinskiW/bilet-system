package com.lot_staz.bilet_system.web.mapper;

import com.lot_staz.bilet_system.data.model.Passenger;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import org.springframework.stereotype.Component;

@Component
public class PassengerMapper implements Mapper<Passenger, PassengerDto> {
    @Override
    public PassengerDto entityToDto(Passenger passenger) {
        return new PassengerDto(
                passenger.getId(),
                passenger.getFirstname(),
                passenger.getLastname(),
                passenger.getEmail(),
                passenger.getPhone()
        );
    }

    @Override
    public Passenger dtoToEntity(PassengerDto dto) {
        return new Passenger(
                dto.id(),
                dto.firstname(),
                dto.lastname(),
                dto.email(),
                dto.phone()
        );
    }
}
