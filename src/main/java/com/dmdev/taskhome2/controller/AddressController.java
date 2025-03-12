package com.dmdev.taskhome2.controller;

import com.dmdev.taskhome2.dto.AddressDto;
import com.dmdev.taskhome2.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable UUID id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto addressDto) {
//        AddressDto createdAddress = addressService.createAddress(addressDto);
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()                           // /api/v1/addresses
//                .path("/{id}")                                  // Добавляем /{id} к URL
//                .buildAndExpand(createdAddress.getId())         // Заменяем {id} на реальный UUID
//                .toUri();
//
//        return ResponseEntity.created(location).body(createdAddress);

        return ResponseEntity.ok(addressService.createAddress(addressDto));
    }
}
