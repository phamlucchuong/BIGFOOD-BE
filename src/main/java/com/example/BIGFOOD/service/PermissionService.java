package com.example.BIGFOOD.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BIGFOOD.dto.request.PermissionCreateRequest;
import com.example.BIGFOOD.dto.response.PermissionResponse;
import com.example.BIGFOOD.enums.ErrorCode;
import com.example.BIGFOOD.exception.AppException;
import com.example.BIGFOOD.mapper.PermissionMapper;
import com.example.BIGFOOD.repository.PermissionRepository;

@Service
public class PermissionService {
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionCreateRequest request){
        if(permissionRepository.existsById(request.getName())){
           throw new AppException(ErrorCode.PERMISSION_EXITED);
        }
        var permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }
   
    public List<PermissionResponse> getAllPermissionResponses(){
        List<PermissionResponse> listPResponses = permissionRepository
                                .findAll()
                                .stream()
                                .map(permissionMapper::toPermissionResponse)
                                .toList();  
        return listPResponses;
    }

    public void deletePermission(String name){
         permissionRepository.deleteById(name);
    }



    
}
