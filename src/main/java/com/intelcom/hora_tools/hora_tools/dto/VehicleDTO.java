package com.intelcom.hora_tools.hora_tools.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {

    private Integer id;
    private String idcrmVehicleId;
    private String vin;
    private String plateNumber;
    private String status;
    private String companyId;
    private VehicleTypeDTO vehicleType;
    private String make;
    private String model;
    private Integer year;
    private String color;
    private Integer capacityCuft;
    private String transmissionType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
