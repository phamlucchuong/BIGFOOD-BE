package com.example.BIGFOOD.configuration;

import java.text.ParseException;
import java.util.Objects;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.example.BIGFOOD.dto.request.IntrospectRequest;
import com.example.BIGFOOD.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;


@Component
public class CustomerJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerKey}")
    private String SIGN_KEY;

    @Autowired 
    AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;
    @Override
    public Jwt decode(String token){
        try {
            var reponse = authenticationService.introspect(
                IntrospectRequest.builder().token(token).build()
                );
            if(!reponse.isAuthenticated()) {
              throw new JwtException("Token invalid");
            }
        } catch (JOSEException | ParseException e) {
           throw new JwtException(e.getMessage());
        }

        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKey = new SecretKeySpec(SIGN_KEY.getBytes() , "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        }
        return nimbusJwtDecoder.decode(token);

    }
    
}
