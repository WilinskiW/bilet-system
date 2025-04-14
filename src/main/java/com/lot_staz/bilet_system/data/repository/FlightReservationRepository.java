package com.lot_staz.bilet_system.data.repository;

import com.lot_staz.bilet_system.data.model.FlightReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightReservationRepository extends JpaRepository<FlightReservation, Long> {
    @Query("SELECT COUNT(fr) > 0 FROM FlightReservation fr WHERE fr.seatNumber = :seatNumber AND fr.flight.id = :id")
    boolean existsBySeatNumberAndFlightId(@Param("seatNumber") String seatNumber, @Param("id") Long id);

    @Query("SELECT fr FROM FlightReservation fr WHERE  fr.seatNumber = :seatNumber AND fr.flight.id = :id")
    Optional<FlightReservation> findBySeatNumberAndFlightId(@Param("seatNumber") String seatNumber, @Param("id") Long id);

    @Query("SELECT p FROM FlightReservation p WHERE p.passenger.id = :id")
    Optional<FlightReservation> findPassengerById(@Param("id") Long id);

    FlightReservation findByReservationNumber(String reservationNumber);
}