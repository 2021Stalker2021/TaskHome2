package com.dmdev.taskhome2.controller;

import com.dmdev.taskhome2.dto.TicketInfoDto;
import com.dmdev.taskhome2.service.TicketInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ticketInfo")
public class TicketInfoController {

    private final TicketInfoService ticketInfoService;

    @GetMapping()
    public ResponseEntity<List<TicketInfoDto>> getAllTicketInfo() {
        return ResponseEntity.ok(ticketInfoService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketInfoDto> getTicketInfoById(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketInfoService.getTicketById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketInfoDto> updateTicketInfo(@PathVariable UUID id, @RequestBody TicketInfoDto ticketInfoDto) {
        return ResponseEntity.ok(ticketInfoService.updateTicketInfo(id, ticketInfoDto));
    }

    @PostMapping
    public ResponseEntity<TicketInfoDto> createTicketInfo(@RequestBody TicketInfoDto ticketInfoDto) {
        ticketInfoService.create(ticketInfoDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicketInfo(@PathVariable UUID id) {
        return ticketInfoService.deleteTicket(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Void> deleteAllTicketInfo() {
        ticketInfoService.deleteAllServices();
        return ResponseEntity.noContent().build();
    }
}
