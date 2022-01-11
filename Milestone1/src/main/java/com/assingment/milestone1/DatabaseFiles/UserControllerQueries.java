package com.assingment.milestone1.DatabaseFiles;

import java.util.*;

import com.assingment.milestone1.Dataclass.User;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserControllerQueries {

    @Autowired
    private UserService service;

    @GetMapping("/user")
    public List<User> list() {
        return service.listAll();
    }

    @GetMapping("/user/{userID}")
    public ResponseEntity<User> get(@PathVariable Integer userID) {
        try {
            User user = service.get(userID);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user")
    public void add(@RequestBody User user) {
        service.save(user);
    }

    @PutMapping("/user/{userID}")
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable Integer userID) {
        try {
            User existUser = service.get(userID);
            service.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/{userID}")
    public void delete(@PathVariable Integer userID) {
        service.delete(userID);
    }

}