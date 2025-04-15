package com.lot_staz.bilet_system.web.controller;

import com.lot_staz.bilet_system.web.dto.OkResponseDto;
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
    public ResponseEntity<OkResponseDto> addPassenger(@Valid @RequestBody PassengerDto passengerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        Long addedId = passengerService.create(passengerDto);
        return ResponseEntity.ok(new OkResponseDto(addedId, "Passenger was added!"));
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
    public ResponseEntity<OkResponseDto> updatePassenger(@PathVariable Long id, @Valid @RequestBody PassengerDto passengerDto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        passengerService.update(id, passengerDto);
        return ResponseEntity.ok(new OkResponseDto(id, "Passenger with id: " + id + " was updated!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OkResponseDto> deletePassenger(@PathVariable Long id) {
        passengerService.delete(id);
        return ResponseEntity.ok(new OkResponseDto(id, "Passenger with id: " + id + " was deleted!"));
    }

    @PostMapping("/verify")
    public ResponseEntity<OkResponseDto> verifyPassenger(@Valid @RequestBody PassengerDto passengerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return ResponseEntity.ok(new OkResponseDto(null, "Passenger data are correct!"));
    }
}
