package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.data.model.Flight;
import com.lot_staz.bilet_system.data.repository.FlightRepository;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.mapper.FlightMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    /**
     * Creates a new flight in the database.
     *
     * @param flightDto the flight data to create
     * @return the ID of the created flight
     * @throws IllegalArgumentException if flight ID is not null (should be null when creating)
     * @throws EntityExistsException if a flight with the same flight number already exists
     */
    @Transactional
    public Long create(FlightDto flightDto) {
        if (flightDto.id() != null) {
            throw new IllegalArgumentException("Flight ID should be null when creating a new flight");
        }

        if(flightRepository.existsFlightByFlightNumber(flightDto.flightNumber())){
            throw new EntityExistsException("Flight with this flight number already exists");
        }

        Flight addedFlight = flightRepository.save(flightMapper.dtoToEntity(flightDto));

        return addedFlight.getId();
    }

    /**
     * Get all flights from database
     *
     * @return List of flights DTOs
     */
    public List<FlightDto> getAllFlights() {
        return flightMapper.entityListToDtoList(flightRepository.findAll());
    }


    /**
     * Get flight by ID
     * @param id ID of flight to get
     * @return FlightDto of search Flight
     * @throws DataNotFoundException If flight doesn't exist
     */
    public FlightDto getFlight(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Flight not found"));

        return flightMapper.entityToDto(flight);
    }

    /**
     * Update existing flight using id and request body
     *
     * @param id        ID of existing flight
     * @param flightDto Request body with new data to change
     * @throws DataNotFoundException When flight can't be found in database
     */
    @Transactional
    public void update(Long id, FlightDto flightDto) {
        Flight existingFlight = flightRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Flight not found"));

        existingFlight.setFlightNumber(flightDto.flightNumber());
        existingFlight.setDeparturePlace(flightDto.departurePlace());
        existingFlight.setArrivalPlace(flightDto.arrivalPlace());
        existingFlight.setDuration(flightDto.duration());
        existingFlight.setDepartureTime(flightDto.departureTime());
        existingFlight.setRoundTrip(flightDto.roundTrip());

        flightRepository.save(existingFlight);
    }

    /**
     * Delete existing flight by ID
     *
     * @param id ID of flight to delete
     * @throws DataNotFoundException When flight can't be found in database
     */
    @Transactional
    public void delete(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Flight not found"));

        flightRepository.delete(flight);
    }
}
