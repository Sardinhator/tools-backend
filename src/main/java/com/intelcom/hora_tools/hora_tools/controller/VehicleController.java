package com.intelcom.hora_tools.hora_tools.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intelcom.hora_tools.hora_tools.dto.VehicleDTO;
import com.intelcom.hora_tools.hora_tools.service.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicles", description = "Vehicle management API with pagination and search")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    @Operation(summary = "Get all vehicles with pagination", description = "Retrieves a paginated list of vehicles with their vehicle types. Supports sorting and searching by VIN, plate number, make, model, or vehicle type name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of vehicles", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<Page<VehicleDTO>> getVehicles(
            @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "20") @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Field to sort by", example = "plateNumber") @RequestParam(defaultValue = "plateNumber") String sortBy,

            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC") @RequestParam(defaultValue = "ASC") String sortDirection,

            @Parameter(description = "Search term to filter vehicles by VIN, plate number, make, model, or vehicle type", example = "Toyota") @RequestParam(required = false) String search) {

        log.info("GET /api/vehicles - page: {}, size: {}, sortBy: {}, sortDirection: {}, search: {}",
                page, size, sortBy, sortDirection, search);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<VehicleDTO> vehicles = vehicleService.getVehiclesWithType(pageable, search);

        log.info("Returning {} vehicles out of {} total", vehicles.getNumberOfElements(), vehicles.getTotalElements());

        return ResponseEntity.ok(vehicles);
    }
}
