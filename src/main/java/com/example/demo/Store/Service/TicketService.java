package com.example.demo.Store.Service;

import com.example.demo.Store.Model.TicketModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TicketService {

	  public abstract TicketModel createTicket(TicketModel ticket);

	  public abstract Page<TicketModel> getTickets(Pageable pageable);
	  public abstract Page<TicketModel> getTicketsActivated(Pageable pageable);
	  public abstract Page<TicketModel> getTicketsDesactivated(Pageable pageable);

	    public abstract TicketModel getTicketById(int id);	    
		
      public abstract boolean deleteTicketLogictById(int id, boolean status);
	   
}
