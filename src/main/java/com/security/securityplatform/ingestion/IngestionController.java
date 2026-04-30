package com.security.securityplatform.ingestion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingest")
public class IngestionController {

    @Autowired
    private IngestionService ingestionService;

    @GetMapping("/abuseipdb")
    public ResponseEntity<String> ingestAbuseIPDB() {
        String data = ingestionService.fetchFromAbuseIPDB();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/alienvault")
    public ResponseEntity<String> ingestAlienVault() {
        String data = ingestionService.fetchFromAlienVault();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/all")
    public ResponseEntity<String> ingestAll() {
        String abuse = ingestionService.fetchFromAbuseIPDB();
        String alien = ingestionService.fetchFromAlienVault();
        return ResponseEntity.ok("AbuseIPDB: " + abuse + "\nAlienVault: " + alien);
    }
}
