package com.lot_staz.bilet_system.data.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "departure_place", nullable = false)
    private String departurePlace;

    @Column(name = "arrival_place", nullable = false)
    private String arrivalPlace;

    @Column(nullable = false)
    private Integer duration;

    @Column(name = "flight_number", nullable = false, unique = true)
    private String flightNumber;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "round_trip", nullable = false)
    private boolean roundTrip;

    public Flight(Long id, String departurePlace, String arrivalPlace, Integer duration, String flightNumber,
                  LocalDateTime departureTime, boolean roundTrip) {
        this.id = id;
        this.departurePlace = departurePlace;
        this.arrivalPlace = arrivalPlace;
        this.duration = duration;
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.roundTrip = roundTrip;
    }

    public Flight() {
    }
}