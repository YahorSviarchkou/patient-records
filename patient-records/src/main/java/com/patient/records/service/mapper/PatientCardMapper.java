package com.patient.records.service.mapper;

import com.patient.records.entity.PatientCard;
import com.patient.records.openapi.dto.PatientCardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface PatientCardMapper extends AbstractMapper<PatientCard, PatientCardDto> {

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Override
    PatientCard toEntity(PatientCardDto patientCardDto);

    @Mapping(target = "age", ignore = true)
    @Override
    PatientCardDto toDto(PatientCard patientCard);
}
