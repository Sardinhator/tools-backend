package com.intelcom.hora_tools.hora_tools.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO for batch VIN validation
 */
@Data
@Schema(description = "Request to validate multiple VINs in a single call")
public class VinBatchRequest {

    @NotEmpty(message = "VIN list cannot be empty")
    @Size(max = 100, message = "Maximum 100 VINs allowed per batch request")
    @Schema(description = "List of Vehicle Identification Numbers to validate", example = "[\"2C4RDGBG2FR744017\", \"3N6CM0KN1LK705166\", \"5YJ3E1EBXKF469258\"]")
    private List<String> vins;
}
