package com.security.securityplatform.ranking;

import com.security.securityplatform.database.DatabaseService;
import com.security.securityplatform.model.IOCEntity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RankingService {

    @Value("${abuseipdb.api.key}")
    private String apiKey;

    @Autowired
    private DatabaseService databaseService;

    public int getSeverityScore(String ipAddress) {
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;
                System.out.println("Ranking attempt " + attempt + " for IP: " + ipAddress);

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Key", apiKey);
                headers.set("Accept", "application/json");

                HttpEntity<String> entity = new HttpEntity<>(headers);
                String url = "https://api.abuseipdb.com/api/v2/check?ipAddress=" + ipAddress;

                ResponseEntity<String> response = restTemplate.exchange(
                        url, HttpMethod.GET, entity, String.class
                );

                JSONObject json = new JSONObject(response.getBody());
                int score = json.getJSONObject("data").getInt("abuseConfidenceScore");
                System.out.println("Score for " + ipAddress + ": " + score);
                return score;

            } catch (Exception e) {
                System.out.println("Ranking attempt " + attempt + " failed for "
                        + ipAddress + ": " + e.getMessage());

                if (attempt >= maxRetries) {
                    System.out.println("All retries exhausted for: " + ipAddress);
                    return 0;
                }

                try {
                    Thread.sleep(1000); // wait 1 second before retry
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return 0;
    }

    public void processAndSave(String iocData) {
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;
                System.out.println("Processing attempt " + attempt + ": " + iocData);

                String[] parts = iocData.split(":", 3);
                if (parts.length >= 3) {
                    String type = parts[0];
                    String value = parts[1];
                    String source = parts[2];

                    int score = 0;
                    if (type.equals("IP")) {
                        score = getSeverityScore(value);
                    }
                    // Note: Domains get score 0 because AbuseIPDB only supports IP ranking.
                    // Domain ranking is a known limitation - future enhancement can use VirusTotal API.

                    IOCEntity entity = new IOCEntity();
                    entity.setType(type);
                    entity.setValue(value);
                    entity.setSource(source);
                    entity.setSeverityScore(score);

                    databaseService.saveIOC(entity);
                    System.out.println("Saved: " + value + " with score: " + score);
                    return; // success — exit loop
                }

            } catch (Exception e) {
                System.out.println("Process attempt " + attempt
                        + " failed: " + e.getMessage());

                if (attempt >= maxRetries) {
                    System.out.println("Failed to process after all retries: " + iocData);
                    return;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}