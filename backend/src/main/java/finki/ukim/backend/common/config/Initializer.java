package finki.ukim.backend.common.config;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.repository.CategoryRepository;
import finki.ukim.backend.administration.repository.DepartmentRepository;
import finki.ukim.backend.administration.repository.MunicipalityRepository;
import finki.ukim.backend.administration.repository.StaffRepository;
import finki.ukim.backend.auth_and_access.model.domain.Address;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.domain.RequestLocation;
import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.enums.RoutingStatus;
import finki.ukim.backend.request_management.repository.RequestRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Profile("h2")
@Component
@AllArgsConstructor
public class Initializer {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final DepartmentRepository departmentRepository;
    private final MunicipalityRepository municipalityRepository;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        log.info("========== H2 INITIALIZER STARTED ==========");

        logImportantSeedData();

        createUser("ardit", "ardit", "arditselmani203@gmail.com", Role.ROLE_ADMINISTRATOR, "Ardit", "Selmani");
        createUser("admin", "admin", "admin@cityfix.com", Role.ROLE_ADMINISTRATOR, "Admin", "Admin");
        createUser("manager", "manager", "manager@cityfix.com", Role.ROLE_MANAGER, "Manager", "Manager");
        createUser("employee", "employee", "employee@cityfix.com", Role.ROLE_EMPLOYEE, "Employee", "Employee");
        createUser("citizen", "citizen", "citizen@cityfix.com", Role.ROLE_CITIZEN, "Citizen", "Citizen");

        createRequest();

        logFinalCheck();

        log.info("========== H2 INITIALIZER FINISHED ==========");
    }

    private void logImportantSeedData() {
        log.info("---- Checking seed data ----");

        Department wasteDepartment = departmentRepository.findByName("Waste Management")
                .orElseThrow(() -> new RuntimeException("Department not found: Waste Management"));

        Municipality skopje = municipalityRepository.findByCode("SKP")
                .orElseThrow(() -> new RuntimeException("Municipality not found: SKP"));

        Category illegalDumping = categoryRepository.findByName("Illegal Dumping")
                .orElseThrow(() -> new RuntimeException("Category not found: Illegal Dumping"));

        log.info("Waste Management department: id={}, name={}", wasteDepartment.getId(), wasteDepartment.getName());
        log.info("SKP municipality: id={}, name={}, code={}", skopje.getId(), skopje.getName(), skopje.getCode());
        log.info("Illegal Dumping category: id={}, name={}", illegalDumping.getId(), illegalDumping.getName());

        log.info("---- Finished checking seed data ----");
    }

    private void createUser(
            String username,
            String rawPassword,
            String email,
            Role role,
            String name,
            String surname
    ) {
        log.info("Creating user: username={}, role={}", username, role);

        if (userRepository.existsByUsername(username)) {
            User existingUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User exists but could not be found: " + username));

            log.warn(
                    "User already exists: id={}, username={}, role={}. Skipping user creation.",
                    existingUser.getId(),
                    existingUser.getUsername(),
                    existingUser.getRole()
            );

            if (existingUser.getRole() == Role.ROLE_MANAGER || existingUser.getRole() == Role.ROLE_EMPLOYEE) {
                log.info("Existing user is staff role. Checking staff row for username={}", username);
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

        log.info(
                "Saved user: id={}, username={}, role={}",
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

        log.info(
                "Preparing staff for userId={}, username={}, departmentId={}, departmentName={}, municipalityId={}, municipalityCode={}",
                user.getId(),
                user.getUsername(),
                department.getId(),
                department.getName(),
                municipality.getId(),
                municipality.getCode()
        );

        Staff staff = staffRepository.findByUserId(user.getId())
                .orElse(null);

        if (staff == null) {
            staff = new Staff(user, department, municipality);
            log.info("No existing staff row found. Creating new staff row.");
        } else {
            log.warn(
                    "Existing staff row found before update: staffId={}, userId={}, departmentId={}, municipalityId={}",
                    staff.getId(),
                    staff.getUser() != null ? staff.getUser().getId() : null,
                    staff.getDepartment() != null ? staff.getDepartment().getId() : null,
                    staff.getMunicipality() != null ? staff.getMunicipality().getId() : null
            );

            staff.setUser(user);
            staff.setDepartment(department);
            staff.setMunicipality(municipality);
        }

        Staff savedStaff = staffRepository.save(staff);

        log.info(
                "Saved staff row: staffId={}, userId={}, username={}, departmentId={}, departmentName={}, municipalityId={}, municipalityCode={}",
                savedStaff.getId(),
                savedStaff.getUser() != null ? savedStaff.getUser().getId() : null,
                savedStaff.getUser() != null ? savedStaff.getUser().getUsername() : null,
                savedStaff.getDepartment() != null ? savedStaff.getDepartment().getId() : null,
                savedStaff.getDepartment() != null ? savedStaff.getDepartment().getName() : null,
                savedStaff.getMunicipality() != null ? savedStaff.getMunicipality().getId() : null,
                savedStaff.getMunicipality() != null ? savedStaff.getMunicipality().getCode() : null
        );
    }

    private void createRequest() {
        log.info("Creating test request...");

        Category category = categoryRepository.findByName("Illegal Dumping")
                .orElseThrow(() -> new RuntimeException("Category not found: Illegal Dumping"));

        Department department = departmentRepository.findByName("Waste Management")
                .orElseThrow(() -> new RuntimeException("Department not found: Waste Management"));

        Municipality municipality = municipalityRepository.findByCode("SKP")
                .orElseThrow(() -> new RuntimeException("Municipality not found: SKP"));

        User citizen = userRepository.findByUsername("citizen")
                .orElseThrow(() -> new RuntimeException("Citizen user not found"));

        log.info(
                "Request will use: citizenId={}, departmentId={}, departmentName={}, municipalityId={}, municipalityCode={}, categoryId={}, categoryName={}",
                citizen.getId(),
                department.getId(),
                department.getName(),
                municipality.getId(),
                municipality.getCode(),
                category.getId(),
                category.getName()
        );

        Request request = new Request(
                "Garbage not collected",
                "Garbage has not been collected since 2n April.",
                new RequestLocation(new BigDecimal("41.9962164"), new BigDecimal("21.4318935")),
                "Multiple black garbage bags are visibly piled on the sidewalk. The garbage has reportedly not been collected since April 2nd, leading to a significant accumulation of waste. This prolonged presence of uncollected refuse creates an unsanitary condition and makes the public area unsafe.",
                Priority.HIGH,
                RequestStatus.SUBMITTED,
                RoutingStatus.CONFIRMED,
                citizen,
                municipality,
                category,
                department
        );

        Request savedRequest = requestRepository.save(request);

        log.info(
                "Saved request: requestId={}, title={}, citizenId={}, departmentId={}, departmentName={}, municipalityId={}, municipalityCode={}, categoryId={}, categoryName={}",
                savedRequest.getId(),
                savedRequest.getTitle(),
                savedRequest.getUser() != null ? savedRequest.getUser().getId() : null,
                savedRequest.getDepartment() != null ? savedRequest.getDepartment().getId() : null,
                savedRequest.getDepartment() != null ? savedRequest.getDepartment().getName() : null,
                savedRequest.getMunicipality() != null ? savedRequest.getMunicipality().getId() : null,
                savedRequest.getMunicipality() != null ? savedRequest.getMunicipality().getCode() : null,
                savedRequest.getCategory() != null ? savedRequest.getCategory().getId() : null,
                savedRequest.getCategory() != null ? savedRequest.getCategory().getName() : null
        );
    }

    private void logFinalCheck() {
        log.info("---- FINAL CHECK: manager staff vs request ----");

        User manager = userRepository.findByUsername("manager")
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        Staff managerStaff = staffRepository.findByUserId(manager.getId())
                .orElseThrow(() -> new RuntimeException("Staff row not found for manager"));

        Request request = requestRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Request with id=1 not found"));

        Long managerDepartmentId = managerStaff.getDepartment() != null
                ? managerStaff.getDepartment().getId()
                : null;

        Long managerMunicipalityId = managerStaff.getMunicipality() != null
                ? managerStaff.getMunicipality().getId()
                : null;

        Long requestDepartmentId = request.getDepartment() != null
                ? request.getDepartment().getId()
                : null;

        Long requestMunicipalityId = request.getMunicipality() != null
                ? request.getMunicipality().getId()
                : null;

        log.info(
                "Manager staff: userId={}, departmentId={}, municipalityId={}",
                manager.getId(),
                managerDepartmentId,
                managerMunicipalityId
        );

        log.info(
                "Request: requestId={}, departmentId={}, municipalityId={}",
                request.getId(),
                requestDepartmentId,
                requestMunicipalityId
        );

        log.info("Same department? {}", managerDepartmentId != null && managerDepartmentId.equals(requestDepartmentId));
        log.info("Same municipality? {}", managerMunicipalityId != null && managerMunicipalityId.equals(requestMunicipalityId));

        log.info("---- END FINAL CHECK ----");
    }
}