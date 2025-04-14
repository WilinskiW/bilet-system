package com.lot_staz.bilet_system.data.repository;

import com.lot_staz.bilet_system.data.model.FlightReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightReservationRepository extends JpaRepository<FlightReservation, Long> {
    @Query("SELECT COUNT(fr) > 0 FROM FlightReservation fr WHERE fr.seatNumber = :seatNumber AND fr.flight.id = :flightId")
    boolean existsBySeatNumberAndFlightId(@Param("seatNumber") String seatNumber, @Param("id") Long flightId);

    @Query("SELECT fr from FlightReservation fr WHERE  fr.seatNumber = :seatNumber AND fr.flight.id = :flightId")
    Optional<FlightReservation> findBySeatNumberAndFlightId(@Param("seatNumber") String seatNumber, @Param("id") Long flightId);
}