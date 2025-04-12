package com.lot_staz.bilet_system.data.repository;

import com.lot_staz.bilet_system.data.model.FlightReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightReservationRepository extends JpaRepository<FlightReservation, Long> {
    boolean existsBySeatNumber(String seatNumber);
}