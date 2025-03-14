package com.dmdev.taskhome2.mappers;

import com.dmdev.taskhome2.dto.ServiceDto;
import com.dmdev.taskhome2.entity.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public ServiceDto toDto(Service service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }

        return ServiceDto.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .serviceType(service.getServiceType())
                .build();
    }

    public Service toEntity(ServiceDto serviceDto) {
        if (serviceDto == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }

        return Service.builder()
                .id(serviceDto.getId())
                .name(serviceDto.getName())
                .description(serviceDto.getDescription())
                .serviceType(serviceDto.getServiceType())
                .build();
    }
}
