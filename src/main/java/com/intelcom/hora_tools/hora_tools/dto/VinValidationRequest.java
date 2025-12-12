package com.intelcom.hora_tools.hora_tools.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO for VIN validation
 */
@Data
@Schema(description = "Request to validate a Vehicle Identification Number (VIN)")
public class VinValidationRequest {

    @NotBlank(message = "VIN is required")
    @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN must contain only valid characters (excluding I, O, Q)")
    @Schema(description = "Vehicle Identification Number (17 characters)", example = "5FNRL3H82AB507091")
    private String vin;
}
