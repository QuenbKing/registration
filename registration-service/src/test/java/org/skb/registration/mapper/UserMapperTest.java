package org.skb.registration.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skb.registration.dto.RegistrationRequest;
import org.skb.registration.entity.User;
import org.skb.registration.entity.UserOutbox;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

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

        assertThat(userOutbox.getLogin()).isEqualTo("user");
        assertThat(userOutbox.getPassword()).isEqualTo("password");
        assertThat(userOutbox.getEmail()).isEqualTo("user@example.com");
        assertThat(userOutbox.getFullname()).isEqualTo("User Name");
    }

    @Test
    void toRegistrationRequest_shouldMapUserOutboxToRegistrationRequest() {
        UserOutbox userOutbox = new UserOutbox();
        userOutbox.setLogin("user");
        userOutbox.setPassword("password");
        userOutbox.setEmail("user@example.com");
        userOutbox.setFullname("User Name");

        RegistrationRequest request = userMapper.toRegistrationRequest(userOutbox);

        assertThat(request.getLogin()).isEqualTo("user");
        assertThat(request.getPassword()).isEqualTo("password");
        assertThat(request.getEmail()).isEqualTo("user@example.com");
        assertThat(request.getFullname()).isEqualTo("User Name");
    }
}
