package com.lot_staz.bilet_system.web.controller;

import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.service.FlightReservationService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("api/reservations")
@RequiredArgsConstructor
public class FlightReservationController {
    private final FlightReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> addFlightReservation(@Valid @RequestBody FlightReservationDto reservationDto,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        reservationService.create(reservationDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FlightReservationDto>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable Long id, @Valid @RequestBody FlightReservationDto reservationDto,
                                                  BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        reservationService.update(id, reservationDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
