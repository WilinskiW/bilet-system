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

    /**
     * Creates a new flight reservation if the provided data is valid.
     * The reservation will be saved in the repository, and an email will be sent to the passenger.
     *
     * @param reservationDto DTO containing the flight reservation data.
     * @throws com.lot_staz.bilet_system.web.exception.SeatAlreadyTakenException If the seat is already reserved.
     * @throws DataNotFoundException If the flight or passenger does not exist.
     */
    @Transactional
    public void create(FlightReservationDto reservationDto) {
        validator.checkIfValidForCreate(reservationDto);
        reservationRepository.save(mapper.dtoToEntity(reservationDto));
        emailService.sendEmail(reservationDto);
    }


    /**
     * Retrieves a list of all flight reservations.
     *
     * @return A list of DTOs representing all flight reservations.
     */
    public List<FlightReservationDto> getAllReservations() {
        return mapper.entityListToDtoList(reservationRepository.findAll());
    }

    public FlightReservationDto getReservation(Long id) {
        FlightReservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Reservation not found")
        );

        return mapper.entityToDto(reservation);
    }

    /**
     * Updates an existing flight reservation by the given ID.
     * If the reservation is found, the provided data will update the existing reservation.
     *
     * @param id The ID of the reservation to be updated.
     * @param reservationDto The new reservation data.
     * @throws DataNotFoundException If the reservation with the specified ID is not found.
     * @throws com.lot_staz.bilet_system.web.exception.SeatAlreadyTakenException If the seat number is already taken by another reservation.
     */
    @Transactional
    public void update(Long id, FlightReservationDto reservationDto) {
        FlightReservation existingReservation = reservationRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Flight reservation not found")
        );

        validator.checkIfValidForUpdate(reservationDto, id);

        existingReservation.setReservationNumber(reservationDto.reservationNumber());
        existingReservation.setSeatNumber(reservationDto.seatNumber());
        existingReservation.setHasDeparted(reservationDto.hasDeparted());

        reservationRepository.save(existingReservation);
    }

    /**
     * Deletes the flight reservation with the specified ID.
     *
     * @param id The ID of the reservation to be deleted.
     * @throws DataNotFoundException If the reservation with the specified ID is not found.
     */
    @Transactional
    public void delete(Long id) {
        FlightReservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Flight reservation not found")
        );

        reservationRepository.delete(reservation);
    }
}