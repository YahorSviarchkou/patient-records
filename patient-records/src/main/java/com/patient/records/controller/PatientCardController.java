package com.patient.records.controller;

import com.patient.records.openapi.api.PatientCardsApi;
import com.patient.records.openapi.dto.CommentDto;
import com.patient.records.openapi.dto.PatientCardDto;
import com.patient.records.service.PatientCardService;
import com.patient.records.service.mapper.CommentMapper;
import com.patient.records.service.mapper.PatientCardMapper;
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
public class PatientCardController implements PatientCardsApi {

    PatientCardService patientCardService;
    PatientCardMapper patientCardMapper;
    CommentMapper commentMapper;

    @PreAuthorize("hasAnyAuthority('CHIEF_PHYSICIAN', 'PHYSICIAN')")
    @Override
    public ResponseEntity<PatientCardDto> addCommentToPatientCard(Long patientId, Long userId, CommentDto commentDto) {
        var comment = commentMapper.toEntity(commentDto);
        return ResponseEntity.ok(patientCardMapper.toDto(patientCardService.writeComment(comment, patientId, userId)));
    }

    @PreAuthorize("hasAuthority('LOCAL_ADMIN')")
    @Override
    public ResponseEntity<PatientCardDto> createPatientCard(PatientCardDto patientCardDto) {
        var patientCard = patientCardMapper.toEntity(patientCardDto);
        return ResponseEntity.ok(patientCardMapper.toDto(patientCardService.createPatientCard(patientCard)));
    }

    @PreAuthorize("hasAnyAuthority('LOCAL_ADMIN', 'CHIEF_PHYSICIAN', 'PHYSICIAN')")
    @Override
    public ResponseEntity<List<PatientCardDto>> getAllPatientCards(
            String FIO, String minBirthday, String maxBirthday, String minCreation, String maxCreation) {
        var patientCardsListDto = patientCardService
                .getAllPatientCards(FIO, minCreation, maxCreation, minBirthday, maxBirthday).stream()
                .map(patientCardService::convertToDto)
                .toList();
        return ResponseEntity.ok(patientCardsListDto);
    }

    @PreAuthorize("hasAnyAuthority('LOCAL_ADMIN', 'CHIEF_PHYSICIAN', 'PHYSICIAN')")
    @Override
    public ResponseEntity<PatientCardDto> getPatientCardById(Long patientId) {
        return ResponseEntity.ok(patientCardService.convertToDto(patientCardService.getPatientCardById(patientId)));
    }

    @PreAuthorize("hasAuthority('LOCAL_ADMIN')")
    @Override
    public ResponseEntity<Void> softDeleteById(Long patientId) {
        patientCardService.softDelete(patientId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('LOCAL_ADMIN')")
    @Override
    public ResponseEntity<PatientCardDto> updatePatientCard(PatientCardDto patientCardDto) {
        var patientCard = patientCardMapper.toEntity(patientCardDto);
        return ResponseEntity.ok(patientCardService.convertToDto(patientCardService.updatePatientCard(patientCard)));
    }
}
