package com.example.notification.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @KafkaListener(
            containerFactory = "billingContainerFactory",
            groupId = "notificationService",
            topics = "${app.kafka-consumers.topic-name}"
    )
    public void getMessagePayload(String message) {
        logger.info("Order received {}", message);
    }

}

