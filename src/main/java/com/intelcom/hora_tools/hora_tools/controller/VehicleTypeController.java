package com.intelcom.hora_tools.hora_tools.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intelcom.hora_tools.hora_tools.dto.VehicleTypeDTO;
import com.intelcom.hora_tools.hora_tools.service.VehicleTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/vehicle-types")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Types", description = "Vehicle type management API")
public class VehicleTypeController {

    private final VehicleTypeService vehicleTypeService;

    @GetMapping
    @Operation(summary = "Get all vehicle types", description = "Retrieves a list of all vehicle types available in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of vehicle types", content = @Content(schema = @Schema(implementation = VehicleTypeDTO.class)))
    })
    public ResponseEntity<List<VehicleTypeDTO>> getAllVehicleTypes() {
        log.info("GET /api/vehicle-types - Fetching all vehicle types");

        List<VehicleTypeDTO> vehicleTypes = vehicleTypeService.getAllVehicleTypes();

        log.info("Returning {} vehicle types", vehicleTypes.size());

        return ResponseEntity.ok(vehicleTypes);
    }
}
