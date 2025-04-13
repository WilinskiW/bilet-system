package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.data.model.FlightReservation;
import com.lot_staz.bilet_system.data.repository.FlightReservationRepository;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.mapper.FlightReservationMapper;
import com.lot_staz.bilet_system.web.utils.FlightReservationValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightReservationService {
    private final FlightReservationRepository reservationRepository;
    private final FlightReservationValidator validator;
    private final FlightReservationMapper mapper;
    private final EmailService emailService;

    @Transactional
    public void create(FlightReservationDto reservationDto) {
        try {
            validator.checkIfValid(reservationDto);
            reservationRepository.save(mapper.dtoToEntity(reservationDto));
            emailService.sendEmail(reservationDto);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong during reservation creation", e);
        }
    }


    public List<FlightReservationDto> getAllReservations() {
        return mapper.entityListToDtoList(reservationRepository.findAll());
    }

    @Transactional
    public void update(Long id, FlightReservationDto reservationDto) {
        FlightReservation existingReservation = reservationRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Flight reservation not found")
        );

        validator.checkIfValid(reservationDto, id);

        existingReservation.setReservationNumber(reservationDto.reservationNumber());
        existingReservation.setSeatNumber(reservationDto.seatNumber());
        existingReservation.setHasDeparted(reservationDto.hasDeparted());

        reservationRepository.save(existingReservation);
    }

    @Transactional
    public void delete(Long id) {
        FlightReservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Flight reservation not found")
        );

        reservationRepository.delete(reservation);
    }
}