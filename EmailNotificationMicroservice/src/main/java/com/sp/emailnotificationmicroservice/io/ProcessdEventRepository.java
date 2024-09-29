package com.sp.emailnotificationmicroservice.io;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessdEventRepository extends JpaRepository<ProcessedEventEntity,Long> {

    Optional<ProcessedEventEntity> findByMessageId(String messageId);
}
