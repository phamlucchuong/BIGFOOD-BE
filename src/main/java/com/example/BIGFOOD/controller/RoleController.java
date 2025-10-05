package com.example.BIGFOOD.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BIGFOOD.dto.request.RoleCreateRequest;
import com.example.BIGFOOD.dto.request.RoleUpdateRequest;
import com.example.BIGFOOD.dto.response.ApiResponse;
import com.example.BIGFOOD.dto.response.RoleResponse;
import com.example.BIGFOOD.service.RoleService;

@RestController
@RequestMapping("api/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleCreateRequest request){
            return ApiResponse.<RoleResponse>builder()
                .results(roleService.createRole(request)).build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRole(){
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResults(roleService.getAllRole());
        return apiResponse;
    }

    @PutMapping("/{name}")
    public ApiResponse<RoleResponse> updateRole(@PathVariable String name , @RequestBody RoleUpdateRequest request ){
        return ApiResponse.<RoleResponse>builder()
            .results(roleService.updateRole(name, request))
            .build();
    }

    @DeleteMapping("/{name}")
    public  ApiResponse<Void> deleteRole(@PathVariable String name){
        roleService.deleteRole(name);
        return ApiResponse.<Void>builder().build();
    }
}
