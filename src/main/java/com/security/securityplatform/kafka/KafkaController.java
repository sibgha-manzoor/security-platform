package com.security.securityplatform.kafka;

import com.security.securityplatform.extraction.ExtractionService;
import com.security.securityplatform.ingestion.IngestionService;
import com.security.securityplatform.model.IOC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerService producerService;

    @Autowired
    private IngestionService ingestionService;

    @Autowired
    private ExtractionService extractionService;

    @PostMapping("/publish/abuseipdb")
    public ResponseEntity<String> publishAbuseIPDB() {
        String rawData = ingestionService.fetchFromAbuseIPDB();
        List<IOC> iocs = extractionService.extractFromAbuseIPDB(rawData);

        for (IOC ioc : iocs) {
            producerService.sendIOC(ioc.getType() + ":" + ioc.getValue() + ":" + ioc.getSource());
        }
        return ResponseEntity.ok("Published " + iocs.size() + " IOCs to Kafka!");
    }

    @PostMapping("/publish/alienvault")
    public ResponseEntity<String> publishAlienVault() {
        String rawData = ingestionService.fetchFromAlienVault();
        List<IOC> iocs = extractionService.extractFromAlienVault(rawData);

        for (IOC ioc : iocs) {
            producerService.sendIOC(ioc.getType() + ":" + ioc.getValue() + ":" + ioc.getSource());
        }
        return ResponseEntity.ok("Published " + iocs.size() + " IOCs to Kafka!");
    }

    @PostMapping("/publish/all")
    public ResponseEntity<String> publishAll() {
        // AbuseIPDB
        String abuseData = ingestionService.fetchFromAbuseIPDB();
        List<IOC> abuseIOCs = extractionService.extractFromAbuseIPDB(abuseData);
        for (IOC ioc : abuseIOCs) {
            producerService.sendIOC(ioc.getType() + ":" + ioc.getValue() + ":" + ioc.getSource());
        }

        // AlienVault
        String alienData = ingestionService.fetchFromAlienVault();
        List<IOC> alienIOCs = extractionService.extractFromAlienVault(alienData);
        for (IOC ioc : alienIOCs) {
            producerService.sendIOC(ioc.getType() + ":" + ioc.getValue() + ":" + ioc.getSource());
        }

        int total = abuseIOCs.size() + alienIOCs.size();
        return ResponseEntity.ok("Published total " + total + " IOCs to Kafka!");
    }
}
