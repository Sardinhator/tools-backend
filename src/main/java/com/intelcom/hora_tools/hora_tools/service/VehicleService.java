package com.intelcom.hora_tools.hora_tools.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intelcom.hora_tools.hora_tools.dto.VehicleDTO;
import com.intelcom.hora_tools.hora_tools.dto.VehicleTypeDTO;
import com.intelcom.hora_tools.hora_tools.entity.Vehicle;
import com.intelcom.hora_tools.hora_tools.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public Page<VehicleDTO> getVehiclesWithType(Pageable pageable, String search) {
        log.debug("Fetching vehicles with type - Page: {}, Size: {}, Search: {}",
                pageable.getPageNumber(), pageable.getPageSize(), search);

        Page<Vehicle> vehicles;
        if (search != null && !search.trim().isEmpty()) {
            vehicles = vehicleRepository.searchVehiclesWithVehicleType(search.trim(), pageable);
            log.debug("Search returned {} vehicles", vehicles.getTotalElements());
        } else {
            vehicles = vehicleRepository.findAllWithVehicleType(pageable);
        }

        return vehicles.map(this::convertToDTO);
    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .idcrmVehicleId(vehicle.getIdcrmVehicleId())
                .vin(vehicle.getVin())
                .plateNumber(vehicle.getPlateNumber())
                .status(vehicle.getStatus())
                .companyId(vehicle.getCompanyId())
                .vehicleType(
                        vehicle.getVehicleType() != null ? convertVehicleTypeToDTO(vehicle.getVehicleType()) : null)
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .color(vehicle.getColor())
                .capacityCuft(vehicle.getCapacityCuft())
                .transmissionType(vehicle.getTransmissionType() != null ? vehicle.getTransmissionType().name() : null)
                .createdAt(vehicle.getCreatedAt())
                .updatedAt(vehicle.getUpdatedAt())
                .build();
    }

    private VehicleTypeDTO convertVehicleTypeToDTO(com.intelcom.hora_tools.hora_tools.entity.VehicleType vehicleType) {
        return VehicleTypeDTO.builder()
                .id(vehicleType.getId())
                .name(vehicleType.getName())
                .description(vehicleType.getDescription())
                .createdAt(vehicleType.getCreatedAt())
                .updatedAt(vehicleType.getUpdatedAt())
                .build();
    }
}
