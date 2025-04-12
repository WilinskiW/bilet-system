package com.lot_staz.bilet_system.data.repository;

import com.lot_staz.bilet_system.data.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> { }