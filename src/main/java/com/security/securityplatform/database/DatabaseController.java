package com.security.securityplatform.database;

import com.security.securityplatform.model.IOCEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/iocs")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping
    public ResponseEntity<List<IOCEntity>> getAllIOCs() {
        return ResponseEntity.ok(databaseService.getAllIOCs());
    }

    @GetMapping("/high-risk")
    public ResponseEntity<List<IOCEntity>> getHighRisk() {
        return ResponseEntity.ok(databaseService.getHighRiskIOCs());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<IOCEntity>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(databaseService.getIOCsByType(type));
    }

    @GetMapping("/source/{source}")
    public ResponseEntity<List<IOCEntity>> getBySource(@PathVariable String source) {
        return ResponseEntity.ok(databaseService.getIOCsBySource(source));
    }

    @GetMapping("/stats")
    public ResponseEntity<String> getStats() {
        long total = databaseService.getTotalCount();
        long highRisk = databaseService.getHighRiskIOCs().size();
        long ips = databaseService.getIOCsByType("IP").size();
        long domains = databaseService.getIOCsByType("Domain").size();

        String stats = "{"
            + "\"total\": " + total + ","
            + "\"highRisk\": " + highRisk + ","
            + "\"IPs\": " + ips + ","
            + "\"Domains\": " + domains
            + "}";
        return ResponseEntity.ok(stats);
    }
}
