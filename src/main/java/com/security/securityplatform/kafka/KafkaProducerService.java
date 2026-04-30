package com.security.securityplatform.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "ioc-topic";

    public void sendIOC(String iocData) {
        kafkaTemplate.send(TOPIC, iocData);
        System.out.println("Sent to Kafka: " + iocData);
    }
}
