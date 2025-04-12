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
        Passenger passenger = new Passenger();

        passenger.setId(dto.passengerId());
        passenger.setFirstname(dto.firstname());
        passenger.setLastname(dto.lastname());
        passenger.setEmail(dto.email());
        passenger.setPhone(dto.phone());

        return passenger;
    }
}
