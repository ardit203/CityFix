package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.Address;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;

public record RegisterUserRequestDto(
        String username,
        String password,
        String email,
        Role role,
        String name,
        String surname,
        String city,
        String street,
        String postalCode
) {
    public User toUser() {
        return new User(username, password, email, role, name, surname, new Address(street, city, postalCode));
    }
}
