package org.skb.registration.mapper;

import org.junit.jupiter.api.Test;
import org.skb.registration.dto.RegistrationRequest;
import org.skb.registration.entity.User;
import org.skb.registration.entity.UserOutbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Value("${custom.statuses.pending}")
    private String pendingStatus;

    @Value("${custom.eventTypes.userRegistered}")
    private String eventType;

    @Test
    void toUser_shouldMapRegistrationRequestToUser() {
        RegistrationRequest request = new RegistrationRequest();
        request.setLogin("user");
        request.setPassword("password");
        request.setEmail("user@example.com");
        request.setFullname("User Name");

        User user = userMapper.toUser(request);

        assertThat(user.getLogin()).isEqualTo("user");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getEmail()).isEqualTo("user@example.com");
        assertThat(user.getFullname()).isEqualTo("User Name");
    }

    @Test
    void toUserOutbox_shouldMapRegistrationRequestToUserOutbox() {
        RegistrationRequest request = new RegistrationRequest();
        request.setLogin("user");
        request.setPassword("password");
        request.setEmail("user@example.com");
        request.setFullname("User Name");

        UserOutbox userOutbox = userMapper.toUserOutbox(request, 1L);

        assertThat(userOutbox.getUserId()).isEqualTo(1L);
        assertThat(userOutbox.getStatus()).isEqualTo(pendingStatus);
        assertThat(userOutbox.getLogin()).isEqualTo("user");
        assertThat(userOutbox.getPassword()).isEqualTo("password");
        assertThat(userOutbox.getEmail()).isEqualTo("user@example.com");
        assertThat(userOutbox.getFullname()).isEqualTo("User Name");
    }

    @Test
    void toRegistrationRequest_shouldMapUserOutboxToRegistrationRequest() {
        UserOutbox userOutbox = new UserOutbox();
        userOutbox.setEventType(eventType);
        userOutbox.setLogin("user");
        userOutbox.setPassword("password");
        userOutbox.setEmail("user@example.com");
        userOutbox.setFullname("User Name");
        userOutbox.setStatus(pendingStatus);

        RegistrationRequest request = userMapper.toRegistrationRequest(userOutbox);

        assertThat(request.getLogin()).isEqualTo("user");
        assertThat(request.getPassword()).isEqualTo("password");
        assertThat(request.getEmail()).isEqualTo("user@example.com");
        assertThat(request.getFullname()).isEqualTo("User Name");
    }
}
