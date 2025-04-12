package com.lot_staz.bilet_system.web.controller;

import com.lot_staz.bilet_system.data.model.Flight;
import com.lot_staz.bilet_system.web.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @PostMapping()
    public ResponseEntity<Void> addFlight(@RequestBody Flight flight) {
        flightService.create(flight);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFlight(@PathVariable Long id, @RequestBody Flight flight) {
        flightService.update(id, flight);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.delete(id);
        return ResponseEntity.ok().build();
    }
}
