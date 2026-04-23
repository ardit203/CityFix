package finki.ukim.backend.repository.administration;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.repository.CategoryRepository;
import finki.ukim.backend.administration.repository.DepartmentRepository;
import finki.ukim.backend.common.config.JpaConfig;
import jakarta.persistence.EntityManager;
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

import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {

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

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Department dept1;
    private Department dept2;
    private Category cat1;
    private Category cat2;
    private Category cat3;

    @BeforeEach
    void setUp() {
        dept1 = departmentRepository.save(new Department("Roads", "Road maintenance department"));
        dept2 = departmentRepository.save(new Department("Parks", "Parks and recreation department"));

        cat1 = categoryRepository.save(new Category("Pothole",    "Potholes on the road", dept1));
        cat2 = categoryRepository.save(new Category("Signage",    "Missing road signs",   dept1));
        cat3 = categoryRepository.save(new Category("Overgrowth", "Overgrown vegetation", dept2));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByName_shouldReturnCategory_whenNameExists() {
        Optional<Category> result = categoryRepository.findByName("Pothole");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Pothole");
    }

    @Test
    void findByName_shouldReturnEmpty_whenNameDoesNotExist() {
        Optional<Category> result = categoryRepository.findByName("Nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    void findByIdWithDepartment_shouldReturnCategoryWithDepartmentEagerlyLoaded() {
        Optional<Category> result = categoryRepository.findByIdWithDepartment(cat1.getId());

        PersistenceUnitUtil util = entityManagerFactory.getPersistenceUnitUtil();

        assertThat(result).isPresent();
        assertThat(util.isLoaded(result.get(), "department")).isTrue();
        assertThat(result.get().getDepartment().getName()).isEqualTo("Roads");
    }

    @Test
    void findByIdWithDepartment_shouldReturnEmpty_whenIdDoesNotExist() {
        Optional<Category> result = categoryRepository.findByIdWithDepartment(9999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAllWithDepartment_shouldReturnAllCategoriesWithDepartments() {
        List<Category> results = categoryRepository.findAllWithDepartment();

        PersistenceUnitUtil util = entityManagerFactory.getPersistenceUnitUtil();

        assertThat(results).hasSize(3);
        assertThat(results).allSatisfy(c -> assertThat(util.isLoaded(c, "department")).isTrue());
    }

    @Test
    void findByDepartmentId_shouldReturnOnlyMatchingCategories() {
        List<Category> results = categoryRepository.findByDepartmentId(dept1.getId());

        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("Pothole", "Signage");
    }

    @Test
    void findByDepartmentId_shouldReturnEmptyList_whenDepartmentHasNoCategories() {
        Department emptyDept = departmentRepository.save(new Department("Water", "Water department"));
        entityManager.flush();
        entityManager.clear();

        List<Category> results = categoryRepository.findByDepartmentId(emptyDept.getId());

        assertThat(results).isEmpty();
    }
}
