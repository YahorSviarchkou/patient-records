package com.patient.records.service.mapper;

import com.patient.records.entity.DoctorDetails;
import com.patient.records.openapi.dto.DoctorDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, PositionMapper.class})
public interface DoctorDetailsMapper extends AbstractMapper<DoctorDetails, DoctorDetailsDto> {

    @Mapping(target = "comments", ignore = true)
    @Override
    DoctorDetails toEntity(DoctorDetailsDto doctorDetailsDto);

    @Override
    DoctorDetailsDto toDto(DoctorDetails doctorDetails);
}
