package org.skb.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.skb.registration.dto.RegistrationRequest;
import org.skb.registration.entity.UserOutbox;
import org.skb.registration.mapper.UserMapper;
import org.skb.registration.repository.UserOutboxRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserOutboxProcessorService {

    private final UserOutboxRepository userOutboxRepository;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, RegistrationRequest> kafkaTemplate;

    @Value("${spring.kafka.topic.registration}")
    private String registrationTopic;

    @Value("${custom.statuses.pending}")
    private String pendingStatus;

    @Value("${custom.statuses.processed}")
    private String processedStatus;


    @Scheduled(fixedDelay = 3000)
    public void processOutboxMessages() {
        List<UserOutbox> userOutboxes = userOutboxRepository.findByStatus(pendingStatus);

        for (UserOutbox userOutbox : userOutboxes) {
            try {
                RegistrationRequest registrationRequest = userMapper.toRegistrationRequest(userOutbox);
                kafkaTemplate.send(registrationTopic, registrationRequest).addCallback(
                        result -> {
                            userOutbox.setStatus(processedStatus);
                            userOutboxRepository.save(userOutbox);
                            log.info("Message sent successfully to Kafka and status updated to 'PROCESSED' for UserOutbox with ID: {}", userOutbox.getId());
                        },
                        ex -> log.error("Failed to send message to Kafka: {}", ex.toString())
                );
            } catch (Exception ex) {
                log.error("Failed to send message to Kafka: {}", ex.toString());
            }
        }
    }
}
