package com.intern.wallet.dto.response;

import com.intern.wallet.constants.WalletStatus;
import com.intern.wallet.constants.WalletType;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@ToString
public class WalletResponse {
    private UUID walletId;
    private UUID userId;
    private WalletType type;
    private WalletStatus status;
    private BigDecimal balance;
    private BigDecimal pendingBalance;
}
