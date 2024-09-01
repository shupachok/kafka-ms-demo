package com.sp.tickets.service;

import com.sp.tickets.rest.CreateTicketRestModel;

public interface TicketService {
    public String createTicket(CreateTicketRestModel createTicketRestModel);
}
