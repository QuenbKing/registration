package org.skb.registration.mapper;

import org.skb.registration.dto.RegistrationRequest;
import org.skb.registration.entity.User;
import org.skb.registration.entity.UserOutbox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    @Value("${custom.eventTypes.userRegistered}")
    private String userRegisteredEventType;

    @Value("${custom.statuses.pending}")
    private String pendingStatus;

    public User toUser(RegistrationRequest request) {
        User user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setFullname(request.getFullname());
        return user;
    }

    public UserOutbox toUserOutbox(RegistrationRequest request, Long userId) {
        UserOutbox userOutbox = new UserOutbox();
        userOutbox.setEventType(userRegisteredEventType);
        userOutbox.setUserId(userId);
        userOutbox.setLogin(request.getLogin());
        userOutbox.setPassword(request.getPassword());
        userOutbox.setEmail(request.getEmail());
        userOutbox.setFullname(request.getFullname());
        userOutbox.setStatus(pendingStatus);
        return userOutbox;
    }

    public RegistrationRequest toRegistrationRequest(UserOutbox userOutbox) {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setLogin(userOutbox.getLogin());
        registrationRequest.setPassword(userOutbox.getPassword());
        registrationRequest.setEmail(userOutbox.getEmail());
        registrationRequest.setFullname(userOutbox.getFullname());
        return registrationRequest;
    }
}
