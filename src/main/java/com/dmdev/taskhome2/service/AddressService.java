package com.dmdev.taskhome2.service;

import com.dmdev.taskhome2.dto.AddressDto;
import com.dmdev.taskhome2.entity.Address;
import com.dmdev.taskhome2.exception.AddressNotFoundException;
import com.dmdev.taskhome2.exception.DuplicateAddressException;
import com.dmdev.taskhome2.mappers.AddressMapper;
import com.dmdev.taskhome2.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressService.class);
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Transactional
    public AddressDto createAddress(AddressDto addressDto) {
        log.info("Creating address: {}", addressDto);
        if (addressRepository.findByBuildingAndStreet(addressDto.getBuilding(), addressDto.getStreet()).isPresent()) {
            throw new DuplicateAddressException("The building or street already exists");
        }

        Address entity = addressMapper.toEntity(addressDto);
        Address savedAddress = addressRepository.save(entity);
        log.info("Address created with ID: {}", savedAddress.getId());

        return addressMapper.toDto(savedAddress);
    }

    public List<AddressDto> getAllAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    public AddressDto getAddressById(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("No addresses found by id"));
        return addressMapper.toDto(address);
    }

    @Transactional
    public AddressDto updateAddress(UUID id, AddressDto addressDto) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        Address updatedAddress = addressMapper.toEntity(addressDto);
        updatedAddress.setId(existingAddress.getId()); // ID должен остаться прежним
        // поля будут установлены из dto

        Address savedAddress = addressRepository.save(updatedAddress);
        return addressMapper.toDto(savedAddress);
    }

    @Transactional
    public void deleteAddress(UUID id) {
        try {
            addressRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AddressNotFoundException("Address not found");
        }
    }

    @Transactional
    public void deleteAllAddresses() {
        addressRepository.deleteAll();
    }
}
