package com.security.securityplatform.kafka;

import com.security.securityplatform.ranking.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private RankingService rankingService;

    @KafkaListener(topics = "ioc-topic", groupId = "security-group")
    public void consumeIOC(String iocData) {
        System.out.println("Received from Kafka: " + iocData);
        // Send to ranking service
        rankingService.processAndSave(iocData);
    }
}
