package com.security.securityplatform.extraction;

import com.security.securityplatform.ingestion.IngestionService;
import com.security.securityplatform.model.IOC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extract")
public class ExtractionController {

    @Autowired
    private ExtractionService extractionService;

    @Autowired
    private IngestionService ingestionService;

    @GetMapping("/abuseipdb")
    public ResponseEntity<List<IOC>> extractAbuseIPDB() {
        String rawData = ingestionService.fetchFromAbuseIPDB();
        List<IOC> iocs = extractionService.extractFromAbuseIPDB(rawData);
        return ResponseEntity.ok(iocs);
    }

    @GetMapping("/alienvault")
    public ResponseEntity<List<IOC>> extractAlienVault() {
        String rawData = ingestionService.fetchFromAlienVault();
        List<IOC> iocs = extractionService.extractFromAlienVault(rawData);
        return ResponseEntity.ok(iocs);
    }

    @GetMapping("/all")
    public ResponseEntity<List<IOC>> extractAll() {
        List<IOC> allIOCs = new java.util.ArrayList<>();
        allIOCs.addAll(extractionService.extractFromAbuseIPDB(ingestionService.fetchFromAbuseIPDB()));
        allIOCs.addAll(extractionService.extractFromAlienVault(ingestionService.fetchFromAlienVault()));
        return ResponseEntity.ok(allIOCs);
    }
}
