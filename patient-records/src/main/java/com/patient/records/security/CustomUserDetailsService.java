package com.patient.records.security;

import com.patient.records.entity.Role;
import com.patient.records.repository.UserRepository;
import com.patient.records.service.RoleService;
import com.patient.records.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${app.default.user.login}")
    String defaultLogin;
    @Value("${app.default.user.password}")
    String defaultPassword;
    @Value("${app.default.user.role}")
    String defaultRole;

    final UserRepository userRepository;
    final UserService userService;
    final RoleService roleService;
    final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new User(user.getLogin(), user.getPassword(), mapRolesToAuthorities(List.of(user.getRole())));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getTitle().name())).collect(Collectors.toList());
    }

    @PostConstruct
    private void createDefaultUser() {
        if(userService.userAlreadyExists(defaultLogin)) {
            var userDb = userService.getUserByLogin(defaultLogin);
            userDb.setPassword(passwordEncoder.encode(defaultPassword));
            userDb.setRole(roleService.getRoleByTitle(defaultRole));
            userService.updateUser(userDb);
            return;
        }
        var user = new com.patient.records.entity.User();
        user.setLogin(defaultLogin);
        user.setPassword(passwordEncoder.encode(defaultPassword));

        Role role = roleService.getRoleByTitle(defaultRole);
        user.setRole(role);

        userService.createUser(user);
    }
}
