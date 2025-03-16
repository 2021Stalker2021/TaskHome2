package com.dmdev.taskhome2.controller;

import com.dmdev.taskhome2.dto.AttractionDto;
import com.dmdev.taskhome2.service.AttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attractions")
public class AttractionController {

    private final AttractionService attractionService;

    @GetMapping
    public ResponseEntity<List<AttractionDto>> getAllAttractions() {
        return ResponseEntity.ok(attractionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttractionDto> findAttractionById(@PathVariable UUID id) {
        return ResponseEntity.ok(attractionService.getAttractionById(id));
    }

    @GetMapping("/service/name")
    public ResponseEntity<List<AttractionDto>> findAttractionByServiceName(@RequestParam String serviceName) {
        return ResponseEntity.ok(attractionService.getAttractionByServiceName(serviceName));
    }

    @GetMapping("/address/city")
    public ResponseEntity<List<AttractionDto>> findAttractionByAddressCity(@RequestParam String city) {
        return ResponseEntity.ok(attractionService.getAttractionByAddressCity(city));
    }

    @GetMapping("/ticket/price")
    public ResponseEntity<List<AttractionDto>> findAttractionByTicketPrice(@RequestParam BigDecimal ticketPrice) {
        return ResponseEntity.ok(attractionService.findAttractionByTicketPrice(ticketPrice));
    }


    @PostMapping
    public ResponseEntity<AttractionDto> createAttraction(@RequestBody AttractionDto attractionDto) {
        AttractionDto createdAttraction = attractionService.create(attractionDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()                           // /api/v1/addresses
                .path("/{id}")                                  // Добавляем /{id} к URL
                .buildAndExpand(createdAttraction.getId())         // Заменяем {id} на реальный UUID
                .toUri();

        return ResponseEntity.created(location).body(createdAttraction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttractionDto> updateAttraction(@PathVariable UUID id, @RequestBody AttractionDto attractionDto) {
        return ResponseEntity.ok(attractionService.updateAttraction(id, attractionDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttraction(@PathVariable UUID id) {
        return attractionService.deleteAttraction(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Void> deleteAllAttractions() {
        attractionService.deleteAllAttractions();
        return ResponseEntity.noContent().build();
    }
}
