package org.skb.registration.controller;

import lombok.RequiredArgsConstructor;
import org.skb.registration.dto.RegistrationRequest;
import org.skb.registration.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@Validated
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationRequest request) {
        registrationService.registerUser(request);
        return new ResponseEntity<>("Registration request submitted! Please check your email", HttpStatus.OK);
    }
}
