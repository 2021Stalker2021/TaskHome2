package com.dmdev.taskhome2.service;

import com.dmdev.taskhome2.dto.ServiceDto;
import com.dmdev.taskhome2.entity.Service;
import com.dmdev.taskhome2.exception.ServiceNotFoundException;
import com.dmdev.taskhome2.mappers.ServiceMapper;
import com.dmdev.taskhome2.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Transactional
    public ServiceDto create(ServiceDto serviceDto) {
        Service savedService = serviceRepository.save(serviceMapper.toEntity(serviceDto));
        return serviceMapper.toDto(savedService);
    }

    @Transactional
    public ServiceDto updateService(UUID id, ServiceDto serviceDto) {
        Service existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        Service updatedService = serviceMapper.toEntity(serviceDto);
        updatedService.setId(existingService.getId()); // ID должен остаться прежним
        // поля будут установлены из dto

        Service savedService = serviceRepository.save(updatedService);
        return serviceMapper.toDto(savedService);
    }

    public List<ServiceDto> getAllServices() {
        List<Service> services = serviceRepository.findAll();

        return services.stream()
                .map(serviceMapper::toDto)
                .toList();
    }

    public ServiceDto getServiceById(UUID id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("No service found by id"));
        return serviceMapper.toDto(service);
    }

    @Transactional
    public boolean deleteService(UUID id) {
        return serviceRepository.findById(id)
                .map(entity -> {
                    serviceRepository.delete(entity);
                    serviceRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public void deleteAllServices() {
        serviceRepository.deleteAll();
    }
}
