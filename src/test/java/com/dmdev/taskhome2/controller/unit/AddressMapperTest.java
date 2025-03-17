package com.dmdev.taskhome2.controller.unit;

import com.dmdev.taskhome2.dto.AddressDto;
import com.dmdev.taskhome2.entity.Address;
import com.dmdev.taskhome2.mappers.AddressMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AddressMapperTest {

    private final AddressMapper addressMapper = new AddressMapper();

    @Test
    void toDto_shouldConvertEntityToDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        Address address = Address.builder()
                .id(id)
                .building(10)
                .street("Main Street")
                .city("New York")
                .region("NY")
                .build();

        // Act
        AddressDto result = addressMapper.toDto(address);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(10, result.getBuilding());
        assertEquals("Main Street", result.getStreet());
        assertEquals("New York", result.getCity());
        assertEquals("NY", result.getRegion());
    }

    @Test
    void toDto_shouldThrowIllegalArgumentExceptionWhenAddressIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> addressMapper.toDto(null));
        assertEquals("address cannot be null", exception.getMessage());
    }

    @Test
    void toEntity_shouldConvertDtoToEntity() {
        // Arrange
        UUID id = UUID.randomUUID();
        AddressDto addressDto = AddressDto.builder()
                .id(id)
                .building(15)
                .street("Broadway")
                .city("Los Angeles")
                .region("CA")
                .build();

        // Act
        Address result = addressMapper.toEntity(addressDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(15, result.getBuilding());
        assertEquals("Broadway", result.getStreet());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("CA", result.getRegion());
    }

    @Test
    void toEntity_shouldThrowIllegalArgumentExceptionWhenAddressDtoIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> addressMapper.toEntity(null));
        assertEquals("addressDto cannot be null", exception.getMessage());
    }
}
