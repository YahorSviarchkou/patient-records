package com.patient.records.service;

import com.patient.records.entity.DoctorDetails;
import com.patient.records.entity.Position;
import com.patient.records.entity.User;
import com.patient.records.openapi.dto.DoctorDetailsRequestDto;
import com.patient.records.repository.DoctorDetailsRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorDetailsService {

    DoctorDetailsRepository doctorDetailsRepository;
    UserService userService;
    RoleService roleService;
    PositionService positionService;
    PasswordEncoder passwordEncoder;

    public List<DoctorDetails> getAllDoctorsDetails() {
        var doctorDetailsList = doctorDetailsRepository.findAllByDeleted(Boolean.FALSE);
        return doctorDetailsList.isEmpty()
                ? Collections.emptyList()
                : doctorDetailsList;
    }

    public DoctorDetails getDoctorDetailsById(@NotNull Long id) {
        return doctorDetailsRepository.findById(id).orElseThrow();
    }

    public DoctorDetails getDoctorDetailsByUserId(@NotNull Long id) {
        return doctorDetailsRepository.findByUserId(id).orElseThrow();
    }

    @Transactional
    public DoctorDetails createDoctorDetails(@NotNull DoctorDetails doctorDetails) {
        if (Objects.nonNull(doctorDetails.getId())) {
            throw new IllegalArgumentException("Doctor details id must be null on creation");
        }
        if (Objects.isNull(doctorDetails.getUser())) {
            throw new IllegalArgumentException("Doctor details user must not be null on creation");
        }

        var user = userService.createUser(doctorDetails.getUser());

        doctorDetails.setId(user.getId());
        doctorDetails.setUser(user);
        doctorDetails.setPosition(positionService.deliverPosition(doctorDetails.getPosition()));

        return doctorDetailsRepository.save(doctorDetails);
    }

    @Transactional
    public DoctorDetails createDoctorDetails(@NotNull DoctorDetailsRequestDto doctorDetailsRequest) {
        if (!doctorDetailsRequest.getLogin().equalsIgnoreCase("admin")
                &&userService.userAlreadyExists(doctorDetailsRequest.getLogin())) {
            throw new IllegalArgumentException("User already exists");
        }

        var position = new Position();
        position.setTitle(doctorDetailsRequest.getPosition());

        var user = new User();
        user.setLogin(doctorDetailsRequest.getLogin());
        user.setPassword(passwordEncoder.encode((doctorDetailsRequest.getPassword())));
        user.setRole(roleService.getRoleByTitle(doctorDetailsRequest.getRoleTitle()));

        var doctorDetails = new DoctorDetails();
        doctorDetails.setFio(doctorDetailsRequest.getFio());
        doctorDetails.setPosition(position);
        doctorDetails.setUser(user);

        return createDoctorDetails(doctorDetails);
    }

    @Transactional
    public DoctorDetails updateDoctorDetails(@NotNull DoctorDetails doctorDetails) {
        if (doctorDetails.getFio().isBlank()) {
            throw new IllegalArgumentException("FIO must not be blank");
        }
        if (Objects.isNull(doctorDetails.getPosition())) {
            throw new IllegalArgumentException("Position must not be blank");
        }

        var doctorDetailsDb = getDoctorDetailsById(doctorDetails.getId());
        doctorDetailsDb.setFio(doctorDetails.getFio());
        doctorDetailsDb.setPosition(positionService.deliverPosition(doctorDetails.getPosition()));
        return doctorDetailsRepository.save(doctorDetailsDb);
    }
}
