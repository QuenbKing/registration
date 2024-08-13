package org.skb.registration.dto;

import lombok.Data;
import org.skb.registration.validation.EmailConstraint;
import org.skb.registration.validation.FullNameConstraint;
import org.skb.registration.validation.LoginConstraint;
import org.skb.registration.validation.PasswordConstraint;

@Data
public class RegistrationRequest {

    @LoginConstraint
    private String login;

    @PasswordConstraint
    private String password;

    @EmailConstraint
    private String email;

    @FullNameConstraint
    private String fullname;
}
