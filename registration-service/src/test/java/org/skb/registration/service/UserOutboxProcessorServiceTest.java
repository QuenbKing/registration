package org.skb.registration.service;

import org.junit.jupiter.api.Test;
import org.skb.registration.dto.RegistrationRequest;
import org.skb.registration.entity.UserOutbox;
import org.skb.registration.mapper.UserMapper;
import org.skb.registration.repository.UserOutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {"scheduling.enabled: false"})
public class UserOutboxProcessorServiceTest {

    @MockBean
    private UserOutboxRepository userOutboxRepository;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private KafkaTemplate<String, RegistrationRequest> kafkaTemplate;

    @Autowired
    private UserOutboxProcessorService userOutboxProcessorService;

    @Value("${spring.kafka.topic.registration}")
    private String registrationTopic;

    @Value("${custom.statuses.pending}")
    private String pendingStatus;

    @Value("${custom.statuses.processed}")
    private String processedStatus;

    @Value("${custom.eventTypes.userRegistered}")
    private String eventType;

    @Test
    void processOutboxMessages_success() {
        UserOutbox userOutbox = new UserOutbox();
        userOutbox.setStatus(pendingStatus);
        userOutbox.setEventType(eventType);
        userOutbox.setLogin("testLogin1");
        userOutbox.setPassword("testPassword1");
        userOutbox.setEmail("test@example.com");
        userOutbox.setFullname("Test User");

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setLogin("testLogin1");
        registrationRequest.setPassword("testPassword1");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setFullname("Test User");

        when(userOutboxRepository.findByStatus(pendingStatus)).thenReturn(Collections.singletonList(userOutbox));
        when(userMapper.toRegistrationRequest(any(UserOutbox.class))).thenReturn(registrationRequest);

        SettableListenableFuture<SendResult<String, RegistrationRequest>> future = new SettableListenableFuture<>();
        future.set(new SendResult<>(null, null));
        when(kafkaTemplate.send(eq(registrationTopic), any(RegistrationRequest.class))).thenReturn(future);

        userOutboxProcessorService.processOutboxMessages();

        verify(userOutboxRepository).findByStatus(pendingStatus);
        verify(userMapper).toRegistrationRequest(userOutbox);
        verify(kafkaTemplate).send(eq(registrationTopic), any(RegistrationRequest.class));
        verify(userOutboxRepository).save(userOutbox);
        assertThat(userOutbox.getStatus()).isEqualTo(processedStatus);
    }

    @Test
    void processOutboxMessages_kafkaSendFails() {
        UserOutbox userOutbox = new UserOutbox();
        userOutbox.setStatus(pendingStatus);
        userOutbox.setStatus(eventType);
        userOutbox.setLogin("testLogin1");
        userOutbox.setPassword("testPassword1");
        userOutbox.setEmail("test@example.com");
        userOutbox.setFullname("Test User");

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setLogin("testLogin1");
        registrationRequest.setPassword("testPassword1");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setFullname("Test User");

        when(userOutboxRepository.findByStatus(pendingStatus)).thenReturn(Collections.singletonList(userOutbox));
        when(userMapper.toRegistrationRequest(any(UserOutbox.class))).thenReturn(registrationRequest);
        when(kafkaTemplate.send(eq(registrationTopic), any(RegistrationRequest.class))).thenThrow(new KafkaException("Kafka send failed"));

        userOutboxProcessorService.processOutboxMessages();

        verify(userOutboxRepository).findByStatus(pendingStatus);
        verify(userMapper).toRegistrationRequest(userOutbox);
        verify(kafkaTemplate).send(eq(registrationTopic), any(RegistrationRequest.class));
        verify(userOutboxRepository, never()).save(any(UserOutbox.class));
    }

    @Test
    void processOutboxMessages_userMapperThrowsException() {
        UserOutbox userOutbox = new UserOutbox();
        userOutbox.setStatus(pendingStatus);
        userOutbox.setStatus(eventType);
        userOutbox.setLogin("testLogin1");
        userOutbox.setPassword("testPassword1");
        userOutbox.setEmail("test@example.com");
        userOutbox.setFullname("Test User");

        when(userOutboxRepository.findByStatus(pendingStatus)).thenReturn(Collections.singletonList(userOutbox));
        when(userMapper.toRegistrationRequest(any(UserOutbox.class))).thenThrow(new RuntimeException("Mapping failed"));

        userOutboxProcessorService.processOutboxMessages();

        verify(userOutboxRepository).findByStatus(pendingStatus);
        verify(userMapper).toRegistrationRequest(userOutbox);
        verify(kafkaTemplate, never()).send(anyString(), any());
        verify(userOutboxRepository, never()).save(any(UserOutbox.class));
    }
}