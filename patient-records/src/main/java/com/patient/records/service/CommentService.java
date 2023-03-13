package com.patient.records.service;

import com.patient.records.entity.Comment;
import com.patient.records.repository.CommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {

    CommentRepository commentRepository;
    DoctorDetailsService doctorDetailsService;

    public List<Comment> getComments(@NotNull Long patientId, Long userId) {
        var comments = commentRepository.findAllByPatientId(patientId);

        if(Objects.nonNull(userId)) {
            comments = comments.stream()
                    .filter(comment -> comment.getDoctorDetails().getUser().getId().equals(userId))
                    .toList();
        }

        return comments;
    }

    public Comment createComment(@NotNull Comment comment) {
        if (Objects.nonNull(comment.getId())) {
            throw new IllegalArgumentException("Comment id must be null on creation");
        }
        if (Objects.isNull(comment.getPatient())) {
            throw new IllegalArgumentException("Patient must not be null on creation");
        }
        if (Objects.isNull(comment.getDoctorDetails())) {
            throw new IllegalArgumentException("Doctor must not be null on creation");
        }

        var doctor = doctorDetailsService.getDoctorDetailsById(comment.getDoctorDetails().getId());

        comment.setDoctorDetails(doctor);

        return commentRepository.save(comment);
    }
}
