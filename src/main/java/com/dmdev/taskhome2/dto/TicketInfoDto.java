package com.dmdev.taskhome2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketInfoDto {
    private UUID id;
    private BigDecimal price;
    private String currency;
    private Boolean availability;
}
