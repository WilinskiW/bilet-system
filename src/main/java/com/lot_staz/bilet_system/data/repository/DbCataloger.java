package com.lot_staz.bilet_system.data.repository;

public interface DbCataloger {
    FlightRepository getFlightRepository();
    PassengerRepository getPassengerRepository();
    FlightReservationRepository getFlightReservationRepository();
}
