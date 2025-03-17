package com.dmdev.taskhome2.controller.unit;

import com.dmdev.taskhome2.dto.ServiceDto;
import com.dmdev.taskhome2.entity.Service;
import com.dmdev.taskhome2.entity.ServiceType;
import com.dmdev.taskhome2.mappers.ServiceMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceMapperTest {

    private final ServiceMapper serviceMapper = new ServiceMapper();

    @Test
    void toDto_shouldConvertServiceToServiceDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        Service service = Service.builder()
                .id(id)
                .name("Wi-Fi")
                .description("Free Wi-Fi access")
                .serviceType(ServiceType.FREE)
                .build();

        // Act
        ServiceDto serviceDto = serviceMapper.toDto(service);

        // Assert
        assertNotNull(serviceDto);
        assertEquals(id, serviceDto.getId());
        assertEquals("Wi-Fi", serviceDto.getName());
        assertEquals("Free Wi-Fi access", serviceDto.getDescription());
        assertEquals(ServiceType.FREE, serviceDto.getServiceType());
    }

    @Test
    void toDto_shouldThrowIllegalArgumentExceptionWhenServiceIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> serviceMapper.toDto(null));
        assertEquals("Service cannot be null", exception.getMessage());
    }

    @Test
    void toEntity_shouldConvertServiceDtoToService() {
        // Arrange
        UUID id = UUID.randomUUID();
        ServiceDto serviceDto = ServiceDto.builder()
                .id(id)
                .name("Parking")
                .description("Paid parking")
                .serviceType(ServiceType.PAID)
                .build();

        // Act
        Service service = serviceMapper.toEntity(serviceDto);

        // Assert
        assertNotNull(service);
        assertEquals(id, service.getId());
        assertEquals("Parking", service.getName());
        assertEquals("Paid parking", service.getDescription());
        assertEquals(ServiceType.PAID, service.getServiceType());
    }

    @Test
    void toEntity_shouldThrowIllegalArgumentExceptionWhenServiceDtoIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> serviceMapper.toEntity(null));
        assertEquals("Service cannot be null", exception.getMessage());
    }
}
