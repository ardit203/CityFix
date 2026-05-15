package finki.ukim.backend.common.config;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.repository.DepartmentRepository;
import finki.ukim.backend.administration.repository.MunicipalityRepository;
import finki.ukim.backend.administration.repository.StaffRepository;
import finki.ukim.backend.auth_and_access.model.domain.Address;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@Profile("dev")
@AllArgsConstructor
public class InitializerDev {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final DepartmentRepository departmentRepository;
    private final MunicipalityRepository municipalityRepository;

    @PostConstruct
    public void init() {
        log.info("========== DOCKER USER INITIALIZER STARTED ==========");

        createUser("ardit", "ardit", "arditselmani203@gmail.com", Role.ROLE_ADMINISTRATOR, "Ardit", "Selmani");
        createUser("admin", "admin", "admin@cityfix.com", Role.ROLE_ADMINISTRATOR, "Admin", "Admin");
        createUser("manager", "manager", "manager@cityfix.com", Role.ROLE_MANAGER, "Manager", "Manager");
        createUser("employee", "employee", "employee@cityfix.com", Role.ROLE_EMPLOYEE, "Employee", "Employee");
        createUser("citizen", "citizen", "citizen@cityfix.com", Role.ROLE_CITIZEN, "Citizen", "Citizen");

        log.info("========== DOCKER USER INITIALIZER FINISHED ==========");
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
            User existingUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User exists but could not be found: " + username));

            log.info("User already exists: username={}, role={}", username, existingUser.getRole());

            if (existingUser.getRole() == Role.ROLE_MANAGER || existingUser.getRole() == Role.ROLE_EMPLOYEE) {
                ensureStaff(existingUser);
            }

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

        User savedUser = userRepository.save(user);

        log.info("Created user: id={}, username={}, role={}",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole()
        );

        if (savedUser.getRole() == Role.ROLE_MANAGER || savedUser.getRole() == Role.ROLE_EMPLOYEE) {
            ensureStaff(savedUser);
        }
    }

    private void ensureStaff(User user) {
        Department department = departmentRepository.findByName("Waste Management")
                .orElseThrow(() -> new RuntimeException("Department not found: Waste Management"));

        Municipality municipality = municipalityRepository.findByCode("SKP")
                .orElseThrow(() -> new RuntimeException("Municipality not found: SKP"));

        Staff staff = staffRepository.findByUserId(user.getId())
                .orElse(null);

        if (staff == null) {
            staff = new Staff(user, department, municipality);
            log.info("Creating staff row for username={}", user.getUsername());
        } else {
            staff.setUser(user);
            staff.setDepartment(department);
            staff.setMunicipality(municipality);
            log.info("Updating existing staff row for username={}", user.getUsername());
        }

        Staff savedStaff = staffRepository.save(staff);

        log.info("Saved staff row: staffId={}, userId={}, username={}, department={}, municipality={}",
                savedStaff.getId(),
                user.getId(),
                user.getUsername(),
                department.getName(),
                municipality.getCode()
        );
    }
}