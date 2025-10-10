package com.example.bigfood.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import com.example.bigfood.entity.Role;
import com.example.bigfood.entity.User;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.repository.RoleRepository;
import com.example.bigfood.repository.UserRepository;

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
    static final String ADMIN_EAMIL = "hoa1312004@gmail.com";
    @NonFinal
    static final String ADMIN_PASSWORD = "123@#@";

    @Bean
    @Transactional
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByEmail(ADMIN_EAMIL).isEmpty()) {
                var roles = new HashSet<Role>();
                Role role = roleRepository.findByName("ADMIN").orElseThrow(() -> new AppException(ErrorCode.Role_NOT_FIND));
                roles.add(role);
                
                User user = User.builder()
                        .email(ADMIN_EAMIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .name("admin")
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }

}
