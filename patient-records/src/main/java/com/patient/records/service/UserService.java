package com.patient.records.service;

import com.patient.records.entity.User;
import com.patient.records.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    RoleService roleService;
    PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        var users = userRepository.findAllByDeletedOrderById(Boolean.FALSE);
        return users.isEmpty()
                ? Collections.emptyList()
                : users;
    }

    public User getUserById(@NotNull Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User getUserByLogin(@NotBlank String login) {
        return userRepository.findByLogin(login.toLowerCase(Locale.ROOT)).orElseThrow(() ->
                new NoSuchElementException("user not found with login=" + login));
    }

    public Boolean userAlreadyExists(String login) {
        return userRepository.existsByLogin(login);
    }

    public User createUser(@NotNull User user) {
        if (Objects.nonNull(user.getId())) {
            throw new IllegalArgumentException("user id must be null on creation");
        }

        user.setLogin(user.getLogin());
        user.setRole(roleService.getRoleByTitle(user.getRole().getTitle().name()));
        user.setDeleted(Boolean.FALSE);

        return userRepository.save(user);
    }

    public User updateUser(@NotNull User user) {
        var userDb = getUserById(user.getId());

        userDb.setLogin(user.getLogin());
        userDb.setPassword(user.getPassword());

        return userRepository.save(userDb);
    }

    public Boolean changeUserPassword(Long userId, @NotBlank String oldPassword, @NotBlank String newPassword) {
        var userDb = getUserById(userId);

        var isTruePassword = passwordEncoder.matches(oldPassword, userDb.getPassword());

        if(isTruePassword) {
            userDb.setPassword(passwordEncoder.encode(newPassword));
            updateUser(userDb);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void softDeleteUser(@NotNull Long id) {
        userRepository.setDeletedInUser(id, Boolean.TRUE);
    }
}
