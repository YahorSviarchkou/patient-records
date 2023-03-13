package com.patient.records.service.mapper;

import com.patient.records.entity.Position;
import com.patient.records.openapi.dto.PositionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface PositionMapper extends AbstractMapper<Position, PositionDto> {

    @Mapping(target = "doctorDetails", ignore = true)
    @Override
    Position toEntity(PositionDto positionDto);

    @Override
    PositionDto toDto(Position entity);
}
