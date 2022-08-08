package com.livecast.userservice.mapper;

import com.livecast.userservice.dto.UserDTO;
import com.livecast.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    public UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    @Mappings({
            @Mapping(source = "id", target = "id", ignore = true),
            @Mapping(source = "type", target = "type", ignore = true),
    })
    User toEntity(UserDTO dto);

    @Mappings({
            @Mapping(source = "id", target = "id", ignore = true),
            @Mapping(source = "type", target = "type", ignore = true),
            @Mapping(source = "username", target = "username", ignore = true),
            @Mapping(source = "email", target = "email", ignore = true),
    })
    void update(@MappingTarget User old, UserDTO request);
}
