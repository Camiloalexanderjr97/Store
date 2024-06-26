package com.example.demo.Store.ServiceImpl;

import com.example.demo.Store.Converter.TicketConverter;
import com.example.demo.Store.Entity.Product;
import com.example.demo.Store.Entity.Ticket;
import com.example.demo.Store.Model.TicketModel;
import com.example.demo.Store.Repository.ProductRepository;
import com.example.demo.Store.Repository.TicketRepository;
import com.example.demo.Store.Service.TicketService;
import com.example.demo.User.Entity.User;
import com.example.demo.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private TicketConverter ticketConverter;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserService userRepository;

	@Override
	public TicketModel createTicket(TicketModel ticket) {
		try {
			Ticket t = ticketConverter.modelToEntidy(ticket);
			t.setActive(true);
			t.setCreated(LocalDateTime.now());
			t.setModified(LocalDateTime.now());
			User user = userRepository.findById(ticket.getIdUser());

			t.setUser(user);
			Product product = productRepository.findById(ticket.getIdProduct()).get();
			t.setPrice(product.getPrice());
			t.setProduct(product);
			t.setTotal(t.getPrice() * t.getAmount());

			return ticketConverter.entityToModel(ticketRepository.save(t));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			return null;
		}

	}

	@Override
	public Page<TicketModel> getTickets(Pageable pageable) {
        Page<Ticket> tickets = ticketRepository.findAll(pageable);
        return tickets.map(ticketConverter::entityToModel);
    }

	@Override
	public TicketModel getTicketById(int id) {
		return ticketConverter.entityToModel(ticketRepository.findById(id).get());
	}

	@Override
	public boolean deleteTicketLogictById(int id, boolean status) {
		boolean resultado = false;
		try {
			if (ticketRepository.updateTicketSetStatusForNameNative(id, status,LocalDateTime.now()) != 1)
				resultado = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return resultado;
	}

	@Override
	public int editarTicketModel(TicketModel ticketModel) {
		try {
			Product product = productRepository.getById(ticketModel.getIdProduct());

			ticketModel.setTotal(ticketModel.getAmount()*product.getPrice());
			ticketRepository.updateTicketUser(ticketModel.getId(), ticketModel.getIdUser(), LocalDateTime.now());

			return ticketRepository.updateTicketProduct(ticketModel.getId(),ticketModel.getAmount(),ticketModel.getPrice(),LocalDateTime.now());




			
		} catch (Exception e) {
			System.out.println(e);
			return 0;
		}
	}

	@Override
	public Page<TicketModel> getTicketsActivated(Pageable pageable) {
		Page<Ticket> tickets = ticketRepository.findAllActiveTickets(pageable);
        return tickets.map(ticketConverter::entityToModel);
    }

	@Override
	public Page<TicketModel> getTicketsDesactivated(Pageable pageable) {
		Page<Ticket> tickets = ticketRepository.findAllDesactivedTickets(pageable);
        return tickets.map(ticketConverter::entityToModel);
    }

	
}