package com.example.demo;

import com.example.demo.Store.Converter.TicketConverter;
import com.example.demo.Store.Entity.Product;
import com.example.demo.Store.Entity.Ticket;
import com.example.demo.Store.Model.TicketModel;
import com.example.demo.Store.Repository.ProductRepository;
import com.example.demo.Store.Repository.TicketRepository;
import com.example.demo.Store.Service.TicketService;
import com.example.demo.Store.ServiceImpl.TicketServiceImpl;
import com.example.demo.User.Entity.User;
import com.example.demo.User.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketConverter ticketConverter;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private TicketModel ticketModel;
    private Ticket ticket;
    private User user;
    private Product product;
    private Pageable pageable;
    private Page<Ticket> ticketPage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        ticketModel = new TicketModel();
        ticket = new Ticket();
        user = new User();
        product = new Product();
        pageable = PageRequest.of(0, 10);
        ticketPage = new PageImpl<>(List.of(ticket));
        
        ticket.setId(1);
        ticket.setActive(true);
        ticket.setCreated(LocalDateTime.now());
        ticket.setModified(LocalDateTime.now());
        ticket.setUser(user);
        ticket.setProduct(product);
        ticket.setAmount(5);
        ticket.setPrice(10.0);
        ticket.setTotal(50.0);

        product.setId(1);
        product.setStock(true);
        product.setAmount(10);
        product.setPrice(10.0);

        user.setId(1);
    }

    @Test
    void testCreateTicket_Success() {
        when(ticketConverter.modelToEntidy(ticketModel)).thenReturn(ticket);
        when(userRepository.findById(ticketModel.getIdUser())).thenReturn(user);
        when(productRepository.findById(ticketModel.getIdProduct())).thenReturn(Optional.of(product));
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(ticketConverter.entityToModel(ticket)).thenReturn(ticketModel);

        TicketModel result = ticketService.createTicket(ticketModel);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testCreateTicket_ProductOutOfStock() {
        ticketModel.setAmount(15);
        when(ticketConverter.modelToEntidy(ticketModel)).thenReturn(ticket);
        when(userRepository.findById(ticketModel.getIdUser())).thenReturn(user);
        when(productRepository.findById(ticketModel.getIdProduct())).thenReturn(Optional.of(product));

        TicketModel result = ticketService.createTicket(ticketModel);

        assertEquals(1, result.getIsValid());
        assertEquals("Required quantity out of stock", result.getMensaje());
    }

    @Test
    void testCreateTicket_ProductNotAvailable() {
        product.setStock(false);
        when(ticketConverter.modelToEntidy(ticketModel)).thenReturn(ticket);
        when(userRepository.findById(ticketModel.getIdUser())).thenReturn(user);
        when(productRepository.findById(ticketModel.getIdProduct())).thenReturn(Optional.of(product));

        TicketModel result = ticketService.createTicket(ticketModel);

        assertEquals(2, result.getIsValid());
        assertEquals("Sold out / product not available", result.getMensaje());
    }

    @Test
    void testCreateTicket_UserOrProductNotFound() {
        when(ticketConverter.modelToEntidy(ticketModel)).thenReturn(ticket);
        when(userRepository.findById(ticketModel.getIdUser())).thenReturn(null);
        when(productRepository.findById(ticketModel.getIdProduct())).thenReturn(Optional.empty());

        TicketModel result = ticketService.createTicket(ticketModel);

    }

    @Test
    void testGetTickets() {
        when(ticketRepository.findAll(pageable)).thenReturn(ticketPage);
        when(ticketConverter.entityToModel(ticket)).thenReturn(ticketModel);

        Page<TicketModel> result = ticketService.getTickets(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(ticketRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetTicketById() {
        when(ticketRepository.findById(1)).thenReturn(Optional.of(ticket));
        when(ticketConverter.entityToModel(ticket)).thenReturn(ticketModel);

        TicketModel result = ticketService.getTicketById(1);

        assertNotNull(result);
        verify(ticketRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteTicketLogictById() {
        when(ticketRepository.updateTicketSetStatusForNameNative(anyInt(), anyBoolean(), any(LocalDateTime.class)))
                .thenReturn(1);

        boolean result = ticketService.deleteTicketLogictById(1, false);

        assertFalse(result);
        verify(ticketRepository, times(1))
                .updateTicketSetStatusForNameNative(anyInt(), anyBoolean(), any(LocalDateTime.class));
    }

    @Test
    void testGetTicketsActivated() {
        when(ticketRepository.findAllActiveTickets(pageable)).thenReturn(ticketPage);
        when(ticketConverter.entityToModel(ticket)).thenReturn(ticketModel);

        Page<TicketModel> result = ticketService.getTicketsActivated(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(ticketRepository, times(1)).findAllActiveTickets(pageable);
    }

    @Test
    void testGetTicketsDesactivated() {
        when(ticketRepository.findAllDesactivedTickets(pageable)).thenReturn(ticketPage);
        when(ticketConverter.entityToModel(ticket)).thenReturn(ticketModel);

        Page<TicketModel> result = ticketService.getTicketsDesactivated(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(ticketRepository, times(1)).findAllDesactivedTickets(pageable);
    }
}
