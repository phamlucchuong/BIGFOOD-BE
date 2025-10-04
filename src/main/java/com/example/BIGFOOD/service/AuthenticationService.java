package com.example.BIGFOOD.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.BIGFOOD.dto.request.AuthenticationRequest;
import com.example.BIGFOOD.dto.request.IntrospectRequest;
import com.example.BIGFOOD.dto.request.LogoutRequest;
import com.example.BIGFOOD.dto.response.AuthenticationRespone;
import com.example.BIGFOOD.dto.response.IntrospectRespone;
import com.example.BIGFOOD.entity.InvalidatedToken;
import com.example.BIGFOOD.entity.User;
import com.example.BIGFOOD.enums.ErrorCode;
import com.example.BIGFOOD.exception.AppException;
import com.example.BIGFOOD.repository.InvalidatedTokenRepository;
import com.example.BIGFOOD.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {
        @Autowired
        UserRepository accountRepository;
        @Autowired
        InvalidatedTokenRepository invalidatedTokenRepository;
     

        @NonFinal
        @Value("${jwt.signerKey}")
        protected String SIGN_KEY  ;
    
        public AuthenticationRespone authenticated (AuthenticationRequest request){
          PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            var account = accountRepository.findById(request.getUsername())
                            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FIND));
       
            boolean authencated = passwordEncoder.matches(request.getPassword(), account.getPassword() );
            if(!authencated){
                throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
            }
            String token = generatedToken(account);
            return AuthenticationRespone.builder()
                    .token(token)
                    .Authencationed(authencated)
                    .build();
        }

        public IntrospectRespone introspect (IntrospectRequest request)  throws JOSEException , ParseException  {
              var token = request.getToken();
              boolean isValid = true;
              try {
                 verifySignedJWT(token);
              } catch (Exception e) {
                  isValid = false;
              }
              return IntrospectRespone.builder()
                    .authenticated(isValid)
                    .build();

        }

         public void logout (LogoutRequest request) throws JOSEException, ParseException  {
             var signToken = verifySignedJWT(request.getToken());
             var jwtID = signToken.getJWTClaimsSet().getJWTID();
             var expriryDate = signToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken  invalidatedToken = new InvalidatedToken().builder()
                            .id(jwtID)
                            .expiryTime(expriryDate)
                            .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }

        public SignedJWT verifySignedJWT(String token) throws JOSEException , ParseException {
             JWSVerifier jwsVerifier = new MACVerifier(SIGN_KEY);
              SignedJWT signedJWT = SignedJWT.parse(token);
              Date expriryDate = signedJWT.getJWTClaimsSet().getExpirationTime();
              var verified = signedJWT.verify(jwsVerifier);
              if(!verified && expriryDate.after(new Date())){
                throw new AppException(ErrorCode.UNAUTHENTICATED);
              }
              if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
                throw new AppException(ErrorCode.UNAUTHENTICATED);
              }
              return signedJWT;
        }

        private String generatedToken(User user){
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet jwtClaimsSet   = new JWTClaimsSet.Builder()
                    .issuer(user.getName())
                    .issueTime(new Date())
                    .expirationTime(new Date(
                        Instant.now().plus(1 , ChronoUnit.HOURS).toEpochMilli()
                    ))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", buildScope(user) )
                    .build(); 
            Payload payload = new Payload(jwtClaimsSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            try {
                jwsObject.sign(new MACSigner(SIGN_KEY));
            } catch (Exception e) {
                log.debug("Không thể tạo token với lỗi = " + e.getMessage());
                throw new RuntimeException(e);
            }
            return jwsObject.serialize();
        }

        private String buildScope(User account){
            StringJoiner jStringJoiner = new StringJoiner(" ");
            if(!CollectionUtils.isEmpty(account.getRoles())){
                account.getRoles().forEach(role -> jStringJoiner.add(role.getName()));
            }
            return jStringJoiner.toString();
        }
    
}
