package com.dmdev.taskhome2.controller.unit;

import com.dmdev.taskhome2.dto.AddressDto;
import com.dmdev.taskhome2.entity.Address;
import com.dmdev.taskhome2.exception.AddressNotFoundException;
import com.dmdev.taskhome2.exception.DuplicateAddressException;
import com.dmdev.taskhome2.mappers.AddressMapper;
import com.dmdev.taskhome2.repository.AddressRepository;
import com.dmdev.taskhome2.service.AddressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressService addressService;

    @Test
    void createAddress_shouldReturnCreatedAddressDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        AddressDto addressDto = AddressDto.builder()
                .building(10)
                .street("Main Street")
                .city("New York")
                .region("NY")
                .build();

        Address address = Address.builder()
                .id(id)
                .building(10)
                .street("Main Street")
                .city("New York")
                .region("NY")
                .build();

        when(addressMapper.toEntity(addressDto)).thenReturn(address);
        when(addressRepository.findByBuildingAndStreet(10, "Main Street")).thenReturn(Optional.empty());
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toDto(address)).thenReturn(addressDto);

        // Act
        AddressDto result = addressService.createAddress(addressDto);

        // Assert
        assertNotNull(result);
        assertEquals(10, result.getBuilding());
        assertEquals("Main Street", result.getStreet());
        assertEquals("New York", result.getCity());
        assertEquals("NY", result.getRegion());

        verify(addressRepository, times(1)).findByBuildingAndStreet(10, "Main Street");
        verify(addressRepository, times(1)).save(address);
        verify(addressMapper, times(1)).toDto(address);
    }

    @Test
    void createAddress_shouldThrowDuplicateAddressException() {
        // Arrange
        AddressDto addressDto = AddressDto.builder()
                .building(10)
                .street("Main Street")
                .city("New York")
                .region("NY")
                .build();

        when(addressRepository.findByBuildingAndStreet(10, "Main Street")).thenReturn(Optional.of(new Address()));

        // Act & Assert
        DuplicateAddressException exception = assertThrows(DuplicateAddressException.class, () -> addressService.createAddress(addressDto));
        assertEquals("The building or street already exists", exception.getMessage());

        verify(addressRepository, times(1)).findByBuildingAndStreet(10, "Main Street");
        verify(addressRepository, never()).save(any());
    }

    @Test
    void getAddressById_shouldReturnAddressDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        Address address = Address.builder()
                .id(id)
                .building(15)
                .street("Broadway")
                .city("Los Angeles")
                .region("CA")
                .build();

        AddressDto addressDto = AddressDto.builder()
                .id(id)
                .building(15)
                .street("Broadway")
                .city("Los Angeles")
                .region("CA")
                .build();

        when(addressRepository.findById(id)).thenReturn(Optional.of(address));
        when(addressMapper.toDto(address)).thenReturn(addressDto);

        // Act
        AddressDto result = addressService.getAddressById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(15, result.getBuilding());
        assertEquals("Broadway", result.getStreet());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("CA", result.getRegion());

        verify(addressRepository, times(1)).findById(id);
        verify(addressMapper, times(1)).toDto(address);
    }

    @Test
    void getAddressById_shouldThrowAddressNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(addressRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class, () -> addressService.getAddressById(id));
        assertEquals("No addresses found by id", exception.getMessage());

        verify(addressRepository, times(1)).findById(id);
        verify(addressMapper, never()).toDto(any());
    }
}
