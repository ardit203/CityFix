package finki.ukim.backend.auth_and_access.service.domain.impl;

import finki.ukim.backend.auth_and_access.helper.PasswordChecker;
import finki.ukim.backend.auth_and_access.model.domain.Address;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.exception.*;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User register(User user) {
        findByUsername(user.getUsername()).orElseThrow(() -> new UsernameAlreadyExistsException(user.getUsername()));
        findByEmail(user.getEmail()).orElseThrow(() -> new EmailAlreadyExistsException(user.getEmail()));
        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new InvalidUserCredentialsException();
        return user;
    }

    @Override
    @Transactional
    public Optional<User> update(String username, User user) {
        return findByUsername(username).map((existingUser) -> {
            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());

            findByEmail(user.getEmail()).orElseThrow(() -> new EmailAlreadyExistsException(user.getEmail()));
            existingUser.setEmail(user.getEmail());

            findByUsername(user.getUsername()).orElseThrow(() -> new UsernameAlreadyExistsException(user.getUsername()));
            existingUser.setUsername(user.getUsername());

            existingUser.setAddress(user.getAddress());
            return userRepository.save(existingUser);
        });
    }

    @Override
    public Optional<User> changePassword(String username, String currentPassword, String newPassword, String confirmNewPassword) {
        return findByUsername(username).map((user) -> {
            if (currentPassword == null || currentPassword.isEmpty() || newPassword == null || newPassword.isEmpty() || confirmNewPassword == null || confirmNewPassword.isEmpty()) {
                throw new PasswordCannotBeEmptyException();
            }

            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new InvalidCurrentPasswordException();
            }

            if (!newPassword.equals(confirmNewPassword)) {
                throw new NewPasswordsDoNotMatchException();
            }

            if (passwordEncoder.matches(newPassword, user.getPassword())) {
                throw new NewPasswordCannotBeSameAsOldException();
            }
            PasswordChecker.checkPassword(newPassword);

            user.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(user);
        });
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
