package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.data.model.Passenger;
import com.lot_staz.bilet_system.data.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public void save(Passenger passenger) {
        passengerRepository.save(passenger);
    }

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public void update(Long id, Passenger passenger) {
        if(!passengerRepository.existsById(id)) {
            throw new RuntimeException("Passenger not found");
        }
        passengerRepository.save(passenger);
    }

    public void delete(Long id) {
        if(!passengerRepository.existsById(id)) {
            throw new RuntimeException("Passenger not found");
        }
        passengerRepository.deleteById(id);
    }
}
