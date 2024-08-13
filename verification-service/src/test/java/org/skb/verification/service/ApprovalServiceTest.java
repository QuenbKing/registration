package org.skb.verification.service;

import org.junit.jupiter.api.Test;
import org.skb.verification.dto.RegistrationRequest;
import org.skb.verification.dto.RegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ApprovalServiceTest {

    @MockBean
    private KafkaTemplate<String, RegistrationResponse> kafkaTemplate;

    @Autowired
    private ApprovalService approvalService;

    @Value("${spring.kafka.topic.verification}")
    private String verificationTopic;

    @Test
    void approveUser_success() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "testLogin1",
                "testPassword1",
                "test@example.com",
                "Test User");
        when(kafkaTemplate.send(eq(verificationTopic), any(RegistrationResponse.class))).thenReturn(null);

        approvalService.approveUser(registrationRequest);

        verify(kafkaTemplate).send(eq(verificationTopic), any(RegistrationResponse.class));
    }

    @Test
    void approveUser_kafkaSendFails() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "testLogin1",
                "testPassword1",
                "test@example.com",
                "Test User");
        when(kafkaTemplate.send(eq(verificationTopic), any(RegistrationResponse.class)))
                .thenThrow(new KafkaException("Kafka send failed"));

        approvalService.approveUser(registrationRequest);

        verify(kafkaTemplate).send(eq(verificationTopic), any(RegistrationResponse.class));
    }

    @Test
    void approveUser_randomFailure() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "testLogin1",
                "testPassword1",
                "test@example.com",
                "Test User");

        Random random = mock(Random.class);
        when(random.nextBoolean()).thenReturn(true);
        approvalService = new ApprovalService(kafkaTemplate);
        RegistrationResponse registrationResponse = new RegistrationResponse("test@example.com", "Your registration request was approved");

        approvalService.approveUser(registrationRequest);

        verify(kafkaTemplate).send(eq(verificationTopic), eq(registrationResponse));
    }
}
