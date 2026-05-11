package finki.ukim.backend.notification.model.events;

import finki.ukim.backend.auth_and_access.model.domain.User;

public record RequestStatusChangedEvent(User citizen, Long requestId, String title, String newStatus) {
}
