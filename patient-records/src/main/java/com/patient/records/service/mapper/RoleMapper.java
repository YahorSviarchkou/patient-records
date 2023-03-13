package com.patient.records.service.mapper;

import com.patient.records.entity.Role;
import com.patient.records.openapi.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper extends AbstractMapper<Role, RoleDto> {

    @Mapping(target = "users", ignore = true)
    @Override
    Role toEntity(RoleDto dto);

    @Override
    RoleDto toDto(Role entity);
}
