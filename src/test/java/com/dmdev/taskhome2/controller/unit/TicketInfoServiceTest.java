package com.dmdev.taskhome2.controller.unit;

import com.dmdev.taskhome2.dto.TicketInfoDto;
import com.dmdev.taskhome2.entity.TicketInfo;
import com.dmdev.taskhome2.exception.TicketNotFoundException;
import com.dmdev.taskhome2.mappers.TicketInfoMapper;
import com.dmdev.taskhome2.repository.TicketInfoRepository;
import com.dmdev.taskhome2.service.TicketInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketInfoServiceTest {

    @Mock
    private TicketInfoRepository ticketInfoRepository;

    @Mock
    private TicketInfoMapper ticketInfoMapper;

    @InjectMocks
    private TicketInfoService ticketInfoService;

    @Test
    void create_shouldReturnTicketInfoDto() {
        // Arrange
        BigDecimal price = new BigDecimal("150.25");
        TicketInfoDto ticketInfoDto = TicketInfoDto.builder()
                .price(price)
                .currency("USD")
                .availability(true)
                .build();

        TicketInfo ticketInfo = new TicketInfo();
        when(ticketInfoMapper.toEntity(ticketInfoDto)).thenReturn(ticketInfo);
        when(ticketInfoRepository.save(ticketInfo)).thenReturn(ticketInfo);
        when(ticketInfoMapper.toDto(ticketInfo)).thenReturn(ticketInfoDto);

        // Act
        TicketInfoDto result = ticketInfoService.create(ticketInfoDto);

        // Assert
        assertNotNull(result);
        assertEquals(price, result.getPrice()); // BigDecimal
        verify(ticketInfoMapper, times(1)).toEntity(ticketInfoDto);
        verify(ticketInfoRepository, times(1)).save(ticketInfo);
        verify(ticketInfoMapper, times(1)).toDto(ticketInfo);
    }

    @Test
    void getAllTickets_shouldReturnListOfTicketInfoDto() {
        // Arrange
        BigDecimal price1 = new BigDecimal("100.50");
        BigDecimal price2 = new BigDecimal("200.75");

        TicketInfo ticket1 = TicketInfo.builder()
                .price(price1)
                .currency("USD")
                .availability(true)
                .build();

        TicketInfo ticket2 = TicketInfo.builder()
                .price(price2)
                .currency("EUR")
                .availability(false)
                .build();

        TicketInfoDto ticketDto1 = TicketInfoDto.builder()
                .price(price1)
                .currency("USD")
                .availability(true)
                .build();

        TicketInfoDto ticketDto2 = TicketInfoDto.builder()
                .price(price2)
                .currency("EUR")
                .availability(false)
                .build();

        when(ticketInfoRepository.findAll()).thenReturn(List.of(ticket1, ticket2));
        when(ticketInfoMapper.toDto(ticket1)).thenReturn(ticketDto1);
        when(ticketInfoMapper.toDto(ticket2)).thenReturn(ticketDto2);

        // Act
        List<TicketInfoDto> result = ticketInfoService.getAllTickets();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(price1, result.get(0).getPrice()); // BigDecimal
        assertEquals(price2, result.get(1).getPrice()); // BigDecimal
        verify(ticketInfoRepository, times(1)).findAll();
        verify(ticketInfoMapper, times(1)).toDto(ticket1);
        verify(ticketInfoMapper, times(1)).toDto(ticket2);
    }

    @Test
    void getTicketById_shouldReturnTicketInfoDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        BigDecimal price = new BigDecimal("300.00");
        TicketInfo ticketInfo = TicketInfo.builder()
                .price(price)
                .currency("GBP")
                .availability(true)
                .build();

        TicketInfoDto ticketInfoDto = TicketInfoDto.builder()
                .price(price)
                .currency("GBP")
                .availability(true)
                .build();

        when(ticketInfoRepository.findById(id)).thenReturn(Optional.of(ticketInfo));
        when(ticketInfoMapper.toDto(ticketInfo)).thenReturn(ticketInfoDto);

        // Act
        TicketInfoDto result = ticketInfoService.getTicketById(id);

        // Assert
        assertNotNull(result);
        assertEquals(price, result.getPrice()); // BigDecimal
        verify(ticketInfoRepository, times(1)).findById(id);
        verify(ticketInfoMapper, times(1)).toDto(ticketInfo);
    }

    @Test
    void getTicketById_shouldThrowTicketNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(ticketInfoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TicketNotFoundException.class, () -> ticketInfoService.getTicketById(id));
        verify(ticketInfoRepository, times(1)).findById(id);
    }

    @Test
    void updateTicketInfo_shouldReturnUpdatedTicketInfoDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        BigDecimal price = new BigDecimal("400.50");
        TicketInfoDto ticketInfoDto = TicketInfoDto.builder()
                .price(price)
                .currency("JPY")
                .availability(false)
                .build();

        TicketInfo existingTicket = new TicketInfo();
        TicketInfo updatedTicket = new TicketInfo();

        when(ticketInfoRepository.findById(id)).thenReturn(Optional.of(existingTicket));
        when(ticketInfoMapper.toEntity(ticketInfoDto)).thenReturn(updatedTicket);
        when(ticketInfoRepository.save(updatedTicket)).thenReturn(updatedTicket);
        when(ticketInfoMapper.toDto(updatedTicket)).thenReturn(ticketInfoDto);

        // Act
        TicketInfoDto result = ticketInfoService.updateTicketInfo(id, ticketInfoDto);

        // Assert
        assertNotNull(result);
        assertEquals(price, result.getPrice()); // BigDecimal
        verify(ticketInfoRepository, times(1)).findById(id);
        verify(ticketInfoMapper, times(1)).toEntity(ticketInfoDto);
        verify(ticketInfoRepository, times(1)).save(updatedTicket);
        verify(ticketInfoMapper, times(1)).toDto(updatedTicket);
    }

    @Test
    void updateTicketInfo_shouldThrowTicketNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        TicketInfoDto ticketInfoDto = new TicketInfoDto();
        when(ticketInfoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TicketNotFoundException.class, () -> ticketInfoService.updateTicketInfo(id, ticketInfoDto));
        verify(ticketInfoRepository, times(1)).findById(id);
    }

    @Test
    void deleteTicket_shouldReturnTrueIfTicketExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        TicketInfo ticketInfo = new TicketInfo();
        when(ticketInfoRepository.findById(id)).thenReturn(Optional.of(ticketInfo));

        // Act
        boolean result = ticketInfoService.deleteTicket(id);

        // Assert
        assertTrue(result);
        verify(ticketInfoRepository, times(1)).findById(id);
        verify(ticketInfoRepository, times(1)).delete(ticketInfo);
        verify(ticketInfoRepository, times(1)).flush();
    }

    @Test
    void deleteTicket_shouldReturnFalseIfTicketDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(ticketInfoRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        boolean result = ticketInfoService.deleteTicket(id);

        // Assert
        assertFalse(result);
        verify(ticketInfoRepository, times(1)).findById(id);
    }

    @Test
    void deleteAllServices_shouldCallRepositoryDeleteAll() {
        // Arrange
        doNothing().when(ticketInfoRepository).deleteAll();

        // Act
        ticketInfoService.deleteAllServices();

        // Assert
        verify(ticketInfoRepository, times(1)).deleteAll();
    }
}
