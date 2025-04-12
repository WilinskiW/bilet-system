package com.lot_staz.bilet_system.data.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DbCatalog implements DbCataloger {
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;
    private final FlightReservationRepository flightReservationRepository;

    @Override
    public FlightRepository getFlightRepository() {
        return flightRepository;
    }

    @Override
    public PassengerRepository getPassengerRepository() {
        return passengerRepository;
    }

    @Override
    public FlightReservationRepository getFlightReservationRepository() {
        return flightReservationRepository;
    }
}
