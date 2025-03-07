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
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer building;

    private String street;

    private String city;

    private String region;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    private List<Attraction> attractions;
}