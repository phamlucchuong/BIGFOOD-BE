package com.example.BIGFOOD.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.BIGFOOD.constant.PredefinedRole;
import com.example.BIGFOOD.entity.Role;
import com.example.BIGFOOD.entity.User;
import com.example.BIGFOOD.repository.RoleRepository;
import com.example.BIGFOOD.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME ="admin";
    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository , RoleRepository roleRepository){
        return args ->{
            if(userRepository.findById("ADMIN").isEmpty()){

                roleRepository.save(Role.builder()
                .name(PredefinedRole.USER_ROLE)
                .description("User role")
                .build());

             Role adminRole = roleRepository.save(Role.builder()
                .name(PredefinedRole.ADMIN_ROLE)
                .description("Admin role")
                .build());
            
                var roles = new HashSet<Role>();
                roles.add(adminRole);
            User user = User.builder()
            .name(ADMIN_USER_NAME)
            .password(passwordEncoder.encode(ADMIN_PASSWORD))
            .roles(roles)
            .build();

            userRepository.save(user);
            log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }
    
}
