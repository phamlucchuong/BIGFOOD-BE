package com.example.bigfood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {


    private final Map<String, OtpData> otpStorage = new HashMap<>();
    private final Random random = new SecureRandom();

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public String generateOtp(String key) {
        String otp = String.format("%04d", random.nextInt(10000));
        Instant expireAt = Instant.now().plusSeconds(60); 
        otpStorage.put(key, new OtpData(otp, expireAt));
        return otp;
    }

    public boolean validateOtp(String key, String otp) {
        OtpData data = otpStorage.get(key);
        if (data == null) return false;

        boolean notExpired = Instant.now().isBefore(data.expireAt());
        boolean correctOtp = data.otp().equals(otp);
        if (correctOtp && notExpired) {
            otpStorage.remove(key);
            return true;
        }
        return false;
    }

    public void sendOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Mã OTP của bạn");
        message.setText("Xin chào,\n\nMã OTP của bạn là: " + otp +
                "\nMã này có hiệu lực trong 1 phút.\n\nTrân trọng!");

        mailSender.send(message);
    }

    public void sendEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Cảnh báo hành vi tiêu cực!");
        message.setText("Xin chào quý khách,\n\nHệ thống của chúng tôi đã phát hiện nhiều đánh giá tiêu cực từ khách hàng về nhà hàng của bạn." +
                "\nVui lòng kiểm tra và cải thiện chất lượng dịch vụ để tránh các biện pháp xử lý từ hệ thống." +
                "\n\nTrân trọng!");

        mailSender.send(message);
    }

    private record OtpData(String otp, Instant expireAt) {}
}
