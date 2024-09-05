package com.sp.tickets.rest;

import com.sp.tickets.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private TicketService ticketService;
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<Object> createTicket(@RequestBody CreateTicketRestModel createTicketRestModel) {
        String ticketId = null;
        try {
            ticketId = ticketService.createTicket(createTicketRestModel);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(e.getMessage(),new Date(),"/tickets"));

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketId);
    }
}
