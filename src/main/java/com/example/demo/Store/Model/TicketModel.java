package com.example.demo.Store.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class TicketModel {

	private int id;
	private LocalDateTime created;
	private LocalDateTime modified;
    private boolean isActive;
	private int amount;
	private double price;
	private double total;
	private int idProduct;
	private int idUser;
	private String nameUser;
	private String nameProduct;

}
