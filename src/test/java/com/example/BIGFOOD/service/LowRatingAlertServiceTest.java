package com.example.BIGFOOD.service;


import com.example.bigfood.repository.ReviewRepository;
import com.example.bigfood.scheduler.SendReportScheduler;
import com.example.bigfood.service.LowRatingAlertService;
import com.example.bigfood.service.OtpService;
import com.example.bigfood.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LowRatingAlertServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private OtpService otpService;
    @Mock
    private LowRatingAlertService lowRatingAlertService;
    @InjectMocks
    private SendReportScheduler sendReportScheduler;

    @Test
    @DisplayName("Should call service when scheduled task runs")
    void testSendWeeklyLowRatingAlerts_CallsService() {
        sendReportScheduler.sendReportToLowRateResraurant();
    }
}
