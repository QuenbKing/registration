package org.skb.registration.repository;

import org.skb.registration.entity.UserOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOutboxRepository extends JpaRepository<UserOutbox, Long> {
    List<UserOutbox> findByStatus(String status);
}
