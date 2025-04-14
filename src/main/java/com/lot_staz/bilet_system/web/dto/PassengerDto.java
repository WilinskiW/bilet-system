package com.lot_staz.bilet_system.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record PassengerDto(
        Long id,

        @NotEmpty(message = "Firstname is required")
        String firstname,

        @NotEmpty(message = "Lastname is required")
        String lastname,

        @Pattern(regexp = "[a-z0-9]+@[a-z]+\\.[a-z]{2,}$", message = "Email is incorrect")
        @NotEmpty(message = "Email can not be null")
        String email,

        @Pattern(
                regexp = "^\\+[1-9][0-9]{0,2}[0-9]{7,12}$",
                message = "Numer telefonu musi zaczynać się od '+' i zawierać od 8 do 15 cyfr"
        )
        String phone
) { }