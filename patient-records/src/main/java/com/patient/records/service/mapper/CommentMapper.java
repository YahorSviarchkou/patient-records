package com.patient.records.service.mapper;

import com.patient.records.entity.Comment;
import com.patient.records.entity.DoctorDetails;
import com.patient.records.openapi.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DoctorDetailsMapper.class})
public interface CommentMapper extends AbstractMapper<Comment, CommentDto> {

    @Mapping(target = "patient", ignore = true)
    @Override
    Comment toEntity(CommentDto commentDto);

    @Override
    CommentDto toDto(Comment entity);
}
