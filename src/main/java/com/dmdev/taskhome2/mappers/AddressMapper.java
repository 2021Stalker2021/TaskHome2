package com.dmdev.taskhome2.mappers;

import com.dmdev.taskhome2.dto.AddressDto;
import com.dmdev.taskhome2.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public AddressDto toDto(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("address cannot be null");
        }

        return AddressDto.builder()
                .id(address.getId())
                .building(address.getBuilding())
                .street(address.getStreet())
                .city(address.getCity())
                .region(address.getRegion())
                .build();
    }

    public Address toEntity(AddressDto addressDto) {
        if (addressDto == null) {
            throw new IllegalArgumentException("addressDto cannot be null");
        }
        return Address.builder()
                .id(addressDto.getId())
                .building(addressDto.getBuilding())
                .street(addressDto.getStreet())
                .city(addressDto.getCity())
                .region(addressDto.getRegion())
                .build();
    }
}
