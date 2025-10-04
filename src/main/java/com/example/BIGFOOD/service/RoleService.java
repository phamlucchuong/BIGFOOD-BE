package com.example.BIGFOOD.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BIGFOOD.dto.request.RoleCreateRequest;
import com.example.BIGFOOD.dto.request.RoleUpdateRequest;
import com.example.BIGFOOD.dto.response.RoleResponse;
import com.example.BIGFOOD.entity.Role;
import com.example.BIGFOOD.enums.ErrorCode;
import com.example.BIGFOOD.exception.AppException;
import com.example.BIGFOOD.mapper.RoleMapper;
import com.example.BIGFOOD.repository.PermissionRepository;
import com.example.BIGFOOD.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired 
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleMapper roleMapper;

    public RoleResponse createRole(RoleCreateRequest request){
        if(roleRepository.existsById(request.getName())){
            throw  new AppException(ErrorCode.Role_EXITED);
        }
        Role role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermission());

        role.setPermission(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAllRole(){
        List<RoleResponse> listRole = roleRepository.findAll()
            .stream().map(roleMapper :: toRoleResponse).toList();
        return listRole;
    }

    public RoleResponse updateRole(String name , RoleUpdateRequest request){
        var role = roleRepository.findById(name).orElseThrow(()-> new AppException(ErrorCode.Role_NOT_FIND));
        roleMapper.toUpdate(role, request);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }
    
    public void deleteRole(String name){
        roleRepository.deleteById(name);
    }
}
