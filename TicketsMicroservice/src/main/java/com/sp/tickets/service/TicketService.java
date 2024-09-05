package com.sp.tickets.service;

import com.sp.tickets.rest.CreateTicketRestModel;

import java.util.concurrent.ExecutionException;

public interface TicketService {
    public String createTicket(CreateTicketRestModel createTicketRestModel) throws Exception;
}
