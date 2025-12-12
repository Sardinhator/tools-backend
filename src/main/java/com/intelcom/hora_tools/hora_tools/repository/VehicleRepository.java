package com.intelcom.hora_tools.hora_tools.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.intelcom.hora_tools.hora_tools.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH v.vehicleType")
    Page<Vehicle> findAllWithVehicleType(Pageable pageable);

    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH v.vehicleType vt " +
            "WHERE LOWER(v.vin) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(v.plateNumber) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(v.make) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(v.model) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(vt.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Vehicle> searchVehiclesWithVehicleType(String search, Pageable pageable);
}
