package com.lot_staz.bilet_system.web.controller;

import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.service.FlightReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/reservations")
@RequiredArgsConstructor
public class FlightReservationController {
    private final FlightReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> addFlightReservation(@RequestBody FlightReservationDto reservationDto) {
        reservationService.create(reservationDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FlightReservationDto>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable Long id, @RequestBody FlightReservationDto reservationDto) {
        reservationService.update(id, reservationDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
