package com.ayush.ecommerce.module.auth.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final StringRedisTemplate redisTemplate;
    private static final SecureRandom random = new SecureRandom();

    @Override
    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1000000));

        redisTemplate.opsForValue()
                .set("otp:"+email, otp, Duration.ofMinutes(5));
        return otp;
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue()
                .get("otp:"+email);
        if(storedOtp == null){
            return false;
        }
        boolean matched = storedOtp.equals(otp);
        if(matched){
            redisTemplate.delete("otp:"+email);
        }
        return matched;
    }
}
