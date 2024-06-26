package com.example.demo.Controller_jwt;


import com.example.demo.Store.Model.TicketModel;
import com.example.demo.Store.Service.TicketService;
import com.example.demo.User.dto.Mensaje;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin
@RestController
@RequestMapping("/auth/ticket")
public class TicketController {

	@Autowired
	private TicketService ticketService;

    private static final Log LOG = LogFactory.getLog(TicketController.class);

    @PostMapping("/new")
    public ResponseEntity<?> nuevo(@Valid @RequestBody TicketModel ticketModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new Mensaje("Misplaced or invalid fields", null));
        }

        try {

           ticketModel = ticketService.createTicket(ticketModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Mensaje("Ticket Save",ticketModel));
        } catch (Exception e) {
            LOG.error("Error saving ticket", e);
            return ResponseEntity.badRequest().body(new Mensaje("Fail Saving Ticket",null));
        }
    }



    @GetMapping("/tickets")
    public ResponseEntity<Page<?>> getAllTickets(@RequestParam int page, @RequestParam int size,@RequestParam String status) {

        try {
	        Pageable pageable = PageRequest.of(page, size);
            switch (status.toLowerCase()) {
                case "all":
                    return ResponseEntity.ok(ticketService.getTickets(pageable));
                case "active":
                    return ResponseEntity.ok(ticketService.getTicketsActivated(pageable));
                case "desactivated":
                    return ResponseEntity.ok(ticketService.getTicketsDesactivated(pageable));
                default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/find/{id}")
	public ResponseEntity<?> findByID(@PathVariable int id) {

        try {
            TicketModel ticketModel = ticketService.getTicketById(id);
            if (ticketModel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("Ticket not found with id: "+id,null));
            }
            return ResponseEntity.ok(ticketModel);
        } catch (HibernateException e) {
            LOG.error("Error finding ticket", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensaje("Internal Server Error", null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTicket(@RequestParam int id, @RequestParam boolean status) {

        try {
			TicketModel ticketModel = ticketService.getTicketById(id);
            if (ticketModel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("Ticket not found with id: "+id, null));
            }else{
				ticketService.deleteTicketLogictById(id, status);
				return ResponseEntity.ok(new Mensaje("Ticket #"+id+" Deleted(Desactivated)",null));
			}
        } catch (HibernateException e) {
            LOG.error("Error deleting ticket", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensaje("Internal Server Error",null));
        }
    }

    @PostMapping("/tickets/edit/{id}")
    public ResponseEntity<?> editTicket(@PathVariable int id, @Valid @RequestBody TicketModel ticketModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new Mensaje("Misplaced or invalid fields",null));
        }
		TicketModel model = ticketService.getTicketById(id);
		if (model == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("Ticket not found with id: "+id,null));
		}else{
        ticketService.editarTicketModel(ticketModel);
        model = ticketService.getTicketById(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Mensaje("Ticket #"+id+"Updated",model));

		}
    }
}