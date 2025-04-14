package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableAsync
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendEmail(FlightReservationDto reservationDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
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
