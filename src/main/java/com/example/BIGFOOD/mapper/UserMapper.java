package com.example.BIGFOOD.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.BIGFOOD.dto.request.UserCreateRequest;
import com.example.BIGFOOD.dto.request.UserUpdateRequest;
import com.example.BIGFOOD.dto.response.UserResponse;
import com.example.BIGFOOD.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);
    UserResponse toUserResponse(User user);
    
    @Mapping(target = "roles" , ignore = true)
    void toUpdate(@MappingTarget User user , UserUpdateRequest request);
}
