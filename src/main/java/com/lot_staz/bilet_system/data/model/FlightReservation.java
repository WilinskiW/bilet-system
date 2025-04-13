package com.lot_staz.bilet_system.data.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FlightReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_number", nullable = false)
    private String reservationNumber;

    @OneToOne
    @JoinColumn(name = "flight_number")
    private Flight flight;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @OneToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @Column(name = "has_departed")
    private boolean hasDeparted;

    public FlightReservation(Long id, String reservationNumber, Flight flight, String seatNumber, Passenger passenger,
                             boolean hasDeparted) {
        this.id = id;
        this.reservationNumber = reservationNumber;
        this.flight = flight;
        this.seatNumber = seatNumber;
        this.passenger = passenger;
        this.hasDeparted = hasDeparted;
    }

    public FlightReservation() {
    }
}