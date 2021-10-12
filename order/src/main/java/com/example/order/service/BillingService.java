package com.example.order.service;

import com.example.order.dto.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    @Value("${app.kafka.billing-topic-name}")
    private String topicName;

    private KafkaTemplate kafkaTemplate;
    private ObjectMapper objectMapper;

    public BillingService(KafkaTemplate kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void charge(Order order) {
        try {
            kafkaTemplate.send(topicName, objectMapper.writeValueAsString(order));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
