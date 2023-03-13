package com.patient.records.service.mapper;

import com.patient.records.entity.AbstractEntity;

public interface AbstractMapper<Entity extends AbstractEntity, Dto> {

    Entity toEntity(Dto dto);

    Dto toDto(Entity entity);
}
