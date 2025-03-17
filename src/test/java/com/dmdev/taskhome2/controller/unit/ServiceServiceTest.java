package com.dmdev.taskhome2.controller.unit;

import com.dmdev.taskhome2.dto.ServiceDto;
import com.dmdev.taskhome2.entity.Service;
import com.dmdev.taskhome2.entity.ServiceType;
import com.dmdev.taskhome2.exception.ServiceNotFoundException;
import com.dmdev.taskhome2.mappers.ServiceMapper;
import com.dmdev.taskhome2.repository.ServiceRepository;
import com.dmdev.taskhome2.service.ServiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceMapper serviceMapper;

    @InjectMocks
    private ServiceService serviceService;

    @Test
    void create_shouldReturnServiceDto() {
        // Arrange
        ServiceDto serviceDto = new ServiceDto();
        Service service = new Service();
        when(serviceMapper.toEntity(serviceDto)).thenReturn(service);
        when(serviceRepository.save(service)).thenReturn(service);
        when(serviceMapper.toDto(service)).thenReturn(serviceDto);

        // Act
        ServiceDto result = serviceService.create(serviceDto);

        // Assert
        assertNotNull(result);
        verify(serviceMapper, times(1)).toEntity(serviceDto);
        verify(serviceRepository, times(1)).save(service);
        verify(serviceMapper, times(1)).toDto(service);
    }

    @Test
    void updateService_shouldReturnUpdatedServiceDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        ServiceDto serviceDto = new ServiceDto();
        Service existingService = new Service();
        Service updatedService = new Service();

        when(serviceRepository.findById(id)).thenReturn(Optional.of(existingService));
        when(serviceMapper.toEntity(serviceDto)).thenReturn(updatedService);
        when(serviceRepository.save(updatedService)).thenReturn(updatedService);
        when(serviceMapper.toDto(updatedService)).thenReturn(serviceDto);

        // Act
        ServiceDto result = serviceService.updateService(id, serviceDto);

        // Assert
        assertNotNull(result);
        verify(serviceRepository, times(1)).findById(id);
        verify(serviceMapper, times(1)).toEntity(serviceDto);
        verify(serviceRepository, times(1)).save(updatedService);
        verify(serviceMapper, times(1)).toDto(updatedService);
    }

    @Test
    void updateService_shouldThrowServiceNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        ServiceDto serviceDto = new ServiceDto();

        when(serviceRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ServiceNotFoundException.class, () -> serviceService.updateService(id, serviceDto));
        verify(serviceRepository, times(1)).findById(id);
        verifyNoMoreInteractions(serviceMapper, serviceRepository);
    }

    @Test
    void getAllServices_shouldReturnListOfServiceDto() {
        // Arrange
        Service service1 = Service.builder()
                .id(UUID.randomUUID())
                .name("Wi-Fi")
                .description("Free Wi-Fi access")
                .serviceType(ServiceType.FREE)
                .build();

        Service service2 = Service.builder()
                .id(UUID.randomUUID())
                .name("Parking")
                .description("Paid parking")
                .serviceType(ServiceType.PAID)
                .build();

        ServiceDto serviceDto1 = ServiceDto.builder()
                .id(service1.getId())
                .name(service1.getName())
                .description(service1.getDescription())
                .serviceType(service1.getServiceType())
                .build();

        ServiceDto serviceDto2 = ServiceDto.builder()
                .id(service2.getId())
                .name(service2.getName())
                .description(service2.getDescription())
                .serviceType(service2.getServiceType())
                .build();

        when(serviceRepository.findAll()).thenReturn(List.of(service1, service2));
        when(serviceMapper.toDto(service1)).thenReturn(serviceDto1);
        when(serviceMapper.toDto(service2)).thenReturn(serviceDto2);

        // Act
        List<ServiceDto> result = serviceService.getAllServices();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(serviceDto1, result.get(0));
        assertEquals(serviceDto2, result.get(1));

        verify(serviceRepository, times(1)).findAll();
        verify(serviceMapper, times(1)).toDto(service1);
        verify(serviceMapper, times(1)).toDto(service2);
    }

    @Test
    void getServiceById_shouldReturnServiceDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        Service service = new Service();
        ServiceDto serviceDto = new ServiceDto();

        when(serviceRepository.findById(id)).thenReturn(Optional.of(service));
        when(serviceMapper.toDto(service)).thenReturn(serviceDto);

        // Act
        ServiceDto result = serviceService.getServiceById(id);

        // Assert
        assertNotNull(result);
        verify(serviceRepository, times(1)).findById(id);
        verify(serviceMapper, times(1)).toDto(service);
    }

    @Test
    void getServiceById_shouldThrowServiceNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();

        when(serviceRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ServiceNotFoundException.class, () -> serviceService.getServiceById(id));
        verify(serviceRepository, times(1)).findById(id);
        verifyNoMoreInteractions(serviceMapper);
    }

    @Test
    void deleteService_shouldReturnTrueIfServiceExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        Service service = new Service();

        when(serviceRepository.findById(id)).thenReturn(Optional.of(service));

        // Act
        boolean result = serviceService.deleteService(id);

        // Assert
        assertTrue(result);
        verify(serviceRepository, times(1)).findById(id);
        verify(serviceRepository, times(1)).delete(service);
        verify(serviceRepository, times(1)).flush();
    }

    @Test
    void deleteService_shouldReturnFalseIfServiceDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();

        when(serviceRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        boolean result = serviceService.deleteService(id);

        // Assert
        assertFalse(result);
        verify(serviceRepository, times(1)).findById(id);
        verifyNoMoreInteractions(serviceRepository);
    }

    @Test
    void deleteAllServices_shouldCallRepositoryDeleteAll() {
        // Arrange
        doNothing().when(serviceRepository).deleteAll();

        // Act
        serviceService.deleteAllServices();

        // Assert
        verify(serviceRepository, times(1)).deleteAll();
    }
}
