package com.example.demo.Store.Converter;

import com.example.demo.Store.Entity.Ticket;
import com.example.demo.Store.Model.TicketModel;
import org.springframework.stereotype.Service;

@Service
public class TicketConverter {

	public Ticket modelToEntidy(TicketModel model) {
		Ticket ticket = new Ticket();
		ticket.setId(model.getId());
		ticket.setCreated(model.getCreated());
		ticket.setActive(model.isActive());
		ticket.setPrice(model.getPrice());
		ticket.setAmount(model.getAmount());
		return ticket;
	}

	public TicketModel entityToModel(Ticket ticket){
		TicketModel model = new TicketModel();
		model.setId(ticket.getId());
		model.setCreated(ticket.getCreated());
		model.setModified(ticket.getModified());
		model.setActive(ticket.isActive());
		model.setPrice(ticket.getPrice());
		model.setAmount(ticket.getAmount());
		model.setNameUser(ticket.getUser().getName());
		model.setTotal(ticket.getTotal());
		model.setNameProduct(ticket.getProduct().getName());
		return model;
	}
}
