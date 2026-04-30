package finki.ukim.backend.notification.repository;

import finki.ukim.backend.notification.model.domain.Notification;
import finki.ukim.backend.notification.model.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStatus(NotificationStatus status);
}
