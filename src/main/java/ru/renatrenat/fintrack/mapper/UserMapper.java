package ru.renatrenat.fintrack.mapper;

import org.mapstruct.*;
import ru.renatrenat.fintrack.dto.UserAdminDto;
import ru.renatrenat.fintrack.dto.UserCreateDto;
import ru.renatrenat.fintrack.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserAdminDto userAdminDto);

    UserAdminDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserAdminDto userAdminDto, @MappingTarget User user);

    User updateWithNull(UserAdminDto userAdminDto, @MappingTarget User user);

    User toEntity(UserCreateDto userCreateDto);

    UserCreateDto toDto1(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserCreateDto userCreateDto, @MappingTarget User user);
}