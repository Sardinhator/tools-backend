package com.intelcom.hora_tools.hora_tools.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intelcom.hora_tools.hora_tools.dto.VehicleTypeDTO;
import com.intelcom.hora_tools.hora_tools.entity.VehicleType;
import com.intelcom.hora_tools.hora_tools.repository.VehicleTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleTypeService {

    private final VehicleTypeRepository vehicleTypeRepository;

    public List<VehicleTypeDTO> getAllVehicleTypes() {
        log.debug("Fetching all vehicle types");

        List<VehicleType> vehicleTypes = vehicleTypeRepository.findAll();

        log.debug("Found {} vehicle types", vehicleTypes.size());

        return vehicleTypes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private VehicleTypeDTO convertToDTO(VehicleType vehicleType) {
        return VehicleTypeDTO.builder()
                .id(vehicleType.getId())
                .name(vehicleType.getName())
                .description(vehicleType.getDescription())
                .createdAt(vehicleType.getCreatedAt())
                .updatedAt(vehicleType.getUpdatedAt())
                .build();
    }
}
