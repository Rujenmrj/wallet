package com.intern.wallet.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class  PageResponse<T> {
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private long totalPages;
    private T content;
}
