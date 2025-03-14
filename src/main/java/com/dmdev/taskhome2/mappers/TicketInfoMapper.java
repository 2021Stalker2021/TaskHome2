package com.dmdev.taskhome2.mappers;

import com.dmdev.taskhome2.dto.TicketInfoDto;
import com.dmdev.taskhome2.entity.TicketInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketInfoMapper {
    public TicketInfoDto toDto(TicketInfo ticketInfo) {
        if (ticketInfo == null) {
            throw new IllegalArgumentException("ticketInfo cannot be null");
        }

        return TicketInfoDto.builder()
                .id(ticketInfo.getId())
                .price(ticketInfo.getPrice())
                .currency(ticketInfo.getCurrency())
                .availability(ticketInfo.getAvailability())
                .build();
    }

    public TicketInfo toEntity(TicketInfoDto ticketInfoDto) {
        if (ticketInfoDto == null) {
            throw new IllegalArgumentException("ticketInfoDto cannot be null");
        }

        return TicketInfo.builder()
                .id(ticketInfoDto.getId())
                .price(ticketInfoDto.getPrice())
                .currency(ticketInfoDto.getCurrency())
                .availability(ticketInfoDto.getAvailability())
                .build();
    }
}
