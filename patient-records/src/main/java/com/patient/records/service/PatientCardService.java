package com.patient.records.service;

import com.patient.records.entity.Comment;
import com.patient.records.entity.PatientCard;
import com.patient.records.openapi.dto.PatientCardDto;
import com.patient.records.repository.PatientCardRepository;
import com.patient.records.service.image.ImageService;
import com.patient.records.service.mapper.PatientCardMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientCardService {

    PatientCardRepository patientCardRepository;
    DoctorDetailsService doctorDetailsService;
    CommentService commentService;
    PatientCardMapper mapper;

    public List<PatientCard> getAllPatientCards(String FIO, String minCreationDate, String maxCreationDate,
                                                String minBirthdayDate, String maxBirthdayDate) {
        List<PatientCard> patientCardsList = patientCardRepository.findAllByDeletedOrderById(Boolean.FALSE);

        if (Objects.nonNull(FIO)) {
            patientCardsList = patientCardsList.stream()
                    .filter(patient -> patient.getFio().toLowerCase(Locale.ROOT).contains(FIO.toLowerCase(Locale.ROOT)))
                    .toList();
        }

        if (Objects.nonNull(minBirthdayDate) && !minBirthdayDate.equals("")) {
            patientCardsList = patientCardsList.stream()
                    .peek(patient -> System.out.println(patient.getDateOfBirthday().after(Timestamp.valueOf(minBirthdayDate))))
                    .filter(patient -> patient.getDateOfBirthday().after(Timestamp.valueOf(minBirthdayDate)))
                    .toList();
        }
        if (Objects.nonNull(maxBirthdayDate) && !maxBirthdayDate.equals("")) {
            patientCardsList = patientCardsList.stream()
                    .filter(patient -> patient.getDateOfBirthday().before(Timestamp.valueOf(maxBirthdayDate)))
                    .toList();
        }

        if (Objects.nonNull(minCreationDate) && !minCreationDate.equals("")) {
            patientCardsList = patientCardsList.stream()
                    .filter(patient -> patient.getCreated().after(Timestamp.valueOf(minCreationDate)))
                    .toList();
        }

        if (Objects.nonNull(maxCreationDate) && !maxCreationDate.equals("")) {
            patientCardsList = patientCardsList.stream()
                    .filter(patient -> patient.getCreated().before(Timestamp.valueOf(maxCreationDate)))
                    .toList();
        }

        return patientCardsList.isEmpty()
                ? Collections.emptyList()
                : patientCardsList;
    }

    public PatientCard getPatientCardById(@NotNull Long id) {
        return patientCardRepository.findById(id).orElseThrow();
    }

    public PatientCard createPatientCard(@NotNull PatientCard patientCard) {
        if (Objects.nonNull(patientCard.getId())) {
            throw new IllegalArgumentException("Patient card id must be null on creation");
        }

        patientCard.setDeleted(Boolean.FALSE);

        return patientCardRepository.save(patientCard);
    }

    public PatientCard updatePatientCard(@NotNull PatientCard patientCard) {
        if (patientCard.getFio().isBlank()) {
            throw new IllegalArgumentException("FIO must not be blank");
        }
        if(Objects.isNull(patientCard.getDateOfBirthday())) {
            throw new IllegalArgumentException("Birthday must not be null");
        }

        var patientCardDb = getPatientCardById(patientCard.getId());
        patientCardDb.setFio(patientCard.getFio());
        patientCardDb.setDateOfBirthday(patientCard.getDateOfBirthday());
        return patientCardRepository.save(patientCardDb);
    }

    @Transactional
    public PatientCard writeComment(@NotNull Comment comment, @NotNull Long patientId, @NotNull Long userId) {
        var patientCard = getPatientCardById(patientId);
        var doctor = doctorDetailsService.getDoctorDetailsByUserId(userId);

        comment.setDoctorDetails(doctor);
        comment.setPatient(patientCard);
        patientCard.getComments().add(commentService.createComment(comment));

        return getPatientCardById(patientId);
    }

    public void setPhoto(@NotNull byte[] photo, @NotNull Long patientId) {
        patientCardRepository.setPhoto(photo, patientId);
    }

    public void softDelete(@NotNull Long id) {
        if(!patientCardRepository.existsById(id)) {
            throw new NoSuchElementException("Patient not found with id=" + id);
        }
        patientCardRepository.setDeletedInPatientCard(id, Boolean.TRUE);
        ImageService.deleteFile(id);
    }

    public Integer getAgeFromDate(@NotNull Timestamp birthday) {
        var currentDate = LocalDateTime.now();
        return Period.between(birthday.toLocalDateTime().toLocalDate(), currentDate.toLocalDate()).getYears();
    }

    public PatientCardDto convertToDto(PatientCard patientCard) {
        var dto = mapper.toDto(patientCard);
        dto.setAge(getAgeFromDate(patientCard.getDateOfBirthday()));
        return dto;
    }
}
