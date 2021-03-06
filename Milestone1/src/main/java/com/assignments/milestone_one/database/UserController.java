package com.assignments.milestone_one.database;


import com.assignments.milestone_one.modelclass.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public List<User> list() {
        return service.listAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> get(@PathVariable String id) {
        try {
            User user = service.get(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users")
    public String add(@RequestBody User user) {


       
        //displaying if user makes invalid entry then simply exit with message
        if(user.checkForInvalidEntries())
            return "Entry invalid please enter username and email";

        //adding correct details todo


        //displaying if user make duplicate entry
        if (service.checkforDuplicateEntry(user))
            return "Duplicate Entry please enter different username/email";

        //displaying which field is left empty
        String result = user.checkForBlankEntries();
        service.save(user);  //saving
        if (result.equalsIgnoreCase(""))
            return "created";
        else
            return "This field "+result+" has been left empty. Kindly update if not required \n created";

    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable String id) {

        try {
            User existProduct = service.get(id);
            service.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}


