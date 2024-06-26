package com.example.demo.User.dto;

import com.example.demo.User.Entity.Rol;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewUser {

	@NotEmpty
	private int id;

	@NotEmpty
	private String name;

	@NotEmpty
	private String username;

	@NotEmpty
	private String password;

	
	@NotEmpty
	private String mail;

	private String rol;

	private Set<Rol> roles = new HashSet<>();

	private String token;
	private LocalDateTime created;
	private LocalDateTime modified;
	private LocalDateTime lastLogin;
	private boolean isActive;



}
