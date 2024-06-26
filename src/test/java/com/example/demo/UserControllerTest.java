package com.example.demo;

import com.example.demo.Controller_jwt.UserController;
import com.example.demo.User.Entity.Rol;
import com.example.demo.User.Entity.User;
import com.example.demo.User.Jwt.JwtProvider;
import com.example.demo.User.Login.RolName;
import com.example.demo.User.Service.RolService;
import com.example.demo.User.Service.UserService;
import com.example.demo.User.Util.ValidEmail;
import com.example.demo.User.dto.Mensaje;
import com.example.demo.User.dto.NewUser;
import com.example.demo.User.dto.loginUser;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RolService rolService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserService userService;

    @Mock
    private ValidEmail validEmail;

    @InjectMocks
    private UserController userController;

    private static final Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testNuevo_ValidUser() {
        NewUser newUser = new NewUser(1, "John Doe", "johndoe", "password", "johndoe@example.com", "user", null, null, null, null, null, false);

        try {
            when(ValidEmail.isValidEmail(anyString())).thenReturn(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  when(userService.loadUserByMail(anyString())).thenReturn(false);
        when(userService.loadUserByUsername(anyString())).thenReturn(false);
        when(rolService.getRolByName(RolName.ROLE_USER)).thenReturn(Optional.of(new Rol(RolName.ROLE_USER)));

        ResponseEntity<?> response = userController.nuevo(newUser, mock(BindingResult.class));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User Save", ((Mensaje) response.getBody()).getMensaje());
    }

    @Test
    public void testNuevo_ExistingEmail() {
        NewUser newUser = new NewUser(1, "John Doe", "johndoe", "password", "johndoe@example.com", "user", null, null, null, null, null, false);

        try {
            when(ValidEmail.isValidEmail(anyString())).thenReturn(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   when(userService.loadUserByMail(anyString())).thenReturn(true);

        ResponseEntity<?> response = userController.nuevo(newUser, mock(BindingResult.class));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("That Mail already exists", ((Mensaje) response.getBody()).getMensaje());
    }

    @Test
    public void testNuevo_ExistingUsername() {
        NewUser newUser = new NewUser(1, "John Doe", "johndoe", "password", "johndoe@example.com", "user", null, null, null, null, null, false);

        try {
            when(ValidEmail.isValidEmail(anyString())).thenReturn(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   when(userService.loadUserByMail(anyString())).thenReturn(false);
        when(userService.loadUserByUsername(anyString())).thenReturn(true);

        ResponseEntity<?> response = userController.nuevo(newUser, mock(BindingResult.class));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("That username already exists", ((Mensaje) response.getBody()).getMensaje());
    }

    @Test
    public void testLogin_ValidCredentials() {
        loginUser loginUser = new loginUser("johndoe","user", "password");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtProvider.generateToken(authentication)).thenReturn("generated_token");

        ResponseEntity<?> response = userController.login(loginUser, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetAllUsers() {
        List<User> userList = Collections.singletonList(new User("John Doe", "johndoe", "password"));

        when(userService.getUsers()).thenReturn(userList);

        ResponseEntity<?> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
    }

    @Test
    public void testFindById_ValidId() {
        User user = new User("John Doe", "johndoe", "password");
        user.setId(1);

        when(userService.findById(1)).thenReturn(user);

        ResponseEntity<?> response = userController.findByID(1);
                assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testDeleteUser_ValidId() {
        int userId = 1;

        ResponseEntity<?> response = userController.deleteUser(userId);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found with id: 1", ((Mensaje) response.getBody()).getMensaje());
    }

    @Test
    public void testEditUser_ValidUser() {
        NewUser newUser = new NewUser(1, "John Doe", "johndoe", "password", "admin", null, null, null, null, null, null, false);

        userService.actualizarRol(any(int.class));

        ResponseEntity<?> response = userController.editUser(1,newUser, mock(BindingResult.class));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found with id: 1", ((Mensaje) response.getBody()).getMensaje());
    }
}