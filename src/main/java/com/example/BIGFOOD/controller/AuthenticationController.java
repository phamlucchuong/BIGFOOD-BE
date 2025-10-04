package com.example.BIGFOOD.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BIGFOOD.dto.request.AuthenticationRequest;
import com.example.BIGFOOD.dto.request.IntrospectRequest;
import com.example.BIGFOOD.dto.request.LogoutRequest;
import com.example.BIGFOOD.dto.response.ApiResponse;
import com.example.BIGFOOD.dto.response.AuthenticationRespone;
import com.example.BIGFOOD.dto.response.IntrospectRespone;
import com.example.BIGFOOD.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;


@RestController
@RequestMapping("/auth")

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
