package com.example.bigfood.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.example.bigfood.entity.User;
import com.example.bigfood.entity.Role;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.mapper.UserMapper;
import com.example.bigfood.repository.UserRepository;
import com.example.bigfood.repository.RoleRepository;
import com.example.bigfood.dto.request.UserCreateRequest;
import com.example.bigfood.dto.request.UserUpdateRequest;
import com.example.bigfood.dto.response.UserResponse;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreateRequest request) {
         User user = userMapper.toUser(request);
         user.setPassword(passwordEncoder.encode(request.getPassword()));
         HashSet<Role> roles = new HashSet<>();
         roleRepository.findById("USER").ifPresent(roles::add);
         user.setRoles(roles);
         return userMapper.toUserResponse(userRepository.save(user));
    }

    public Boolean verifyEmail(String emailRequest){
         return userRepository.existsByEmail(emailRequest);
    }

    public UserResponse updateUser(String email , UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FIND));
            userMapper.toUpdate(user, request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUser() {
        List<UserResponse> users = userRepository.findAll().stream()
                                        .map(userMapper::toUserResponse).toList();
         return  users;
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }
}
