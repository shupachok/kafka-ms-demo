package com.sp.emailnotificationmicroservice.io;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "processed-events")
@Data
public class ProcessedEventEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String messageId;

    @Column(nullable = false)
    private String ticketId;
}
