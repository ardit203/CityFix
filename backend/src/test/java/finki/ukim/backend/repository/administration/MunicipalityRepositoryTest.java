package finki.ukim.backend.repository.administration;

import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.repository.MunicipalityRepository;
import finki.ukim.backend.common.config.JpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Testcontainers
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MunicipalityRepositoryTest {

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
    private MunicipalityRepository municipalityRepository;

    private Municipality skopje;
    private Municipality bitola;

    @BeforeEach
    void setUp() {
        skopje  = municipalityRepository.save(new Municipality("Skopje",  "SKP"));
        bitola  = municipalityRepository.save(new Municipality("Bitola",  "BIT"));
    }

    @Test
    void findByName_shouldReturnMunicipality_whenNameExists() {
        Optional<Municipality> result = municipalityRepository.findByName("Skopje");

        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("SKP");
    }

    @Test
    void findByName_shouldReturnEmpty_whenNameDoesNotExist() {
        Optional<Municipality> result = municipalityRepository.findByName("Ohrid");

        assertThat(result).isEmpty();
    }

    @Test
    void findByCode_shouldReturnMunicipality_whenCodeExists() {
        Optional<Municipality> result = municipalityRepository.findByCode("BIT");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Bitola");
    }

    @Test
    void findByCode_shouldReturnEmpty_whenCodeDoesNotExist() {
        Optional<Municipality> result = municipalityRepository.findByCode("XXX");

        assertThat(result).isEmpty();
    }

    @Test
    void save_shouldThrowException_whenDuplicateNameIsInserted() {
        assertThatThrownBy(() -> {
            municipalityRepository.saveAndFlush(new Municipality("Skopje", "SKP2"));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_shouldThrowException_whenDuplicateCodeIsInserted() {
        assertThatThrownBy(() -> {
            municipalityRepository.saveAndFlush(new Municipality("NewCity", "SKP"));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
