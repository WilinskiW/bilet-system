package com.lot_staz.bilet_system.web.controller;

import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.service.PassengerService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/passengers")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @PostMapping
    public ResponseEntity<Void> addPassenger(@Valid @RequestBody PassengerDto passengerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        passengerService.create(passengerDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PassengerDto>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDto> getPassengerById(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassenger(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePassenger(@PathVariable Long id, @Valid @RequestBody PassengerDto passengerDto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        passengerService.update(id, passengerDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyPassenger(@Valid @RequestBody PassengerDto passengerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return ResponseEntity.ok().build();
    }
}
