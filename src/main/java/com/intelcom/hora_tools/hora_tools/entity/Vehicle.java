package com.intelcom.hora_tools.hora_tools.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "idcrm_vehicle_id", nullable = false, length = 50)
    private String idcrmVehicleId;

    @Column(name = "vin", nullable = false, unique = true, length = 50)
    private String vin;

    @Column(name = "plate_number", nullable = false, length = 25)
    private String plateNumber;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "company_id", nullable = false, length = 50)
    private String companyId;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType;

    @Column(name = "make", length = 50)
    private String make;

    @Column(name = "model", length = 50)
    private String model;

    @Column(name = "year")
    private Integer year;

    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "capacity_cuft")
    private Integer capacityCuft;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission_type", columnDefinition = "ENUM('Automatic','Manual')")
    private TransmissionType transmissionType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum TransmissionType {
        AUTOMATIC,
        MANUAL
    }
}
