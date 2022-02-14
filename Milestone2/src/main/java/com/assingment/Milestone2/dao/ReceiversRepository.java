package com.assingment.Milestone2.dao;

import com.assingment.Milestone2.model.Receivers;
import com.assingment.Milestone2.model.Senders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceiversRepository extends JpaRepository<Receivers, String> {



    @Query(value = "select * from receivers where receivers.user_phonenumber=:userPhonenumber", nativeQuery = true)
    public List<Receivers> getByUserPhonenumber(String userPhonenumber);
}
