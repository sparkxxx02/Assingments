package com.assignments.milestone_one.database;
import com.assignments.milestone_one.modelclass.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {

    public List<User> findByEmailOrMobilenumber(String email,String mobilenumber);

}
