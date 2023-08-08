package com.authentication.auth.controller;


import com.authentication.auth.constant.MessageStatus;
import com.authentication.auth.dto.AuthRequest;
import com.authentication.auth.dto.Message;
import com.authentication.auth.model.User;
import com.authentication.auth.service.AuthService;
import com.authentication.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    public AuthService authService;

    @Autowired
    public JwtService jwtService;

    @Autowired
    public AuthenticationManager authenticationManager;

    @GetMapping("/hello")
    public ResponseEntity<Message> sayHello()
    {
        Message message=new Message(MessageStatus.SUCCESS,"Hello world...");
        return ResponseEntity.ok(message);
    }

    @PostMapping("/add")
    public ResponseEntity<Message> addUser(@RequestBody User user)
    {
        Message response= authService.addUser(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/secure")
    public ResponseEntity<Message> secureTest()
    {
        Message message=new Message(MessageStatus.SUCCESS,"Secured endpoint accessed");
        return ResponseEntity.ok(message);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Message> authenticate(@RequestBody AuthRequest authRequest)
    {

        Authentication authentication=null;
        try
        {
             authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),authRequest.getPassword()));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        if(authentication!=null && authentication.isAuthenticated())
        {
            String jwt=jwtService.generateToken(authRequest.getEmail());
            Message response=new Message(MessageStatus.SUCCESS,jwt);
            return ResponseEntity.ok(response);
        }

            Message response=new Message(MessageStatus.FAILURE,"Invalid username or password");
            return ResponseEntity.badRequest().body(response);

    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> fetchAll()
    {
        List<User> users=authService.allUser();
        return ResponseEntity.ok(users);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Message> deleteUser(@PathVariable int id)
    {
        Message message;
       if(authService.deleteUser(id))
       {
           message=new Message(MessageStatus.SUCCESS,"Deleted successfully");
       }
       else
       {
           message=new Message(MessageStatus.FAILURE,"Unable to delete");
       }

       return ResponseEntity.ok(message);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<User>> findUser(@PathVariable int id)
    {
        Optional<User> user=authService.findUser(id);
        return ResponseEntity.ok(user);
    }
}
