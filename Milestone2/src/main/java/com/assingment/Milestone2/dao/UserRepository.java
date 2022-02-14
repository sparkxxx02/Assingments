package com.assingment.Milestone2.dao;
import com.assingment.Milestone2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User,String> {

    public User findBymobilenumber(String phoneNumber);


}
