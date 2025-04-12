package com.lot_staz.bilet_system.web.service;

import com.lot_staz.bilet_system.data.model.FlightReservation;
import com.lot_staz.bilet_system.data.repository.DbCataloger;
import com.lot_staz.bilet_system.web.dto.FlightReservationDto;
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

    @Transactional
    public void create(FlightReservationDto reservationDto) {
        checkExistenceOfData(reservationDto);
        dbCataloger.getFlightReservationRepository().save(mapper.dtoToEntity(reservationDto));
    }

    private void checkExistenceOfData(FlightReservationDto reservationDto) {
        if (!dbCataloger.getFlightRepository().existsById(reservationDto.flight().flightId())) {
            throw new RuntimeException("Flight not found");
        }

        if (!dbCataloger.getPassengerRepository().existsById(reservationDto.passenger().passengerId())) {
            throw new RuntimeException("Passenger not found");
        }
    }

    public List<FlightReservationDto> getAllReservations() {
        return mapper.entityListToDtoList(dbCataloger.getFlightReservationRepository().findAll());
    }

    @Transactional
    public void update(Long id, FlightReservationDto reservationDto) {
        checkExistenceOfData(reservationDto);

        Optional<FlightReservation> reservation = dbCataloger.getFlightReservationRepository().findById(id);

        if(reservation.isEmpty()){
            throw new RuntimeException("Flight reservation not found");
        }

        var flightReservation = reservation.get();

        flightReservation.setReservationNumber(reservationDto.reservationNumber());
        flightReservation.setSeatNumber(reservationDto.seatNumber());
        flightReservation.setHasDeparted(reservationDto.hasDeparted());

        dbCataloger.getFlightReservationRepository().save(flightReservation);
    }

    @Transactional
    public void delete(Long id) {
        if(!dbCataloger.getFlightReservationRepository().existsById(id)){
            throw new RuntimeException("Flight reservation not found");
        }
        dbCataloger.getFlightReservationRepository().deleteById(id);
    }
}