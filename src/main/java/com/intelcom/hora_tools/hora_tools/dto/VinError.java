package com.intelcom.hora_tools.hora_tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for VIN validation error details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Detailed error information for VIN validation failures")
public class VinError {

    @Schema(description = "Error code", example = "VIN_NOT_FOUND")
    private String code;

    @Schema(description = "Error message", example = "Vehicle data not found.")
    private String message;
}
