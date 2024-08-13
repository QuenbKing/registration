package org.skb.verification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.skb.verification.dto.RegistrationRequest;
import org.skb.verification.dto.RegistrationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
public class ApprovalService {

    private final KafkaTemplate<String, RegistrationResponse> kafkaTemplate;

    @Value("${spring.kafka.topic.verification}")
    private String verificationTopic;

    public void approveUser(RegistrationRequest userRequest) {
        boolean isApproved = new Random().nextBoolean();

        String status = isApproved ? "Your registration request was approved":"Your registration request was rejected";
        RegistrationResponse userResponse = new RegistrationResponse(userRequest.email(), status);


        try {
            kafkaTemplate.send(verificationTopic, userResponse).addCallback(
                    result -> log.info("Message sent successfully to Kafka with email {} and status {}", userRequest.email(), status),
                    ex -> log.error("Failed to send message to Kafka: {}", ex.toString())
            );
        } catch (Exception ex) {
            log.error("Failed to send message to Kafka: {}", ex.toString());
        }
    }
}
