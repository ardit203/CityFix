package finki.ukim.backend.repository.administration;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.repository.DepartmentRepository;
import finki.ukim.backend.administration.repository.MunicipalityRepository;
import finki.ukim.backend.administration.repository.StaffRepository;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import finki.ukim.backend.common.config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StaffRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("cityfix_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired private StaffRepository      staffRepository;
    @Autowired private UserRepository       userRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private MunicipalityRepository municipalityRepository;
    @Autowired private EntityManager        entityManager;
    @Autowired private EntityManagerFactory entityManagerFactory;

    private User       user1;
    private User       user2;
    private Department dept1;
    private Department dept2;
    private Municipality mun1;
    private Municipality mun2;
    private Staff      staff1;
    private Staff      staff2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User("emp1", "pw1", "emp1@example.com", Role.ROLE_EMPLOYEE));
        user2 = userRepository.save(new User("emp2", "pw2", "emp2@example.com", Role.ROLE_MANAGER));

        dept1 = departmentRepository.save(new Department("Roads", "Road dept"));
        dept2 = departmentRepository.save(new Department("Parks", "Parks dept"));

        mun1 = municipalityRepository.save(new Municipality("Skopje", "SKP"));
        mun2 = municipalityRepository.save(new Municipality("Bitola", "BIT"));

        staff1 = staffRepository.save(new Staff(user1, dept1, mun1));
        staff2 = staffRepository.save(new Staff(user2, dept2, mun2));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findAllWithAll_shouldReturnAllStaffWithJoinedRelations() {
        List<Staff> results = staffRepository.findAllWithAll();
        PersistenceUnitUtil util = entityManagerFactory.getPersistenceUnitUtil();

        assertThat(results).hasSize(2);
        assertThat(results).allSatisfy(s -> {
            assertThat(util.isLoaded(s, "user")).isTrue();
            assertThat(util.isLoaded(s, "department")).isTrue();
            assertThat(util.isLoaded(s, "municipality")).isTrue();
        });
    }

    @Test
    void findStaffById_shouldReturnStaffWithRelations_whenIdExists() {
        Optional<Staff> result = staffRepository.findStaffById(staff1.getId());
        PersistenceUnitUtil util = entityManagerFactory.getPersistenceUnitUtil();

        assertThat(result).isPresent();
        assertThat(util.isLoaded(result.get(), "user")).isTrue();
        assertThat(result.get().getUser().getUsername()).isEqualTo("emp1");
        assertThat(result.get().getDepartment().getName()).isEqualTo("Roads");
        assertThat(result.get().getMunicipality().getName()).isEqualTo("Skopje");
    }

    @Test
    void findStaffById_shouldReturnEmpty_whenIdDoesNotExist() {
        Optional<Staff> result = staffRepository.findStaffById(9999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findByUsername_shouldReturnStaff_whenUsernameExists() {
        Optional<Staff> result = staffRepository.findByUsername("emp1");

        assertThat(result).isPresent();
        assertThat(result.get().getUser().getUsername()).isEqualTo("emp1");
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameDoesNotExist() {
        Optional<Staff> result = staffRepository.findByUsername("nobody");

        assertThat(result).isEmpty();
    }

    @Test
    void findByDepartmentId_shouldReturnOnlyMatchingStaff() {
        List<Staff> results = staffRepository.findByDepartmentId(dept1.getId());

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getUser().getUsername()).isEqualTo("emp1");
    }

    @Test
    void findByMunicipalityId_shouldReturnOnlyMatchingStaff() {
        List<Staff> results = staffRepository.findByMunicipalityId(mun2.getId());

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getUser().getUsername()).isEqualTo("emp2");
    }

    @Test
    void find_shouldReturnStaff_whenAllThreeKeysMatch() {
        Optional<Staff> result = staffRepository.find("emp1", dept1.getId(), mun1.getId());
        PersistenceUnitUtil util = entityManagerFactory.getPersistenceUnitUtil();

        assertThat(result).isPresent();
        assertThat(util.isLoaded(result.get(), "user")).isTrue();
        assertThat(result.get().getUser().getUsername()).isEqualTo("emp1");
    }

    @Test
    void find_shouldReturnEmpty_whenDepartmentDoesNotMatch() {
        Optional<Staff> result = staffRepository.find("emp1", dept2.getId(), mun1.getId());

        assertThat(result).isEmpty();
    }
}
