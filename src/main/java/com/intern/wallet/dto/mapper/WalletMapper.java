package com.intern.wallet.dto.mapper;

import com.intern.wallet.dto.response.WalletResponse;
import com.intern.wallet.entity.Wallet;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;


public class WalletMapper {

    @Bean
    public static WalletResponse toResponse(Wallet wallet) {
        WalletResponse response = new WalletResponse();

        response.setWalletId(wallet.getId());   // assuming AbstractEntity has id
        response.setUserId(wallet.getUserId());
        response.setType(wallet.getType());
        response.setStatus(wallet.getStatus());
        response.setPendingBalance(wallet.getPendingBalance());
        response.setBalance(wallet.getAvailableBalance());

        return response;
    }
}