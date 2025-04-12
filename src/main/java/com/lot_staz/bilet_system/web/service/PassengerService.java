package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.data.model.Passenger;
import com.lot_staz.bilet_system.data.repository.PassengerRepository;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.mapper.PassengerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;
    private final PassengerMapper mapper;

    @Transactional
    public void create(PassengerDto passengerDto) {
        passengerRepository.save(mapper.dtoToEntity(passengerDto));
    }

    public List<PassengerDto> getAllPassengers() {
        return mapper.entityListToDtoList(passengerRepository.findAll());
    }

    @Transactional
    public void update(Long id, PassengerDto passengerDto) {
        if(!passengerRepository.existsById(id)) {
            throw new RuntimeException("Passenger not found");
        }

        Passenger passenger = mapper.dtoToEntity(passengerDto);
        passenger.setId(id);

        passengerRepository.save(passenger);
    }

    @Transactional
    public void delete(Long id) {
        if(!passengerRepository.existsById(id)) {
            throw new RuntimeException("Passenger not found");
        }
        passengerRepository.deleteById(id);
    }
}
