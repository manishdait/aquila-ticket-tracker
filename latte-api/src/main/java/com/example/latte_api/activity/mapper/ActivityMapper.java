package com.example.latte_api.activity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import com.example.latte_api.activity.Activity;
import com.example.latte_api.activity.dto.ActivityDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityMapper {
  ActivityMapper INSTANT = Mappers.getMapper(ActivityMapper.class);

  @Mapping(target = "author", source = "author.firstname")
  ActivityDto mapToActivityDto(Activity activity);
}
