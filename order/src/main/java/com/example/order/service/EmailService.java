package com.example.order.service;


import com.example.order.dto.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${app.kafka.notification-topic-name}")
    private String topicName;

    private KafkaTemplate kafkaTemplate;
    private ObjectMapper objectMapper;

    public EmailService(KafkaTemplate kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendEmail(Order order) {
        try {
            kafkaTemplate.send(topicName, objectMapper.writeValueAsString(order));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
