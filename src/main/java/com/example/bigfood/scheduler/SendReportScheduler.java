package com.example.bigfood.scheduler;


import com.example.bigfood.service.LowRatingAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SendReportScheduler {
    private final LowRatingAlertService lowRatingAlertService;

    @Scheduled(cron = "0 0 0 * * MON")
    public void sendReportToLowRateResraurant() {
        lowRatingAlertService.sendReportToLowRatingRestaurant();
    }
}
