package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.data.model.Passenger;
import com.lot_staz.bilet_system.data.repository.PassengerRepository;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
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

    /**
     * Create new instance of passenger in database
     *
     * @param passengerDto request body
     * @throws IllegalArgumentException When passenger ID is not null when creating a new passenger
     */
    @Transactional
    public Long create(PassengerDto passengerDto) {
        if (passengerDto.id() != null) {
            throw new IllegalArgumentException("Passenger ID should be null when creating a new passenger");
        }

        Passenger savedPassenger = passengerRepository.save(mapper.dtoToEntity(passengerDto));
        return savedPassenger.getId();
    }

    /**
     * Get all passengers from database
     *
     * @return List of passengers DTOs
     */
    public List<PassengerDto> getAllPassengers() {
        return mapper.entityListToDtoList(passengerRepository.findAll());
    }

    /**
     * Get passenger by ID
     *
     * @param id Passenger ID
     * @throws DataNotFoundException If passenger not found in database
     * @return PassengerDto of search Passenger
     */
    public PassengerDto getPassenger(Long id) {
        Passenger passenger = passengerRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Passenger not found")
        );
        return mapper.entityToDto(passenger);
    }

    /**
     * Update existing passenger using id and request body
     *
     * @param id           ID of existing passenger
     * @param passengerDto Request body with new data to change
     * @throws DataNotFoundException When passenger can't be found in database
     */
    @Transactional
    public void update(Long id, PassengerDto passengerDto) {
        Passenger existingPassenger = passengerRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Passenger not found"));

        existingPassenger.setFirstname(passengerDto.firstname());
        existingPassenger.setLastname(passengerDto.lastname());
        existingPassenger.setEmail(passengerDto.email());
        existingPassenger.setPhone(passengerDto.phone());

        passengerRepository.save(existingPassenger);
    }

    /**
     * Delete existing passenger by ID
     *
     * @param id ID of passenger to delete
     * @throws DataNotFoundException When passenger can't be found in database
     */
    @Transactional
    public void delete(Long id) {
        Passenger passengerToDelete = passengerRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Passenger not found")
        );

        passengerRepository.delete(passengerToDelete);
    }
}
