package com.example.demo;

import com.example.demo.Controller_jwt.TicketController;
import com.example.demo.Store.Model.TicketModel;
import com.example.demo.Store.Service.TicketService;
import com.example.demo.User.dto.Mensaje;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private TicketController ticketController;

    private TicketModel ticketModel;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ticketModel = new TicketModel();
        ticketModel.setIsValid(0);
        ticketModel.setMensaje("Required quantity out of stock");
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testNuevo_TicketIsValid() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(ticketService.createTicket(ticketModel)).thenReturn(ticketModel);

        ResponseEntity<?> response = ticketController.nuevo(ticketModel, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Ticket Saved", ((Mensaje) response.getBody()).getMensaje());
        verify(ticketService, times(1)).createTicket(ticketModel);
    }

    @Test
    void testNuevo_TicketInvalidFields() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = ticketController.nuevo(ticketModel, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Misplaced or invalid fields", ((Mensaje) response.getBody()).getMensaje());
    }

    @Test
    void testNuevo_Exception() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(ticketService.createTicket(ticketModel)).thenThrow(RuntimeException.class);

        ResponseEntity<?> response = ticketController.nuevo(ticketModel, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Fail Saving Ticket", ((Mensaje) response.getBody()).getMensaje());
    }

    @Test
    void testGetAllTickets_All() {
        Page<?> page = new PageImpl<>(Collections.emptyList());

        ResponseEntity<Page<?>> response = ticketController.getAllTickets(0, 10, "all");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllTickets_Active() {
        Page<?> page = new PageImpl<>(Collections.emptyList());

        ResponseEntity<Page<?>> response = ticketController.getAllTickets(0, 10, "active");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllTickets_Desactivated() {
        Page<?> page = new PageImpl<>(Collections.emptyList());

        ResponseEntity<Page<?>> response = ticketController.getAllTickets(0, 10, "desactivated");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllTickets_InvalidStatus() {
        ResponseEntity<Page<?>> response = ticketController.getAllTickets(0, 10, "invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testFindByID_TicketFound() {
        when(ticketService.getTicketById(1)).thenReturn(ticketModel);

        ResponseEntity<?> response = ticketController.findByID(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketModel, response.getBody());
    }

    @Test
    void testFindByID_TicketNotFound() {
        when(ticketService.getTicketById(1)).thenReturn(null);

        ResponseEntity<?> response = ticketController.findByID(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Ticket not found with id: 1", ((Mensaje) response.getBody()).getMensaje());
    }

   

    @Test
    void testDeleteTicket_TicketFound() {
        when(ticketService.getTicketById(1)).thenReturn(ticketModel);

        ResponseEntity<?> response = ticketController.deleteTicket(1, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ticket #1 Deleted(Desactivated)", ((Mensaje) response.getBody()).getMensaje());
        verify(ticketService, times(1)).deleteTicketLogictById(1, true);
    }

    @Test
    void testDeleteTicket_TicketNotFound() {
        when(ticketService.getTicketById(1)).thenReturn(null);

        ResponseEntity<?> response = ticketController.deleteTicket(1, true);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Ticket not found with id: 1", ((Mensaje) response.getBody()).getMensaje());
    }

}
