package org.skb.verification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.skb.verification.dto.RegistrationRequest;
import org.skb.verification.service.ApprovalService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class RegistrationRequestListener {

    private final ApprovalService approvalService;

    @KafkaListener(topics = "${spring.kafka.topic.registration}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleRegistrationRequest(RegistrationRequest registrationRequest) {
        try {
            approvalService.approveUser(registrationRequest);
        } catch (Exception e) {
            log.error("Error handling registration request for email: {}", registrationRequest.email());
        }
    }
}
