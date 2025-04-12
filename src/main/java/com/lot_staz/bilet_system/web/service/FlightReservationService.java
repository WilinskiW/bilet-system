package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.data.model.FlightReservation;
import com.lot_staz.bilet_system.data.repository.DbCataloger;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
import com.lot_staz.bilet_system.web.exception.DataNotFoundException;
import com.lot_staz.bilet_system.web.exception.SeatAlreadyTakenException;
import com.lot_staz.bilet_system.web.mapper.FlightReservationMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightReservationService {
    private final DbCataloger dbCataloger;
    private final FlightReservationMapper mapper;
    private final EmailService emailService;

    @Transactional
    public void create(FlightReservationDto reservationDto) {
        verifyData(reservationDto);
        dbCataloger.getFlightReservationRepository().save(mapper.dtoToEntity(reservationDto));
        emailService.sendEmail(reservationDto);
    }

    private void verifyData(FlightReservationDto reservationDto) {
        if (!dbCataloger.getFlightRepository().existsById(reservationDto.flight().flightId())) {
            throw new DataNotFoundException("Flight not found");
        }

        if (!dbCataloger.getPassengerRepository().existsById(reservationDto.passenger().passengerId())) {
            throw new DataNotFoundException("Passenger not found");
        }

        if(dbCataloger.getFlightReservationRepository().existsBySeatNumber(reservationDto.seatNumber())) {
           throw new SeatAlreadyTakenException("Seat number already in use");
        }
    }

    public List<FlightReservationDto> getAllReservations() {
        return mapper.entityListToDtoList(dbCataloger.getFlightReservationRepository().findAll());
    }

    @Transactional
    public void update(Long id, FlightReservationDto reservationDto) {
        Optional<FlightReservation> reservation = dbCataloger.getFlightReservationRepository().findById(id);

        if(reservation.isEmpty()){
            throw new DataNotFoundException("Flight reservation not found");
        }

        verifyData(reservationDto);

        var flightReservation = reservation.get();

        flightReservation.setReservationNumber(reservationDto.reservationNumber());
        flightReservation.setSeatNumber(reservationDto.seatNumber());
        flightReservation.setHasDeparted(reservationDto.hasDeparted());

        dbCataloger.getFlightReservationRepository().save(flightReservation);
    }

    @Transactional
    public void delete(Long id) {
        if(!dbCataloger.getFlightReservationRepository().existsById(id)){
            throw new DataNotFoundException("Flight reservation not found");
        }
        dbCataloger.getFlightReservationRepository().deleteById(id);
    }
}