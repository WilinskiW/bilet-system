package com.lot_staz.bilet_system.web.controller;

import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.dto.OkResponseDto;
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
    public ResponseEntity<OkResponseDto> addFlightReservation(@Valid @RequestBody FlightReservationDto reservationDto,
                                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        Long addedId = reservationService.create(reservationDto);
        return ResponseEntity.ok(new OkResponseDto(addedId, "Flight reservation was created!"));
    }

    @GetMapping
    public ResponseEntity<List<FlightReservationDto>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightReservationDto> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservation(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OkResponseDto> updateReservation(@PathVariable Long id, @Valid @RequestBody FlightReservationDto reservationDto,
                                                  BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        reservationService.update(id, reservationDto);
        return ResponseEntity.ok(new OkResponseDto(id, "Flight reservation with id: " + id + " was updated!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OkResponseDto> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok(new OkResponseDto(id, "Flight reservation with id: " + id + " was deleted!"));
    }
}
