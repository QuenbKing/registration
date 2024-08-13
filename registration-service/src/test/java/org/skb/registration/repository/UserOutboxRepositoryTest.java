package org.skb.registration.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skb.registration.entity.UserOutbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class UserOutboxRepositoryTest {

    @Autowired
    private UserOutboxRepository userOutboxRepository;

    @BeforeEach
    void setUp() {
        userOutboxRepository.deleteAll();
    }

    @Test
    void whenFindByStatus_thenReturnUserOutboxes() {
        UserOutbox userOutbox1 = new UserOutbox();
        userOutbox1.setStatus("PENDING");
        userOutboxRepository.save(userOutbox1);

        UserOutbox userOutbox2 = new UserOutbox();
        userOutbox2.setStatus("PENDING");
        userOutboxRepository.save(userOutbox2);

        UserOutbox userOutbox3 = new UserOutbox();
        userOutbox3.setStatus("PROCESSED");
        userOutboxRepository.save(userOutbox3);

        List<UserOutbox> pendingOutboxes = userOutboxRepository.findByStatus("PENDING");

        assertThat(pendingOutboxes).hasSize(2);
        assertThat(pendingOutboxes).extracting(UserOutbox::getStatus).containsOnly("PENDING");
    }

    @Test
    void whenFindByStatus_thenReturnEmptyListIfNoMatchingStatus() {
        UserOutbox userOutbox = new UserOutbox();
        userOutbox.setStatus("PROCESSED");
        userOutboxRepository.save(userOutbox);

        List<UserOutbox> pendingOutboxes = userOutboxRepository.findByStatus("PENDING");

        assertThat(pendingOutboxes).isEmpty();
    }

    @Test
    void whenSave_thenDataIsPersistedInDatabase() {
        UserOutbox userOutbox = new UserOutbox();
        userOutbox.setEventType("USER_REGISTERED");
        userOutbox.setUserId(1L);
        userOutbox.setLogin("testLogin1");
        userOutbox.setPassword("testPassword1");
        userOutbox.setEmail("test@example.com");
        userOutbox.setFullname("Test User");
        userOutbox.setStatus("PENDING");
        userOutbox.setCreatedAt(LocalDateTime.now());

        UserOutbox savedUserOutbox = userOutboxRepository.save(userOutbox);

        assertThat(savedUserOutbox).isNotNull();
        assertThat(savedUserOutbox.getId()).isNotNull();
        assertThat(savedUserOutbox.getEventType()).isEqualTo("USER_REGISTERED");
        assertThat(savedUserOutbox.getUserId()).isEqualTo(1L);
        assertThat(savedUserOutbox.getLogin()).isEqualTo("testLogin1");
        assertThat(savedUserOutbox.getPassword()).isEqualTo("testPassword1");
        assertThat(savedUserOutbox.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUserOutbox.getFullname()).isEqualTo("Test User");
        assertThat(savedUserOutbox.getStatus()).isEqualTo("PENDING");
    }
}
