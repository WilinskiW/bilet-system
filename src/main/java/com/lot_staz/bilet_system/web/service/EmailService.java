package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEmail(FlightReservationDto reservationDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("systembiletow.lot.staz@gmail.com");
        message.setTo(reservationDto.passenger().email());
        message.setSubject("Rezerwacja biletów dla lotu: " + reservationDto.flight().flightNumber());
        message.setText(
                "Dziękujemy za kupienie biletu!\n" +
                        "Informacje o twoim bilecie: \n" +
                        "Numer rezerwacja: " + reservationDto.reservationNumber() + "\n" +
                        "Numer lotu: " + reservationDto.flight().flightNumber() + "\n" +
                        "Data wylotu: " + reservationDto.flight().departureTime()
        );

        mailSender.send(message);
    }
}
