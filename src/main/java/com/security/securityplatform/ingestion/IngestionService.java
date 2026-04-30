package com.security.securityplatform.ingestion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IngestionService {

    @Value("${abuseipdb.api.key}")
    private String abuseApiKey;

    @Value("${alienvault.api.key}")
    private String alienVaultApiKey;

    public String fetchFromAbuseIPDB() {
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;
                System.out.println("Attempt " + attempt + " - Fetching from AbuseIPDB...");

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Key", abuseApiKey);
                headers.set("Accept", "application/json");

                HttpEntity<String> entity = new HttpEntity<>(headers);
                String url = "https://api.abuseipdb.com/api/v2/blacklist?limit=20";

                ResponseEntity<String> response = restTemplate.exchange(
                        url, HttpMethod.GET, entity, String.class
                );

                System.out.println("Success on attempt " + attempt);
                return response.getBody();

            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt >= maxRetries) {
                    System.out.println("All retries exhausted!");
                    return "{\"error\": \"Failed after " + maxRetries + " attempts\"}";
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return "{\"error\": \"Unknown error\"}";
    }

    public String fetchFromAlienVault() {
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;
                System.out.println("Attempt " + attempt + " - Fetching from AlienVault...");

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("X-OTX-API-KEY", alienVaultApiKey);

                HttpEntity<String> entity = new HttpEntity<>(headers);
                String url = "https://otx.alienvault.com/api/v1/pulses/subscribed?limit=5";

                ResponseEntity<String> response = restTemplate.exchange(
                        url, HttpMethod.GET, entity, String.class
                );

                System.out.println("AlienVault success on attempt " + attempt);
                return response.getBody();

            } catch (Exception e) {
                System.out.println("AlienVault attempt " + attempt + " failed: " + e.getMessage());
                if (attempt >= maxRetries) {
                    System.out.println("All AlienVault retries exhausted!");
                    return "{\"error\": \"Failed after " + maxRetries + " attempts\"}";
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return "{\"error\": \"Unknown error\"}";
    }
}