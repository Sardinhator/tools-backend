package com.intelcom.hora_tools.hora_tools.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.intelcom.hora_tools.hora_tools.config.VinDecoderConfig;
import com.intelcom.hora_tools.hora_tools.dto.VinDecoderResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for validating and decoding Vehicle Identification Numbers (VINs)
 * using an external VIN decoder API
 */
@Service
@Slf4j
public class VinDecoderService {

    private final RestClient restClient;
    private final VinDecoderConfig config;

    public VinDecoderService(VinDecoderConfig config) {
        this.config = config;
        this.restClient = RestClient.builder()
                .baseUrl(config.getBaseUrl())
                .defaultHeader("x-api-key", config.getApiKey())
                .defaultHeader("Accept", "application/json")
                .build();

        log.info("VinDecoderService initialized with baseUrl: {}", config.getBaseUrl());
    }

    /**
     * Decode a VIN and retrieve vehicle information
     *
     * @param vin The Vehicle Identification Number (17 characters)
     * @return VinDecoderResponse containing decoded vehicle information
     * @throws VinDecoderException if the API call fails or returns an error
     */
    public VinDecoderResponse decodeVin(String vin) {
        try {
            log.debug("Decoding VIN: {}", vin);

            VinDecoderResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/vin/decode")
                            .queryParam("vin", vin)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, (request, clientResponse) -> {
                        log.error("Server error decoding VIN {}: {}", vin, clientResponse.getStatusCode());
                        throw new VinDecoderException("VIN decoder service is temporarily unavailable");
                    })
                    .body(VinDecoderResponse.class);

            if (response == null) {
                throw new VinDecoderException("Empty response from VIN decoder service");
            }

            log.info("Successfully decoded VIN: {} -> {}", vin, response.getDisplayName());
            return response;

        } catch (VinDecoderException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error decoding VIN: {}", vin, e);
            throw new VinDecoderException("Failed to decode VIN: " + e.getMessage(), e);
        }
    }

    /**
     * Validate if a VIN is valid without retrieving full details
     *
     * @param vin The Vehicle Identification Number
     * @return true if the VIN is valid, false otherwise
     */
    public boolean isValidVin(String vin) {
        try {
            VinDecoderResponse response = decodeVin(vin);
            return response.isSuccessful();
        } catch (Exception e) {
            log.warn("VIN validation failed for: {}", vin, e);
            return false;
        }
    }

    /**
     * Decode multiple VINs in a single batch request
     *
     * @param vins List of Vehicle Identification Numbers
     * @return List of VinDecoderResponse for each VIN
     * @throws VinDecoderException if the API call fails
     */
    public List<VinDecoderResponse> decodeBatch(List<String> vins) {
        try {
            log.info("Decoding batch of {} VINs", vins.size());

            // Create request body matching API expectations
            java.util.Map<String, List<String>> requestBody = java.util.Map.of("vins", vins);

            List<VinDecoderResponse> responses = restClient.post()
                    .uri("/api/v1/vin/decode/batch")
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, clientResponse) -> {
                        log.error("Client error decoding batch: {}", clientResponse.getStatusCode());
                        throw new VinDecoderException("Invalid batch request");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, clientResponse) -> {
                        log.error("Server error decoding batch: {}", clientResponse.getStatusCode());
                        throw new VinDecoderException("VIN decoder service is temporarily unavailable");
                    })
                    .body(new ParameterizedTypeReference<List<VinDecoderResponse>>() {
                    });

            if (responses == null || responses.isEmpty()) {
                throw new VinDecoderException("Empty response from VIN decoder batch service");
            }

            long successCount = responses.stream().filter(VinDecoderResponse::isSuccessful).count();
            log.info("Batch decode completed: {}/{} successful", successCount, responses.size());

            return responses;

        } catch (VinDecoderException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error decoding batch", e);
            throw new VinDecoderException("Failed to decode batch: " + e.getMessage(), e);
        }
    }

    /**
     * Custom exception for VIN decoder errors
     */
    public static class VinDecoderException extends RuntimeException {
        public VinDecoderException(String message) {
            super(message);
        }

        public VinDecoderException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
