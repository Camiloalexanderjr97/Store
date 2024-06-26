package com.example.demo.Controller_jwt;

import com.example.demo.User.Entity.Rol;
import com.example.demo.User.Entity.User;
import com.example.demo.User.Jwt.JwtProvider;
import com.example.demo.User.Login.RolName;
import com.example.demo.User.Service.RolService;
import com.example.demo.User.Service.UserService;
import com.example.demo.User.Util.ValidEmail;
import com.example.demo.User.dto.JwtDto;
import com.example.demo.User.dto.Mensaje;
import com.example.demo.User.dto.NewUser;
import com.example.demo.User.dto.loginUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@CrossOrigin
@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	RolService rolService;

	@Autowired
	JwtProvider jwtProvider;

	@Autowired
	private UserService userService;

	@Autowired
	private ValidEmail validEmail;

    private static final Log LOG = LogFactory.getLog(UserController.class);

    @PostMapping("/new")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NewUser nuevoUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new Mensaje("Misplaced or invalid fields",null));
        }

        if (!validEmail.isValidEmail(nuevoUser.getMail())) {
            return ResponseEntity.badRequest().body(new Mensaje("Mail invalid",null));
        }

        if (userService.loadUserByMail(nuevoUser.getMail())) {
            return ResponseEntity.badRequest().body(new Mensaje("That Mail already exists",null));
        }

        if (userService.loadUserByUsername(nuevoUser.getUsername())) {
            return ResponseEntity.badRequest().body(new Mensaje("That username already exists",null));
        }

        try {
            User user = createUserFromNewUser(nuevoUser);
           user= userService.crearUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Mensaje("User Save",user));
        } catch (Exception e) {
            LOG.error("Error saving user", e);
            return ResponseEntity.badRequest().body(new Mensaje("Fail Saving User",null));
        }
    }

    private User createUserFromNewUser(NewUser nuevoUser) {
        User user = new User(nuevoUser.getName(), nuevoUser.getUsername(), passwordEncoder.encode(nuevoUser.getPassword()));
        user.setMail(nuevoUser.getMail());

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getRolByName(RolName.ROLE_USER).orElseThrow(() -> new RuntimeException("Role not found: ROLE_USER")));

        if ("admin".equalsIgnoreCase(nuevoUser.getRol())) {
            roles.add(rolService.getRolByName(RolName.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Role not found: ROLE_ADMIN")));
        }
        user.setRoles(roles);

        return user;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody loginUser login, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new Mensaje("Misplaced or invalid fields",null));
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtProvider.generateToken(auth);
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        userService.setTokenBd(jwt, login.getUsername());

        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return ResponseEntity.ok(jwtDto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> listUsers = userService.getUsers();
            return ResponseEntity.ok(listUsers);
        } catch (HibernateException e) {
            LOG.error("Error fetching users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/users/find/{id}")
	public ResponseEntity<?> findByID(@PathVariable int id) {

        try {
            User user = userService.findById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("User not found with id: "+id,null));
            }
            return ResponseEntity.ok(user);
        } catch (HibernateException e) {
            LOG.error("Error finding user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensaje("Internal Server Error",null));
        }
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {

        try {
			User user = userService.findById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("User not found with id: "+id,null));
            }else{
				userService.deleteUser(id);
				return ResponseEntity.ok(new Mensaje("User Deleted",null));
			}
        } catch (HibernateException e) {
            LOG.error("Error deleting user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensaje("Internal Server Error",null));
        }
    }

    @PostMapping("/users/edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable int id, @Valid @RequestBody NewUser nuevoUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new Mensaje("Misplaced or invalid fields",null));
        }
		User user = userService.findById(id);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("User not found with id: "+id,null));
		}else{
		
        user = new User(id, nuevoUser.getName(), nuevoUser.getUsername(), passwordEncoder.encode(nuevoUser.getPassword()));
        user.setMail(nuevoUser.getMail());
        

        userService.editarUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Mensaje("User Updated",userService.findById(id)));

		}
    }
}