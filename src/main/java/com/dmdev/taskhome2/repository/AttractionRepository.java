package com.dmdev.taskhome2.repository;

import com.dmdev.taskhome2.entity.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, UUID> {

    List<Attraction> findByServicesName(String serviceName);

    List<Attraction> findByAddressCity(String addressCity);

    List<Attraction> findByTicketInfoPrice(BigDecimal ticketInfoPrice);
}
