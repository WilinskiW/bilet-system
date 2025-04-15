package com.lot_staz.bilet_system.web.controller;

import com.lot_staz.bilet_system.web.dto.OkResponseDto;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.service.FlightService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<OkResponseDto> addFlight(@Valid @RequestBody FlightDto flightDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        Long addedId = flightService.create(flightDto);
        return ResponseEntity.ok(new OkResponseDto(addedId, "Flight was added!"));
    }

    @GetMapping
    public ResponseEntity<List<FlightDto>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDto> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlight(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OkResponseDto> updateFlight(@PathVariable Long id, @Valid @RequestBody FlightDto flightDto,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        flightService.update(id, flightDto);
        return ResponseEntity.ok(new OkResponseDto(id, "Flight with id: " + id + " was updated!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OkResponseDto> deleteFlight(@PathVariable Long id) {
        flightService.delete(id);
        return ResponseEntity.ok(new OkResponseDto(id, "Flight with id: " + id + " was deleted!"));
    }
}
