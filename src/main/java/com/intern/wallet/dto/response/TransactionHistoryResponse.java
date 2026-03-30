package com.intern.wallet.dto.response;


import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class TransactionHistoryResponse {
    private List<TransactionEntry> history;
    private WalletResponse wallet;
}
