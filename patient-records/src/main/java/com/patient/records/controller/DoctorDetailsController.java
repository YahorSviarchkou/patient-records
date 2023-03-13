package com.patient.records.controller;

import com.patient.records.openapi.api.DoctorDetailsApi;
import com.patient.records.openapi.dto.DoctorDetailsDto;
import com.patient.records.openapi.dto.DoctorDetailsRequestDto;
import com.patient.records.service.DoctorDetailsService;
import com.patient.records.service.mapper.DoctorDetailsMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorDetailsController implements DoctorDetailsApi {

    DoctorDetailsService doctorDetailsService;
    DoctorDetailsMapper doctorDetailsMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ResponseEntity<DoctorDetailsDto> createDoctorDetails(DoctorDetailsRequestDto doctorDetailsDto) {
        return ResponseEntity.ok(doctorDetailsMapper.toDto(doctorDetailsService.createDoctorDetails(doctorDetailsDto)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ResponseEntity<List<DoctorDetailsDto>> getAllDoctorsDetails() {
        var doctorDetailsList = doctorDetailsService.getAllDoctorsDetails().stream()
                .map(doctorDetailsMapper::toDto)
                .toList();
        return ResponseEntity.ok(doctorDetailsList);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ResponseEntity<DoctorDetailsDto> getDoctorDetailsById(Long doctorId) {
        return ResponseEntity.ok(doctorDetailsMapper.toDto(doctorDetailsService.getDoctorDetailsById(doctorId)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ResponseEntity<DoctorDetailsDto> updateDoctorDetails(DoctorDetailsDto doctorDetailsDto) {
        var doctorDetails = doctorDetailsMapper.toEntity(doctorDetailsDto);
        return ResponseEntity.ok(doctorDetailsMapper.toDto(doctorDetailsService.updateDoctorDetails(doctorDetails)));
    }
}
