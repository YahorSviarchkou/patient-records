package com.patient.records.controller;

import com.patient.records.entity.Role;
import com.patient.records.entity.User;
import com.patient.records.openapi.api.UsersApi;
import com.patient.records.openapi.dto.ChangeUserPasswordRequestDto;
import com.patient.records.openapi.dto.MessageDto;
import com.patient.records.openapi.dto.UserDto;
import com.patient.records.security.request.RegisterRequest;
import com.patient.records.service.RoleService;
import com.patient.records.service.UserService;
import com.patient.records.service.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController implements UsersApi {

    UserService userService;
    UserMapper mapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ResponseEntity<MessageDto> changeUserPassword(Long userId, ChangeUserPasswordRequestDto changeUserPasswordRequestDto) {
        var oldPassword = changeUserPasswordRequestDto.getOldPassword();
        var newPassword = changeUserPasswordRequestDto.getNewPassword();

        var changed = userService.changeUserPassword(userId, oldPassword, newPassword);

        var message = new MessageDto();
        if(changed) {
            message.setValue("Password changed successfully");
            return ResponseEntity.ok(message);
        }
        message.setValue("Incorrect password");
        return ResponseEntity.badRequest().body(message);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ResponseEntity<List<UserDto>> getUsersByFilter(Long id, String login) {
        if (Objects.nonNull(id)) {
            return ResponseEntity.ok(List.of(mapper.toDto(userService.getUserById(id))));
        } else if (Objects.nonNull(login)) {
            return ResponseEntity.ok(List.of(mapper.toDto(userService.getUserByLogin(login))));
        }
        var users = userService.getAllUsers().stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ResponseEntity<Void> softDeleteUserById(Long userId) {
        userService.softDeleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}