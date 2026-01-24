package com.example.bigfood.service;


import com.example.bigfood.repository.ReviewRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.rmi.server.LogStream.log;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LowRatingAlertService {
    ReviewRepository reviewRepository;
    OtpService otpService;

    public void sendReportToLowRatingRestaurant() {
        List<String> emails = reviewRepository.getLowRatingEmail();
        for(String email : emails) {
            otpService.sendEmail(email);
            log("send email to " + email);
        }
    }

}
