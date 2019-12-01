package com.anuragroy.controller;

import com.anuragroy.config.TokenProvider;
import com.anuragroy.dto.AuthToken;
import com.anuragroy.dto.LoginUserDto;
import com.anuragroy.dto.UserDto;
import com.anuragroy.model.User;
import com.anuragroy.service.UserService;
import com.anuragroy.utils.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(API.login)
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping(API.authenticate)
    public ResponseEntity<?> authenticateUser(@RequestBody LoginUserDto loginUser) throws Exception {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUser.getUsername(),
                            loginUser.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(authentication);
            return ResponseEntity.ok(new AuthToken(token));
        }catch (BadCredentialsException e) {
                throw new Exception("Incorrect username or password", e);
        }
    }

    @PostMapping(API.signUp)
    public User saveUser(@RequestBody UserDto user){
        try{
            return userService.save(user);
        }catch(Exception e){
//            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(API.getUsers)
    public ResponseEntity<List<User>> listUsers(){
        try {
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping(API.getUser)
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id){
        try {
            return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping(API.hello)
    public String getMessage(){
            try {
                return "Welcome";
            }catch(Exception e){
                e.printStackTrace();
                return "error";
            }
        }

}
