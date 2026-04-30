package finki.ukim.backend.auth_and_access.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.projection.UserPageableProjection;
import finki.ukim.backend.auth_and_access.model.projection.UserWithIdUsernameAndEmail;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService extends UserDetailsService {
    User findById(Long id);

    User findByIdWithProfile(Long id);

    User findByIdWithProfileAndPic(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    List<UserWithIdUsernameAndEmail> findAll();

    Page<UserPageableProjection> findAll(int page,
                                         int size,
                                         String sortBy,
                                         Long id,
                                         String username,
                                         String email,
                                         Role role);

    User adminUpdateUser(Long id, User user);

    User deleteById(Long id);

    User changeRole(Long id, Role role);

    User lock(Long id, LocalDateTime until);

    User unlock(Long id);

    User save(User user);

    ///
    ///
    ///
    ///
    User updateAccount(Long id, User user);

    User updateProfile(Long id, UserProfile userProfile);

    User updateProfilePicture(Long id, MultipartFile file);

    User deleteProfilePicture(Long id);

    User changePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword);
}
