package com.patient.records.controller;

import com.patient.records.entity.Role;
import com.patient.records.entity.User;
import com.patient.records.openapi.dto.UserDto;
import com.patient.records.security.JWTGenerator;
import com.patient.records.security.request.LoginRequest;
import com.patient.records.security.request.RegisterRequest;
import com.patient.records.security.response.AuthResponse;
import com.patient.records.service.RoleService;
import com.patient.records.service.UserService;
import com.patient.records.service.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "authorization", description = "authorization documentation")
public class AuthController {

    AuthenticationManager authenticationManager;
    UserService userService;
    UserMapper userMapper;
    RoleService roleService;
    PasswordEncoder passwordEncoder;
    JWTGenerator jwtGenerator;

    @Operation(
            operationId = "login",
            summary = "login user",
            tags = {"authorization"}
    )
    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        if(userService.getUserByLogin(loginRequest.login()).getDeleted().equals(Boolean.TRUE)) {
            throw new UsernameNotFoundException("User was deleted");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.login(),
                        loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    @Operation(
            operationId = "register",
            summary = "Register new user",
            tags = {"authorization"}
    )
    @PostMapping("register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Long> register(@RequestBody RegisterRequest registerRequest) {
        if (!registerRequest.login().equalsIgnoreCase("admin")
                &&userService.userAlreadyExists(registerRequest.login())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setLogin(registerRequest.login());
        user.setPassword(passwordEncoder.encode((registerRequest.password())));

        Role role = roleService.getRoleByTitle(registerRequest.roleTitle());
        user.setRole(role);

        var userDb = userService.createUser(user);

        return new ResponseEntity<>(userDb.getId(), HttpStatus.OK);
    }

    @Operation(
            operationId = "getUserByToken",
            summary = "get user by token",
            tags = {"authorization"}
    )
    @GetMapping("user")
    public ResponseEntity<UserDto> getUserByToken(@RequestParam String accessToken) {
        if(StringUtils.hasText(accessToken) && jwtGenerator.validateToken(accessToken)) {
            String username = jwtGenerator.getUsernameFromJWT(accessToken);
            return ResponseEntity.ok(userMapper.toDto(userService.getUserByLogin(username)));
        }
        throw new UsernameNotFoundException("User not found");
    }
}