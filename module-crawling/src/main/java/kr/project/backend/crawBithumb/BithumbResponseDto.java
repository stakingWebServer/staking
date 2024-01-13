package com.craw.crawlingprogram.crawBithumb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BithumbResponseDto {
    private String status;
    private BithumbData data;
}
