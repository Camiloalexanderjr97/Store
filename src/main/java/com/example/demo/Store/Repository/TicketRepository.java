package com.example.demo.Store.Repository;


import com.example.demo.Store.Entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

	@Repository
	public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	   
		
		Page<Ticket> findAll(Pageable pageable);

	  @Modifying
	  @Transactional
	  @Query(value = "  UPDATE ticket SET ticket.is_active= :status,  ticket.modified= :modified WHERE ticket.id = :id",
	    nativeQuery = true)
	  int updateTicketSetStatusForNameNative(int id, boolean status, LocalDateTime modified);


	  @Modifying
	  @Transactional
	  @Query(value = "UPDATE ticket t SET t.user_id = :userId, t.modified = :modified WHERE t.id = :id",	    
	  nativeQuery = true)
	  int updateTicketUser(@Param("id") int id, @Param("userId") int userId, @Param("modified") LocalDateTime modified);
  
	  @Modifying
	  @Transactional
	  @Query(value = "UPDATE Ticket t SET t.amount = :amount, t.price = :price, t.modified = :modified WHERE t.id = :id",
	  nativeQuery = true)
	  int updateTicketProduct( int id, int amount, double price, LocalDateTime modified);
  

	  @Query(value = "SELECT t FROM Ticket t WHERE t.isActive = true")
	  Page<Ticket> findAllActiveTickets(Pageable pageable);
	
	  @Query(value = "SELECT t FROM Ticket t WHERE t.isActive = false")
	  Page<Ticket> findAllDesactivedTickets(Pageable pageable);
	  

	
}
