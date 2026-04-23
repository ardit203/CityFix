package finki.ukim.backend.service.administration;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.exception.CitizenCannotBeStaffException;
import finki.ukim.backend.administration.repository.StaffRepository;
import finki.ukim.backend.administration.service.domain.impl.StaffServiceImpl;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    private StaffServiceImpl staffService;

    private User employee;
    private User citizen;
    private Department dept;
    private Municipality mun;
    private Staff existingStaff;

    @BeforeEach
    void setUp() {
        staffService  = new StaffServiceImpl(staffRepository);
        employee      = new User("emp1", "pw", "emp1@example.com", Role.ROLE_EMPLOYEE);
        citizen       = new User("cit1", "pw", "cit1@example.com", Role.ROLE_CITIZEN);
        dept          = new Department("Roads", "Road dept");
        mun           = new Municipality("Skopje", "SKP");
        existingStaff = new Staff(employee, dept, mun);
    }

    // ── create ────────────────────────────────────────────────────────────

    @Test
    void create_shouldSaveAndReturnStaff_whenUserIsNotCitizen() {
        when(staffRepository.save(existingStaff)).thenReturn(existingStaff);

        Staff result = staffService.create(existingStaff);

        assertThat(result).isEqualTo(existingStaff);
        verify(staffRepository).save(existingStaff);
    }

    @Test
    void create_shouldThrow_whenUserRoleIsCitizen() {
        Staff citizenStaff = new Staff(citizen, dept, mun);

        assertThatThrownBy(() -> staffService.create(citizenStaff))
                .isInstanceOf(CitizenCannotBeStaffException.class);
        verify(staffRepository, never()).save(any());
    }

    // ── update ────────────────────────────────────────────────────────────

    @Test
    void update_shouldUpdateAndReturnStaff_whenIdExistsAndUserIsNotCitizen() {
        User manager = new User("mgr1", "pw", "mgr1@example.com", Role.ROLE_MANAGER);
        Staff updatedStaff = new Staff(manager, dept, mun);

        when(staffRepository.findStaffById(1L)).thenReturn(Optional.of(existingStaff));
        when(staffRepository.save(any(Staff.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Staff> result = staffService.update(1L, updatedStaff);

        assertThat(result).isPresent();
        assertThat(result.get().getUser().getUsername()).isEqualTo("mgr1");
    }

    @Test
    void update_shouldReturnEmpty_whenIdDoesNotExist() {
        when(staffRepository.findStaffById(99L)).thenReturn(Optional.empty());

        Optional<Staff> result = staffService.update(99L, existingStaff);

        assertThat(result).isEmpty();
        verify(staffRepository, never()).save(any());
    }

    @Test
    void update_shouldThrow_whenUpdatedUserRoleIsCitizen() {
        Staff citizenStaff = new Staff(citizen, dept, mun);

        when(staffRepository.findStaffById(1L)).thenReturn(Optional.of(existingStaff));

        assertThatThrownBy(() -> staffService.update(1L, citizenStaff))
                .isInstanceOf(CitizenCannotBeStaffException.class);
        verify(staffRepository, never()).save(any());
    }

    // ── deleteById ────────────────────────────────────────────────────────

    @Test
    void deleteById_shouldReturnDeletedStaff_whenExists() {
        when(staffRepository.findStaffById(1L)).thenReturn(Optional.of(existingStaff));

        Optional<Staff> result = staffService.deleteById(1L);

        assertThat(result).isPresent();
        verify(staffRepository).delete(existingStaff);
    }

    @Test
    void deleteById_shouldReturnEmpty_whenStaffNotFound() {
        when(staffRepository.findStaffById(99L)).thenReturn(Optional.empty());

        Optional<Staff> result = staffService.deleteById(99L);

        assertThat(result).isEmpty();
        verify(staffRepository, never()).delete(any());
    }

    // ── findByDepartmentId / findByMunicipalityId ─────────────────────────

    @Test
    void findByDepartmentId_shouldReturnMatchingStaffList() {
        when(staffRepository.findByDepartmentId(1L)).thenReturn(List.of(existingStaff));

        List<Staff> result = staffService.findByDepartmentId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(existingStaff);
    }

    @Test
    void findByMunicipalityId_shouldReturnMatchingStaffList() {
        when(staffRepository.findByMunicipalityId(1L)).thenReturn(List.of(existingStaff));

        List<Staff> result = staffService.findByMunicipalityId(1L);

        assertThat(result).hasSize(1);
    }
}
