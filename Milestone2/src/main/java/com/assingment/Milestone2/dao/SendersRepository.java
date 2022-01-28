package com.assingment.Milestone2.dao;

import com.assingment.Milestone2.model.Senders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SendersRepository extends JpaRepository<Senders, String> {



    @Query(value = "select * from senders,receivers where senders.user_phonenumber=:userPhonenumber or receivers.user_phonenumber=:userPhonenumber", nativeQuery = true)
    public List<Senders> getByUserPhonenumber(String userPhonenumber);
}
