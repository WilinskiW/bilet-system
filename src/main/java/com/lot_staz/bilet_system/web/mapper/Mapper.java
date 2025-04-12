package com.lot_staz.bilet_system.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface Mapper<TEntity, TDto> {
    TDto entityToDto(TEntity entity);

    default List<TDto> entityListToDtoList(List<TEntity> entityList){
        return entityList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    TEntity dtoToEntity(TDto dto);
}
