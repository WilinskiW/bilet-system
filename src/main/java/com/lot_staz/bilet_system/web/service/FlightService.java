package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.data.model.Flight;
import com.lot_staz.bilet_system.data.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;

    public void create(Flight flight) {
        flightRepository.save(flight);
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public void update(Long id, Flight flight) {
        if(!flightRepository.existsById(id)) {
            throw new RuntimeException("Flight not found");
        }
        flightRepository.save(flight);
    }

    public void delete(Long id) {
        if(!flightRepository.existsById(id)) {
            throw new RuntimeException("Flight not found");
        }
        flightRepository.deleteById(id);
    }
}
