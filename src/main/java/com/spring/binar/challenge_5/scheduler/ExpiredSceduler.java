package com.spring.binar.challenge_5.scheduler;

import com.spring.binar.challenge_5.service.implementation.PaymentServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component

public class ExpiredSceduler {
    private static final Logger LOG = LogManager.getLogger(PaymentServiceImpl.class);
    @Scheduled(initialDelay = 30000, fixedDelay = 30000)
    public void markExpired(){
        Logger.info("Pembayaran : {} ", new Date());
    }
}
