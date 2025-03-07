package com.dmdev.taskhome2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    private ServiceType serviceType;

    @ManyToMany(mappedBy = "services",cascade = CascadeType.ALL)
    /*
        @ManyToMany (обратная сторона отношения).
        Потому что В Service используется mappedBy, чтобы указать, что владельцем отношения является
        Attraction. Это позволяет избежать дублирования управления связями.
     */
    private List<Attraction> attractions;
}
