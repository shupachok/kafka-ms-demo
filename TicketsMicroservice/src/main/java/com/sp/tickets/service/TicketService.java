package com.sp.tickets.service;

import com.sp.core.dto.Ticket;
import com.sp.tickets.rest.CreateTicketRestModel;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface TicketService {
    public String createTicket(CreateTicketRestModel createTicketRestModel) throws Exception;
    List<Ticket> findAll();
    Ticket reserve(Ticket desiredProduct, UUID orderId);
    void cancelReservation(Ticket productToCancel, UUID orderId);
}
