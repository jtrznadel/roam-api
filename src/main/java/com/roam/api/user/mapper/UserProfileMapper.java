package com.roam.api.user.mapper;

import com.roam.api.user.dto.CurrentUserProfileResponse;
import com.roam.api.user.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    CurrentUserProfileResponse toResponse(UserProfile userProfile);
}
