package finki.ukim.backend.service.auth_and_access;

import finki.ukim.backend.auth_and_access.model.domain.Address;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.repository.UserProfileRepository;
import finki.ukim.backend.auth_and_access.service.domain.impl.UserProfileServiceImpl;
import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.model.exception.FileErrorException;
import finki.ukim.backend.file_handling.service.domain.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    private UserProfile existingProfile;
    private User user;

    private Address address(String street, String city, String postalCode) {
        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        address.setPostalCode(postalCode);
        return address;
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("alice");

        existingProfile = new UserProfile();
        existingProfile.setUserId(10L);
        existingProfile.setUser(user);
        existingProfile.setName("Alice");
        existingProfile.setSurname("Wonderland");
        existingProfile.setAddress(address("Wonderland Ave", "Skopje", "1000"));
        existingProfile.setPhoneNumber("123456789");
    }

    @Test
    void findByUserId_shouldReturnProfile_whenFound() {
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(existingProfile));
        Optional<UserProfile> result = userProfileService.findByUserId(1L);
        assertThat(result).isPresent().contains(existingProfile);
    }

    @Test
    void findByUsername_shouldReturnProfile_whenFound() {
        when(userProfileRepository.findByUser_Username("alice")).thenReturn(Optional.of(existingProfile));
        Optional<UserProfile> result = userProfileService.findByUsername("alice");
        assertThat(result).isPresent().contains(existingProfile);
    }

    @Test
    void create_shouldSaveAndReturnProfile() {
        when(userProfileRepository.save(existingProfile)).thenReturn(existingProfile);
        UserProfile result = userProfileService.create(existingProfile);
        assertThat(result).isEqualTo(existingProfile);
        verify(userProfileRepository).save(existingProfile);
    }

    @Test
    void update_shouldUpdateFieldsAndSave_whenFound() {
        UserProfile updatedData = new UserProfile();
        updatedData.setName("Alice New");
        updatedData.setSurname("Wonderland New");
        updatedData.setAddress(address("New Address", "Tetovo", "1200"));
        updatedData.setDateOfBirth(LocalDate.of(1990, 1, 1));
        updatedData.setPhoneNumber("987654321");

        when(userProfileRepository.findById(10L)).thenReturn(Optional.of(existingProfile));
        when(userProfileRepository.save(existingProfile)).thenReturn(existingProfile);

        Optional<UserProfile> result = userProfileService.update(10L, updatedData);

        assertThat(result).isPresent();
        UserProfile saved = result.get();
        assertThat(saved.getName()).isEqualTo("Alice New");
        assertThat(saved.getSurname()).isEqualTo("Wonderland New");
        assertThat(saved.getAddress()).isNotNull();
        assertThat(saved.getAddress().getStreet()).isEqualTo("New Address");
        assertThat(saved.getAddress().getCity()).isEqualTo("Tetovo");
        assertThat(saved.getAddress().getPostalCode()).isEqualTo("1200");
        assertThat(saved.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(saved.getPhoneNumber()).isEqualTo("987654321");

        verify(userProfileRepository).save(existingProfile);
    }

    @Test
    void update_shouldReturnEmpty_whenNotFound() {
        when(userProfileRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserProfile> result = userProfileService.update(99L, new UserProfile());

        assertThat(result).isEmpty();
        verify(userProfileRepository, never()).save(any());
    }

    @Test
    void updateProfilePicture_shouldReturnEmpty_whenProfileNotFound() {
        when(userProfileRepository.findByUserId(99L)).thenReturn(Optional.empty());

        Optional<UserProfile> result = userProfileService.updateProfilePicture(99L, mock(MultipartFile.class));

        assertThat(result).isEmpty();
        verify(fileService, never()).create(any());
        verify(fileService, never()).update(any(), any());
    }

    @Test
    void updateProfilePicture_shouldCreateNewFile_whenProfileHasNoPicture() {
        MultipartFile mockFile = mock(MultipartFile.class);
        File savedFile = new File();
        savedFile.setId(123L);

        existingProfile.setProfilePicture(null);

        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(existingProfile));
        when(fileService.create(mockFile)).thenReturn(savedFile);
        when(userProfileRepository.save(existingProfile)).thenReturn(existingProfile);

        Optional<UserProfile> result = userProfileService.updateProfilePicture(1L, mockFile);

        assertThat(result).isPresent();
        assertThat(result.get().getProfilePicture()).isEqualTo(savedFile);
        verify(fileService).create(mockFile);
        verify(fileService, never()).update(any(), any());
        verify(userProfileRepository).save(existingProfile);
    }

    @Test
    void updateProfilePicture_shouldUpdateExistingFile_whenProfileHasPicture() {
        MultipartFile mockFile = mock(MultipartFile.class);
        File oldFile = new File();
        oldFile.setId(123L);
        existingProfile.setProfilePicture(oldFile);

        File newFile = new File();
        newFile.setId(456L);

        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(existingProfile));
        when(fileService.update(123L, mockFile)).thenReturn(Optional.of(newFile));
        when(userProfileRepository.save(existingProfile)).thenReturn(existingProfile);

        Optional<UserProfile> result = userProfileService.updateProfilePicture(1L, mockFile);

        assertThat(result).isPresent();
        assertThat(result.get().getProfilePicture()).isEqualTo(newFile);
        verify(fileService).update(123L, mockFile);
        verify(fileService, never()).create(any());
        verify(userProfileRepository).save(existingProfile);
    }

    @Test
    void updateProfilePicture_shouldThrowException_whenUpdateFails() {
        MultipartFile mockFile = mock(MultipartFile.class);
        File oldFile = new File();
        oldFile.setId(123L);
        existingProfile.setProfilePicture(oldFile);

        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(existingProfile));
        when(fileService.update(123L, mockFile)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProfileService.updateProfilePicture(1L, mockFile))
                .isInstanceOf(FileErrorException.class)
                .hasMessageContaining("Could not update profile picture for file id 123");

        verify(userProfileRepository, never()).save(any());
    }
}
