package org.skb.registration.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.skb.registration.dto.RegistrationResponse;
import org.skb.registration.service.SendMailerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

@Service
@Log4j2
@RequiredArgsConstructor
public class RegistrationResponseListener {

    private final SendMailerService sendMailerService;

    @KafkaListener(topics = "${spring.kafka.topic.verification}", groupId = "${spring.kafka.consumer.group-id}")
    public void handeRegistrationResponse(RegistrationResponse registrationResponse) {
        try {
            sendMailerService.sendMail(registrationResponse);
        } catch (TimeoutException e) {
            log.error("Failed to send email to {} due to a timeout", registrationResponse.email());
        }
    }
}
