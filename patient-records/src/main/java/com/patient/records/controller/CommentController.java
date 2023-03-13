package com.patient.records.controller;

import com.patient.records.openapi.api.CommentsApi;
import com.patient.records.openapi.dto.CommentDto;
import com.patient.records.service.CommentService;
import com.patient.records.service.mapper.CommentMapper;
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
public class CommentController implements CommentsApi {

    CommentService commentService;
    CommentMapper commentMapper;

    @PreAuthorize("hasAuthority('CHIEF_PHYSICIAN')")
    @Override
    public ResponseEntity<List<CommentDto>> getCommentsByPatientId(Long patientId) {
        return ResponseEntity.ok(commentService.getComments(patientId, null).stream()
                .map(commentMapper::toDto)
                .toList());
    }

    @PreAuthorize("hasAnyAuthority('CHIEF_PHYSICIAN', 'PHYSICIAN')")
    @Override
    public ResponseEntity<List<CommentDto>> getCommentsByPatientIdAndDoctorId(Long patientId, Long userId) {
        return  ResponseEntity.ok(commentService.getComments(patientId, userId).stream()
                .map(commentMapper::toDto)
                .toList());
    }
}
