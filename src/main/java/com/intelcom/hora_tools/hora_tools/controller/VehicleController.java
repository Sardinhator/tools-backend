package com.intelcom.hora_tools.hora_tools.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intelcom.hora_tools.hora_tools.dto.VehicleDTO;
import com.intelcom.hora_tools.hora_tools.dto.VinBatchRequest;
import com.intelcom.hora_tools.hora_tools.dto.VinDecoderResponse;
import com.intelcom.hora_tools.hora_tools.dto.VinError;
import com.intelcom.hora_tools.hora_tools.dto.VinValidationRequest;
import com.intelcom.hora_tools.hora_tools.service.VehicleService;
import com.intelcom.hora_tools.hora_tools.service.VinDecoderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicles", description = "Vehicle management API with pagination and search")
public class VehicleController {

        private final VehicleService vehicleService;
        private final VinDecoderService vinDecoderService;

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

                log.info("Returning {} vehicles out of {} total", vehicles.getNumberOfElements(),
                                vehicles.getTotalElements());

                return ResponseEntity.ok(vehicles);
        }

        @PostMapping("/validate-vin")
        @Operation(summary = "Validate and decode a VIN", description = "Validates a Vehicle Identification Number (VIN) and retrieves detailed vehicle information from the Intelcom VIN decoder service")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "VIN successfully decoded", content = @Content(schema = @Schema(implementation = VinDecoderResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid VIN format or VIN not found"),
                        @ApiResponse(responseCode = "500", description = "VIN decoder service error")
        })
        public ResponseEntity<VinDecoderResponse> validateVin(
                        @Valid @RequestBody VinValidationRequest request) {

                log.info("POST /api/vehicles/validate-vin - VIN: {}", request.getVin());

                try {
                        VinDecoderResponse response = vinDecoderService.decodeVin(request.getVin());

                        if (response.isSuccessful()) {
                                log.info("VIN {} decoded successfully: {}", request.getVin(),
                                                response.getDisplayName());
                                return ResponseEntity.ok(response);
                        } else {
                                log.warn("VIN {} validation failed: {}", request.getVin(),
                                                response.getFirstErrorMessage());
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                        }

                } catch (VinDecoderService.VinDecoderException e) {
                        log.error("VIN decoder service error for VIN {}: {}", request.getVin(), e.getMessage());
                        VinDecoderResponse errorResponse = new VinDecoderResponse();
                        errorResponse.setVin(request.getVin());
                        errorResponse.setErrors(List.of(new VinError("SERVICE_ERROR", e.getMessage())));
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                }
        }

        @GetMapping("/decode-vin")
        @Operation(summary = "Decode a VIN (GET method)", description = "Alternative GET endpoint to decode a VIN using query parameter")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "VIN successfully decoded"),
                        @ApiResponse(responseCode = "400", description = "Invalid or missing VIN"),
                        @ApiResponse(responseCode = "500", description = "VIN decoder service error")
        })
        public ResponseEntity<VinDecoderResponse> decodeVinByQuery(
                        @Parameter(description = "Vehicle Identification Number (17 characters)", example = "2C4RDGCG8KR111639") @RequestParam String vin) {

                log.info("GET /api/vehicles/decode-vin - VIN: {}", vin);

                if (vin == null || vin.length() != 17) {
                        log.warn("Invalid VIN format: {}", vin);
                        VinDecoderResponse errorResponse = new VinDecoderResponse();
                        errorResponse.setVin(vin);
                        errorResponse.setErrors(
                                        List.of(new VinError("INVALID_FORMAT", "VIN must be exactly 17 characters")));
                        return ResponseEntity.badRequest().body(errorResponse);
                }

                try {
                        VinDecoderResponse response = vinDecoderService.decodeVin(vin);

                        // Return 200 even if VIN has errors (following API contract)
                        if (response.isSuccessful()) {
                                log.info("VIN {} decoded successfully: {}", vin, response.getDisplayName());
                        } else {
                                log.warn("VIN {} has validation errors: {}", vin, response.getFirstErrorMessage());
                        }

                        return ResponseEntity.ok(response);

                } catch (VinDecoderService.VinDecoderException e) {
                        log.error("VIN decoder service error for VIN {}: {}", vin, e.getMessage());
                        VinDecoderResponse errorResponse = new VinDecoderResponse();
                        errorResponse.setVin(vin);
                        errorResponse.setErrors(List.of(new VinError("SERVICE_ERROR", e.getMessage())));
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                }
        }

        @PostMapping("/validate-vin/batch")
        @Operation(summary = "Validate and decode multiple VINs in batch", description = "Validates multiple Vehicle Identification Numbers (VINs) in a single request and retrieves detailed vehicle information for each")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "207", description = "Multi-Status: Batch processed, check individual results for success/failure", content = @Content(schema = @Schema(implementation = VinDecoderResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request (empty list, too many VINs)"),
                        @ApiResponse(responseCode = "500", description = "VIN decoder service error")
        })
        public ResponseEntity<List<VinDecoderResponse>> validateVinBatch(
                        @Valid @RequestBody VinBatchRequest request) {

                log.info("POST /api/vehicles/validate-vin/batch - {} VINs", request.getVins().size());

                try {
                        List<VinDecoderResponse> responses = vinDecoderService.decodeBatch(request.getVins());

                        long successCount = responses.stream().filter(VinDecoderResponse::isSuccessful).count();
                        log.info("Batch decode completed: {}/{} successful", successCount, responses.size());

                        // Return 207 Multi-Status for batch operations
                        return ResponseEntity.status(207).body(responses);

                } catch (VinDecoderService.VinDecoderException e) {
                        log.error("VIN decoder batch service error: {}", e.getMessage());
                        throw e;
                }
        }
}
