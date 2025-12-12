package com.intelcom.hora_tools.hora_tools.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Configuration properties for the VIN Decoder API
 */
@Configuration
@ConfigurationProperties(prefix = "app.vin-decoder")
@Data
public class VinDecoderConfig {

    /**
     * Base URL of the VIN decoder service
     * Example: https://vin-decoder-stg.intelcom.ca
     */
    private String baseUrl;

    /**
     * API key for authentication
     */
    private String apiKey;

    /**
     * Connection timeout in milliseconds (default: 5000)
     */
    private int connectionTimeout = 5000;

    /**
     * Read timeout in milliseconds (default: 10000)
     */
    private int readTimeout = 10000;

    /**
     * Whether VIN validation is enabled
     */
    private boolean enabled = true;
}
