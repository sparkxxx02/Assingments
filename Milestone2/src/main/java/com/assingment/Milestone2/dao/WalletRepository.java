package com.assingment.Milestone2.dao;

import com.assingment.Milestone2.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {

    @Query(value = "select amount from wallet where wallet.mobilenumber=:mobilenumber", nativeQuery = true)
    public String getByAmount(String mobilenumber);

    public Wallet findByMobilenumber(String mobilenumber);

}
