package com.assignments.milestone_one.database;

import com.assignments.milestone_one.database.UserRepository;
import com.assignments.milestone_one.modelclass.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository repo;

    public List<User> listAll() {
        return repo.findAll();
    }

    //Function for saving the user
    public void save(User user) {
        repo.save(user);
    }

    //Function for finding the user by id
    public User get(String username) {
        return repo.findById(username).get();
    }

    //Function for deleting the user
    public void delete(String username) {
        repo.deleteById(username);
    }

    public boolean checkforDuplicateEntry(User user){
        return repo.existsById(user.getUsername()) || repo.existsById(user.getEmail());
    }



}


