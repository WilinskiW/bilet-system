package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.data.model.Flight;
import com.lot_staz.bilet_system.data.repository.FlightRepository;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.mapper.FlightMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    @Transactional
    public void create(FlightDto flightDto) {
        flightRepository.save(flightMapper.dtoToEntity(flightDto));
    }

    public List<FlightDto> getAllFlights() {
        return flightMapper.entityListToDtoList(flightRepository.findAll());
    }

    @Transactional
    public void update(Long id, FlightDto flightDto) {
        if(!flightRepository.existsById(id)) {
            throw new DataNotFoundException("Flight not found");
        }

        Flight flight = flightMapper.dtoToEntity(flightDto);
        flight.setId(id);

        flightRepository.save(flight);
    }

    @Transactional
    public void delete(Long id) {
        if(!flightRepository.existsById(id)) {
            throw new DataNotFoundException("Flight not found");
        }
        flightRepository.deleteById(id);
    }
}
