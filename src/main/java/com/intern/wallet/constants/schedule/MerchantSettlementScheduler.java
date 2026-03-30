package com.intern.wallet.constants.schedule;

import com.intern.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantSettlementScheduler {

    private final WalletService walletService;

    // runs every night at midnight
    @Scheduled(cron = "${app.cron}")
    public void settleMerchantBalances() {

        log.info("Starting merchant settlement job");

        walletService.settleMerchantBalances();

        log.info("Merchant settlement completed");
    }
}
