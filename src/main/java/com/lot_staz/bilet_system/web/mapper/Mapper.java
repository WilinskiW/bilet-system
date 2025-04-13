package com.lot_staz.bilet_system.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic interface for mapping between database entities (TEntity) and their corresponding Data Transfer Objects (TDto).
 * Supports conversion of single objects as well as lists.
 *
 * @param <TEntity> the type of the entity (JPA entity)
 * @param <TDto> the type of the DTO (Data Transfer Object)
 */
public interface Mapper<TEntity, TDto> {
    TDto entityToDto(TEntity entity);

    default List<TDto> entityListToDtoList(List<TEntity> entityList){
        return entityList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    TEntity dtoToEntity(TDto dto);
}
