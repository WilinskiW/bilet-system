package com.lot_staz.bilet_system.web.controller;

import com.lot_staz.bilet_system.data.model.Passenger;
import com.lot_staz.bilet_system.web.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/passengers")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @PostMapping()
    public ResponseEntity<Void> addPassenger(@RequestBody Passenger passenger) {
        passengerService.create(passenger);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Passenger>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePassenger(@PathVariable Long id, @RequestBody Passenger passenger) {
        passengerService.update(id, passenger);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.delete(id);
        return ResponseEntity.ok().build();
    }
}
