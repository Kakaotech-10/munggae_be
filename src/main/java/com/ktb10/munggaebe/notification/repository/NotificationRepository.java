package com.ktb10.munggaebe.notification.repository;

import com.ktb10.munggaebe.notification.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByMemberId(Pageable pageable, Long memberId);

    List<Notification> findByMemberIdAndIdGreaterThan(Long userId, Long lastEventId, Sort sort);

    @Query("SELECT MAX(n.id) FROM Notification n")
    Long findLastId();
}
