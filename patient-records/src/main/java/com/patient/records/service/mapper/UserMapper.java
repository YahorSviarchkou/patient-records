package com.patient.records.service.mapper;

import com.patient.records.entity.User;
import com.patient.records.openapi.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper extends AbstractMapper<User, UserDto> {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "doctorDetails", ignore = true)
    @Override
    User toEntity(UserDto dto);

    @Override
    UserDto toDto(User entity);
}
