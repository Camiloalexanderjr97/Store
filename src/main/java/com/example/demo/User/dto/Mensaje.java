package com.example.demo.User.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mensaje {
	private String mensaje;
	private Object dato;

	public Mensaje(String mensaje, Object dato) {
		super();
		this.mensaje = mensaje;
		this.dato = dato;
	}


}
