package org.skb.verification.dto;

public record RegistrationRequest(String login, String password, String email, String fullname) {
}
