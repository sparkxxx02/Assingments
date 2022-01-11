package com.assingment.milestone1.DatabaseFiles;

import java.util.List;

import javax.transaction.Transactional;

import com.assingment.milestone1.Dataclass.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository repo;

    public List<User> listAll() {
        return repo.findAll();
    }

    public void save(User user) {
        repo.save(user);
    }

    public User get(Integer userID) {
        return repo.findById(userID).get();
    }

    public void delete(Integer userID) {
        repo.deleteById(userID);
    }
}
