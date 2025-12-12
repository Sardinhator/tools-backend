package com.intelcom.hora_tools.hora_tools.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO for VIN Decoder API response
 * Maps the response from the Intelcom VIN decoder service
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Response from VIN decoder containing vehicle information")
public class VinDecoderResponse {

    @Schema(description = "Vehicle Identification Number", example = "2C4RDGCG8KR111639")
    private String vin;

    @Schema(description = "Vehicle manufacturer/brand", example = "dodge")
    private String make;

    @Schema(description = "Vehicle model", example = "grand caravan")
    private String model;

    @Schema(description = "Manufacturing year", example = "2019")
    private Integer year;

    @Schema(description = "Intelcom internal vehicle type classification", example = "120 Gas")
    private String intelcomVehicleType;

    @Schema(description = "Engine fuel type", example = "gas")
    private String engineType;

    @Schema(description = "List of detailed validation errors if VIN decoding failed")
    private List<VinError> errors;

    /**
     * Check if the VIN decoding was successful
     */
    public boolean isSuccessful() {
        return errors == null || errors.isEmpty();
    }

    /**
     * Check if there were errors during VIN decoding
     */
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    /**
     * Get a simplified display name for the vehicle
     */
    public String getDisplayName() {
        if (year != null && make != null && model != null) {
            return year + " " + capitalize(make) + " " + capitalize(model);
        }
        return "Unknown Vehicle";
    }

    /**
     * Get the first error message if present
     */
    public String getFirstErrorMessage() {
        return (errors != null && !errors.isEmpty()) ? errors.get(0).getMessage() : null;
    }

    /**
     * Get the first error code if present
     */
    public String getFirstErrorCode() {
        return (errors != null && !errors.isEmpty()) ? errors.get(0).getCode() : null;
    }

    /**
     * Helper method to capitalize first letter of each word
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return result.toString().trim();
    }
}
