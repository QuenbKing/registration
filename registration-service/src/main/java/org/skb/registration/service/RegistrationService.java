package org.skb.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.skb.registration.dto.RegistrationRequest;
import org.skb.registration.entity.User;
import org.skb.registration.entity.UserOutbox;
import org.skb.registration.errors.RegistrationException;
import org.skb.registration.mapper.UserMapper;
import org.skb.registration.repository.UserOutboxRepository;
import org.skb.registration.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final UserOutboxRepository userOutboxRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public void registerUser(RegistrationRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RegistrationException("A user with such an email has already been registered");
        } else if (userRepository.existsByLogin(userRequest.getLogin())){
            throw new RegistrationException("A user with such an email has already been registered");
        }

        String encodePassword = passwordEncoder.encode(userRequest.getPassword());
        userRequest.setPassword(encodePassword);

        User user = userMapper.toUser(userRequest);
        userRepository.save(user);

        UserOutbox userOutbox = userMapper.toUserOutbox(userRequest, user.getId());
        userOutboxRepository.save(userOutbox);
    }
}
