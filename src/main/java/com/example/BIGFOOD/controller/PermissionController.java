package com.example.bigfood.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.PermissionCreateRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.PermissionResponse;
import com.example.bigfood.service.PermissionService;

@RestController
@RequestMapping("api/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionCreateRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .results(permissionService.createPermission(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermission(){
        ApiResponse<List<PermissionResponse> >apiResponse = new ApiResponse<>();
        apiResponse.setResults(permissionService.getAllPermissionResponses());
        return apiResponse;
    }

    @DeleteMapping("/{name}")
    public ApiResponse<Void> deletePermission(@PathVariable String name){
        permissionService.deletePermission(name);
        return ApiResponse.<Void>builder().build();
    }
    
}
