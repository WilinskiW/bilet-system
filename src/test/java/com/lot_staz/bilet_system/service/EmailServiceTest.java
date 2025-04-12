package com.lot_staz.bilet_system.service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.lot_staz.bilet_system.web.dto.FlightDto;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.dto.PassengerDto;
import com.lot_staz.bilet_system.web.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceTest {

    private GreenMail greenMail;

    @Autowired
    private EmailService emailService;


    @BeforeEach
    void setUp() {
        this.greenMail = new GreenMail(new ServerSetup(5050, null, "smtp"));
        this.greenMail.setUser("testuser@localhost.com", "testpassword");
        greenMail.start();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    void sentEmailTest() throws MessagingException {
        FlightDto flightDto = new FlightDto(1L, "TEST dep", "TEST arr", 100,
                "TEST", LocalDateTime.now(), false);
        PassengerDto passengerDto = new PassengerDto(1L, "Joe", "Doe", "joedoe@gmail.com", "123456789");
        FlightReservationDto dto = new FlightReservationDto(
                1L,
                "TEST RESERVATION",
                flightDto,
                "TEST SEAT NUMBER",
                passengerDto,
                true
        );

        emailService.sendEmail(dto);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals("Rezerwacja biletów dla lotu: TEST", receivedMessages[0].getSubject());
        assertEquals("testuser@localhost.com", receivedMessages[0].getFrom()[0].toString());
    }
}
