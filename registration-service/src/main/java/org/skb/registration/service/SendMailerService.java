package org.skb.registration.service;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.skb.registration.dto.RegistrationResponse;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Log4j2
public class SendMailerService {

    public void sendMail(RegistrationResponse registrationResponse) throws TimeoutException {
        if(shouldThrowTimeout()) {
            sleep();

            throw new TimeoutException("Timeout!");
        }

        if(shouldSleep()) {
            sleep();
        }

        log.info("Message sent to {}, status: {}.", registrationResponse.email(), registrationResponse.status());
    }

    @SneakyThrows
    private static void sleep() {
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }

    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldThrowTimeout() {
        return new Random().nextInt(10) == 1;
    }
}
