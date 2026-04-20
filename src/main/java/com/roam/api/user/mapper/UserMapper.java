package com.roam.api.user.mapper;

import com.roam.api.user.dto.CurrentUserResponse;
import com.roam.api.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    CurrentUserResponse toResponse(User user);
}
