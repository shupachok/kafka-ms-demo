package com.sp.tickets.rest;

import com.sp.tickets.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets ")
public class TicketController {

    private TicketService ticketService;
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<String> createTicket(@RequestBody CreateTicketRestModel createTicketRestModel) {
        ticketService.createTicket(createTicketRestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
}
