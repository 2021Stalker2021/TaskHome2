package com.dmdev.taskhome2.repository;

import com.dmdev.taskhome2.entity.TicketInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketInfoRepository extends JpaRepository<TicketInfo, UUID> {
}
