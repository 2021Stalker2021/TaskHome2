package com.dmdev.taskhome2.service;

import com.dmdev.taskhome2.dto.TicketInfoDto;
import com.dmdev.taskhome2.entity.TicketInfo;
import com.dmdev.taskhome2.exception.TicketNotFoundException;
import com.dmdev.taskhome2.mappers.TicketInfoMapper;
import com.dmdev.taskhome2.repository.TicketInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TicketInfoService {

    private final TicketInfoRepository ticketInfoRepository;
    private final TicketInfoMapper ticketInfoMapper;

    @Transactional
    public TicketInfoDto create(TicketInfoDto ticketInfoDto) {
        TicketInfo savedTicket = ticketInfoRepository.save(ticketInfoMapper.toEntity(ticketInfoDto));
        return ticketInfoMapper.toDto(savedTicket);
    }

    public List<TicketInfoDto> getAllTickets() {
        List<TicketInfo> tickets = ticketInfoRepository.findAll();

        return tickets.stream()
                .map(ticketInfoMapper::toDto)
                .toList();
    }

    public TicketInfoDto getTicketById(UUID id) {
        TicketInfo ticket = ticketInfoRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("No ticket found by id"));
        return ticketInfoMapper.toDto(ticket);
    }

    @Transactional
    public TicketInfoDto updateTicketInfo(UUID id, TicketInfoDto ticketInfoDto) {
        TicketInfo existingTicket = ticketInfoRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        TicketInfo updatedTicket = ticketInfoMapper.toEntity(ticketInfoDto);
        updatedTicket.setId(existingTicket.getId()); // ID должен остаться прежним
        // поля будут установлены из dto

        TicketInfo savedTicket = ticketInfoRepository.save(updatedTicket);
        return ticketInfoMapper.toDto(savedTicket);
    }

    @Transactional
    public boolean deleteTicket(UUID id) {
        return ticketInfoRepository.findById(id)
                .map(entity -> {
                    ticketInfoRepository.delete(entity);
                    ticketInfoRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public void deleteAllServices() {
        ticketInfoRepository.deleteAll();
    }
}
