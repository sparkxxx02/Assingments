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

    //Function for validating existing user with same username, email or mobile number
    public String doesExist(User user){
        boolean exists1 = repo.existsById(user.getUsername());
        if (exists1) return "username";
        List<User> exists2 = repo.findByEmailOrMobilenumber(user.getEmail(), user.getMobilenumber());
        if (exists2.size() != 0) return "contact details";
        return "none";
    }

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

    public boolean checkIfPresent(String id) {
        return repo.existsById(id);
    }


}


