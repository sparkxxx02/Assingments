package com.assingment.Milestone2.dao;

import com.assingment.Milestone2.model.Transaction;
import com.assingment.Milestone2.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query(value = "select * from transaction where transaction.payment_from_mobilenumber=:mobilenumber", nativeQuery = true)
    public List<Transaction> getBytranxid(String mobilenumber);

}
