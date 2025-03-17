package com.dmdev.taskhome2.controller.unit;

import com.dmdev.taskhome2.dto.AddressDto;
import com.dmdev.taskhome2.dto.AttractionDto;
import com.dmdev.taskhome2.dto.ServiceDto;
import com.dmdev.taskhome2.dto.TicketInfoDto;
import com.dmdev.taskhome2.entity.*;
import com.dmdev.taskhome2.mappers.AddressMapper;
import com.dmdev.taskhome2.mappers.AttractionMapper;
import com.dmdev.taskhome2.mappers.ServiceMapper;
import com.dmdev.taskhome2.mappers.TicketInfoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttractionMapperTest {

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private TicketInfoMapper ticketInfoMapper;

    @Mock
    private ServiceMapper serviceMapper;

    @InjectMocks
    private AttractionMapper attractionMapper;

    @Test
    void toDto_shouldConvertEntityToDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        Address address = new Address();
        TicketInfo ticketInfo = new TicketInfo();
        Service service = new Service();

        Attraction attraction = Attraction.builder()
                .id(id)
                .name("Eiffel Tower")
                .description("Iconic tower in Paris")
                .type(AttractionType.LANDMARK)
                .address(address)
                .ticketInfo(ticketInfo)
                .services(List.of(service))
                .build();


        when(addressMapper.toDto(address)).thenReturn(null);
        when(ticketInfoMapper.toDto(ticketInfo)).thenReturn(null);
        when(serviceMapper.toDto(service)).thenReturn(null);

        // Act
        AttractionDto result = attractionMapper.toDto(attraction);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Eiffel Tower", result.getName());
        assertEquals("Iconic tower in Paris", result.getDescription());
        assertEquals(AttractionType.LANDMARK, result.getAttractionType());
        assertNotNull(result.getServices());
        assertEquals(1, result.getServices().size()); // Проверка, что services содержит 1 элемент

        // Проверяем вызовы мапперов
        verify(addressMapper, times(1)).toDto(address); // Проверка вызова addressMapper
        verify(ticketInfoMapper, times(1)).toDto(ticketInfo); // Проверка вызова ticketInfoMapper
        verify(serviceMapper, times(1)).toDto(service); // Проверка вызова serviceMapper
    }

    @Test
    void toDto_shouldThrowIllegalArgumentExceptionWhenAttractionIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> attractionMapper.toDto(null));
        assertEquals("attraction is null", exception.getMessage());
    }

    @Test
    void toEntity_shouldConvertDtoToEntity() {
        // Arrange
        UUID id = UUID.randomUUID();
        AddressDto addressDto = new AddressDto(); // Создание объекта AddressDto
        TicketInfoDto ticketInfoDto = new TicketInfoDto(); // Создание объекта TicketInfoDto
        ServiceDto serviceDto = new ServiceDto(); // Создание объекта ServiceDto

        AttractionDto attractionDto = AttractionDto.builder()
                .id(id)
                .name("Statue of Liberty")
                .description("Iconic statue in New York")
                .attractionType(AttractionType.LANDMARK)
                .address(addressDto)
                .ticketInfo(ticketInfoDto)
                .services(List.of(serviceDto))
                .build();

        // Мокируем вызовы мапперов
        when(addressMapper.toEntity(addressDto)).thenReturn(null);
        when(ticketInfoMapper.toEntity(ticketInfoDto)).thenReturn(null);
        when(serviceMapper.toEntity(serviceDto)).thenReturn(null);

        // Act
        Attraction result = attractionMapper.toEntity(attractionDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Statue of Liberty", result.getName());
        assertEquals("Iconic statue in New York", result.getDescription());
        assertEquals(AttractionType.LANDMARK, result.getType());
        assertNotNull(result.getServices());
        assertEquals(1, result.getServices().size()); // Проверка, что services содержит 1 элемент

        // Проверяем вызовы мапперов
        verify(addressMapper, times(1)).toEntity(addressDto); // Проверка вызова addressMapper
        verify(ticketInfoMapper, times(1)).toEntity(ticketInfoDto); // Проверка вызова ticketInfoMapper
        verify(serviceMapper, times(1)).toEntity(serviceDto); // Проверка вызова serviceMapper
    }

    @Test
    void toEntity_shouldThrowIllegalArgumentExceptionWhenAttractionDtoIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> attractionMapper.toEntity(null));
        assertEquals("attractionDto is null", exception.getMessage());
    }
}
