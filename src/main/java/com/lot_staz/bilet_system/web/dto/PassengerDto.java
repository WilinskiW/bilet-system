package com.lot_staz.bilet_system.web.dto;

public record PassengerDto(
        Long passengerId,
        String firstname,
        String lastname,
        String email,
        String phone
) { }