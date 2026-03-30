package com.intern.wallet.dto.request;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class BalanceInquiryRequest {
    private UUID walletId;
}
