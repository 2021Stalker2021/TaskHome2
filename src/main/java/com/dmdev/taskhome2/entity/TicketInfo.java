package com.dmdev.taskhome2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket_info")
public class TicketInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal price;

    private String currency;

    private Boolean availability;

    @OneToOne(mappedBy = "ticketInfo", cascade = CascadeType.ALL)
    private Attraction attraction;
}
