package com.sp.tickets.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private String message;
    private Date timestamp;
    private String detail;
}
