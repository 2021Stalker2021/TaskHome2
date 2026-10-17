package com.dmdev.taskhome2.controller.unit;

import com.dmdev.taskhome2.dto.TicketInfoDto;
import com.dmdev.taskhome2.entity.TicketInfo;
import com.dmdev.taskhome2.mappers.TicketInfoMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TicketInfoMapperTest {

    private final TicketInfoMapper ticketInfoMapper = new TicketInfoMapper();

    @Test
    void toDto_shouldConvertTicketInfoToTicketInfoDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        BigDecimal price = new BigDecimal("100.50");
        TicketInfo ticketInfo = TicketInfo.builder()
                .id(id)
                .price(price)
                .currency("USD")
                .availability(true)
                .build();

        // Act
        TicketInfoDto ticketInfoDto = ticketInfoMapper.toDto(ticketInfo);

        // Assert
        assertNotNull(ticketInfoDto);
        assertEquals(id, ticketInfoDto.getId());
        assertEquals(price, ticketInfoDto.getPrice()); // BigDecimal
        assertEquals("USD", ticketInfoDto.getCurrency());
        assertTrue(ticketInfoDto.getAvailability());
    }

    @Test
    void toDto_shouldThrowIllegalArgumentExceptionWhenTicketInfoIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ticketInfoMapper.toDto(null));
        assertEquals("ticketInfo cannot be null", exception.getMessage());
    }

    @Test
    void toEntity_shouldConvertTicketInfoDtoToTicketInfo() {
        // Arrange
        UUID id = UUID.randomUUID();
        BigDecimal price = new BigDecimal("200.75");
        TicketInfoDto ticketInfoDto = TicketInfoDto.builder()
                .id(id)
                .price(price)
                .currency("EUR")
                .availability(false)
                .build();

        // Act
        TicketInfo ticketInfo = ticketInfoMapper.toEntity(ticketInfoDto);

        // Assert
        assertNotNull(ticketInfo);
        assertEquals(id, ticketInfo.getId());
        assertEquals(price, ticketInfo.getPrice()); // BigDecimal
        assertEquals("EUR", ticketInfo.getCurrency());
        assertFalse(ticketInfo.getAvailability());
    }

    @Test
    void toEntity_shouldThrowIllegalArgumentExceptionWhenTicketInfoDtoIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ticketInfoMapper.toEntity(null));
        assertEquals("ticketInfoDto cannot be null", exception.getMessage());
    }
}
