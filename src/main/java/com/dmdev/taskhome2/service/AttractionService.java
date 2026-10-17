package com.dmdev.taskhome2.service;

import com.dmdev.taskhome2.dto.AttractionDto;
import com.dmdev.taskhome2.entity.Attraction;
import com.dmdev.taskhome2.exception.AttractionNotFoundException;
import com.dmdev.taskhome2.mappers.AttractionMapper;
import com.dmdev.taskhome2.repository.AttractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttractionService {

    private final AttractionRepository attractionRepository;
    private final AttractionMapper attractionMapper;

    @Transactional
    public AttractionDto create(AttractionDto attractionDto) {
        Attraction savedAttraction = attractionRepository.save(attractionMapper.toEntity(attractionDto));
        return attractionMapper.toDto(savedAttraction);
    }

    public List<AttractionDto> getAll() {
        return attractionRepository.findAll().stream()
                .map(attractionMapper::toDto)
                .toList();
    }

    public List<AttractionDto> getAttractionByServiceName(String serviceName) {
        List<Attraction> attractions = attractionRepository.findByServicesName(serviceName);

        if (attractions.isEmpty()) {
            throw new IllegalArgumentException("No landmarks were found by service name: " + serviceName);
        }

        return attractions.stream()
                .map(attractionMapper::toDto)
                .toList();
    }

    public List<AttractionDto> getAttractionByAddressCity(String addressCity) {
        List<Attraction> attractions = attractionRepository.findByAddressCity(addressCity);

        if (attractions.isEmpty()) {
            throw new IllegalArgumentException("No landmarks were found by address city: " + addressCity);
        }

        return attractions.stream()
                .map(attractionMapper::toDto)
                .toList();
    }

    public List<AttractionDto> findAttractionByTicketPrice(BigDecimal ticketPrice) {
        List<Attraction> attractions = attractionRepository.findByTicketInfoPrice(ticketPrice);

        if (attractions.isEmpty()) {
            throw new IllegalArgumentException("No landmarks were found by ticket price: " + ticketPrice);
        }

        return attractions.stream()
                .map(attractionMapper::toDto)
                .toList();
    }

    public AttractionDto getAttractionById(UUID id) {
        Attraction attraction = attractionRepository.findById(id)
                .orElseThrow(() -> new AttractionNotFoundException("No attractions found by id"));
        return attractionMapper.toDto(attraction);
    }

    @Transactional
    public AttractionDto updateAttraction(UUID id, AttractionDto attractionDto) {
        Attraction existingAttraction = attractionRepository.findById(id)
                .orElseThrow(() -> new AttractionNotFoundException("Address not found"));

        Attraction updatedAttraction = attractionMapper.toEntity(attractionDto);
        updatedAttraction.setId(existingAttraction.getId()); // ID должен остаться прежним
        // поля будут установлены из dto

        Attraction savedAttraction = attractionRepository.save(updatedAttraction);
        return attractionMapper.toDto(savedAttraction);
    }

    @Transactional
    public boolean deleteAttraction(UUID id) {
        return attractionRepository.findById(id)
                .map(entity -> {
                    attractionRepository.delete(entity);
                    attractionRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public void deleteAllAttractions() {
        attractionRepository.deleteAll();
    }
}
