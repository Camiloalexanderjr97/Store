package com.example.demo.Store.Entity;

import com.example.demo.User.Entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	private LocalDateTime created;
	private LocalDateTime modified;
    private boolean isActive;
	private int amount;
	private double price;
	private double total;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
	private User user;

    
    @NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinTable(name = "ticket_product", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "ticket_id"))
	private Product product;
	

    

}
