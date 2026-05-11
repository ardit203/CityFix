package finki.ukim.backend.common.config;

import finki.ukim.backend.auth_and_access.model.domain.Address;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Profile("h2")
@Component
@AllArgsConstructor
public class Initializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        createUser(
                "ardit",
                "ardit",
                "arditselmani203@gmail.com",
                Role.ROLE_ADMINISTRATOR,
                "Ardit",
                "Selmani"
        );

        createUser(
                "admin",
                "admin",
                "admin@cityfix.com",
                Role.ROLE_ADMINISTRATOR,
                "Admin",
                "Admin"
        );

        createUser(
                "manager",
                "manager",
                "manager@cityfix.com",
                Role.ROLE_MANAGER,
                "Manager",
                "Manager"
        );

        createUser(
                "employee",
                "employee",
                "employee@cityfix.com",
                Role.ROLE_EMPLOYEE,
                "Employee",
                "Employee"
        );

        createUser(
                "citizen",
                "citizen",
                "citizen@cityfix.com",
                Role.ROLE_CITIZEN,
                "Citizen",
                "Citizen"
        );
    }

    private void createUser(
            String username,
            String rawPassword,
            String email,
            Role role,
            String name,
            String surname
    ) {
        if (userRepository.existsByUsername(username)) {
            return;
        }

        User user = new User(
                username,
                passwordEncoder.encode(rawPassword),
                email,
                role
        );

        Address address = new Address(
                "Test Street",
                "Kumanovo",
                "1300"
        );

        UserProfile profile = new UserProfile(
                name,
                surname,
                address,
                LocalDate.of(2000, 1, 1),
                Gender.OTHER,
                "070000000"
        );

        user.setProfile(profile);

        userRepository.save(user);
    }
}