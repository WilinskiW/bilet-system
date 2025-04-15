package com.lot_staz.bilet_system.data.repository;

import com.lot_staz.bilet_system.data.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    boolean existsFlightByFlightNumber(String flightNumber);

    Flight findByFlightNumber(String flightNumber);
}