package com.example.bigfood.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.AuthenticationRequest;
import com.example.bigfood.dto.request.IntrospectRequest;
import com.example.bigfood.dto.request.LogoutRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.AuthenticationRespone;
import com.example.bigfood.dto.response.IntrospectRespone;
import com.example.bigfood.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;


@RestController
@RequestMapping("api/auth")

public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService ;

    @PostMapping
    public ApiResponse<AuthenticationRespone> authenticated(@RequestBody AuthenticationRequest request){
           ApiResponse<AuthenticationRespone> apiResponse = new ApiResponse<>();
           apiResponse.setResults(authenticationService.authenticated(request));
         return apiResponse;
    }
    @PostMapping("/introspect")
    public ApiResponse<IntrospectRespone> authenticate(@RequestBody IntrospectRequest request) throws ParseException , JOSEException{
        return ApiResponse.<IntrospectRespone>builder()
        .results(authenticationService.introspect(request))
        .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException , JOSEException{
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
        .build();
    }


}
