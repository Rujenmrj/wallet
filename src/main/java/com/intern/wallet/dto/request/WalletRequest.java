package com.intern.wallet.dto.request;

import com.intern.wallet.constants.WalletType;
import lombok.Data;

import java.util.UUID;

@Data
public class WalletRequest {
    private UUID userId;
    private WalletType type;
}
