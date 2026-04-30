package com.security.securityplatform.ranking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rank")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping("/check")
    public ResponseEntity<String> checkIP(@RequestParam String ip) {
        int score = rankingService.getSeverityScore(ip);
        return ResponseEntity.ok("IP: " + ip + " | Severity Score: " + score + "/100");
    }
}
