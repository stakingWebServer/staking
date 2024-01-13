package com.craw.crawlingprogram.crawKorbit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KorbitResponseDto {
    private Long timestamp;
    private String last;
    private String open;
    private String bid;
    private String ask;
    private String low;
    private String high;
    private String volume;
    private String change;
    private String changePercent;
}
