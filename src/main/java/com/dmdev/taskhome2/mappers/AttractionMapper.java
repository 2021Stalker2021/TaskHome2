package com.dmdev.taskhome2.mappers;

import com.dmdev.taskhome2.dto.AttractionDto;
import com.dmdev.taskhome2.entity.Attraction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttractionMapper {
    private final AddressMapper addressMapper;
    private final TicketInfoMapper ticketInfoMapper;
    private final ServiceMapper serviceMapper;

    public AttractionDto toDto(Attraction attraction) {
        if (attraction == null) {
            throw new IllegalArgumentException("attraction is null");
        }

        return AttractionDto.builder()
                .id(attraction.getId())
                .name(attraction.getName())
                .description(attraction.getDescription())
                .attractionType(attraction.getType())
                .address(attraction.getAddress() != null ? addressMapper.toDto(attraction.getAddress()) : null)
                .ticketInfo(attraction.getTicketInfo() != null ? ticketInfoMapper.toDto(attraction.getTicketInfo()) : null)
                .services(attraction.getServices().stream()
                        .map(serviceMapper::toDto)
                        .toList()).build();
    }

    public Attraction toEntity(AttractionDto attractionDto) {
        if (attractionDto == null) {
            throw new IllegalArgumentException("attractionDto is null");
        }

        return Attraction.builder()
                .id(attractionDto.getId())
                .name(attractionDto.getName())
                .description(attractionDto.getDescription())
                .type(attractionDto.getAttractionType())
                .address(addressMapper.toEntity(attractionDto.getAddress()))
                .ticketInfo(ticketInfoMapper.toEntity(attractionDto.getTicketInfo()))
                .services(attractionDto.getServices().stream()
                        .map(serviceMapper::toEntity)
                        .toList()).build();
    }
}
