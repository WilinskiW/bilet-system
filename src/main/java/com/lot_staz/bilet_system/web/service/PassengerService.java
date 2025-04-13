package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.data.model.Passenger;
import com.lot_staz.bilet_system.data.repository.PassengerRepository;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.mapper.PassengerMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;
    private final PassengerMapper mapper;

    /**
     * Create new instance of passenger in database
     * @param passengerDto request body
     */
    @Transactional
    public void create(PassengerDto passengerDto) {
        if(passengerDto.passengerId() != null && passengerRepository.existsById(passengerDto.passengerId())) {
            throw new EntityExistsException("Passenger with id " + passengerDto.passengerId() + " already exists");
        }

        passengerRepository.save(mapper.dtoToEntity(passengerDto));
    }

    /**
     * Get all passengers from database
     * @return List of passengers DTOs
     */
    public List<PassengerDto> getAllPassengers() {
        return mapper.entityListToDtoList(passengerRepository.findAll());
    }

    /**
     * Update existing passenger using id and request body
     * @param id ID of existing passenger
     * @param passengerDto Request body with new data to change
     */
    @Transactional
    public void update(Long id, PassengerDto passengerDto) {
        if(!passengerRepository.existsById(id)) {
            throw new DataNotFoundException("Passenger not found");
        }

        Passenger passenger = mapper.dtoToEntity(passengerDto);
        passenger.setId(id);

        passengerRepository.save(passenger);
    }

    /**
     * Delete existing passenger by ID
     * @param id ID of passenger to delete
     */
    @Transactional
    public void delete(Long id) {
        if(!passengerRepository.existsById(id)) {
            throw new DataNotFoundException("Passenger not found");
        }
        passengerRepository.deleteById(id);
    }
}
