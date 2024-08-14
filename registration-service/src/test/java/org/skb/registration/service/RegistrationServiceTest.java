package org.skb.registration.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skb.registration.dto.RegistrationRequest;
import org.skb.registration.entity.User;
import org.skb.registration.entity.UserOutbox;
import org.skb.registration.mapper.UserMapper;
import org.skb.registration.repository.UserOutboxRepository;
import org.skb.registration.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserOutboxRepository userOutboxRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void registerUser_shouldSaveUserAndUserOutbox() {
        RegistrationRequest request = new RegistrationRequest();
        request.setLogin("user1");
        request.setPassword("password1");
        request.setEmail("user@example.com");
        request.setFullname("User Name");

        User user = new User();
        user.setId(1L);
        user.setLogin("user1");
        user.setPassword("password1");
        user.setEmail("user@example.com");
        user.setFullname("User Name");

        UserOutbox userOutbox = new UserOutbox();
        userOutbox.setId(1L);
        userOutbox.setUserId(user.getId());
        userOutbox.setLogin("user1");
        userOutbox.setPassword("password1");
        userOutbox.setEmail("user@example.com");
        userOutbox.setFullname("User Name");


        when(passwordEncoder.encode("password1")).thenReturn("encodedPassword");
        when(userMapper.toUser(request)).thenReturn(user);
        when(userMapper.toUserOutbox(request, user.getId())).thenReturn(userOutbox);
        when(userRepository.save(user)).thenReturn(user);
        when(userOutboxRepository.save(userOutbox)).thenReturn(userOutbox);

        registrationService.registerUser(request);

        verify(passwordEncoder).encode("password1");
        verify(userRepository).save(user);
        verify(userOutboxRepository).save(userOutbox);
    }
}
