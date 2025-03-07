package com.dmdev.taskhome2.dto;

import com.dmdev.taskhome2.entity.AttractionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDto {
    private UUID id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private AttractionType attractionType;
    private AddressDto address;
    private TicketInfoDto ticketInfo;
    private List<ServiceDto> services;
}
