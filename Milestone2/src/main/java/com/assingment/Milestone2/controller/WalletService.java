package com.assingment.Milestone2.controller;

import com.assingment.Milestone2.dao.WalletRepository;
import com.assingment.Milestone2.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class WalletService {

    @Autowired
    private WalletRepository repo;

    public List<Wallet> listAll() {
        return repo.findAll();
    }

    //Function for saving the wallet
    public void save(Wallet wallet) {
        repo.save(wallet);
    }

    //Function for finding the wallet by id
    public Wallet get(String walletname) {
        return repo.findById(String.valueOf(walletname)).get();
    }

    //Function for deleting the wallet
    public void delete(String walletname) {
        repo.deleteById(walletname);
    }

    public boolean checkforDuplicateEntry(Wallet wallet){
        List<Wallet> list=repo.findByMobilenumber(wallet.getMobilenumber());
        return !list.isEmpty();
    }
    public boolean checkForSufficientAmount(String payer_mobile_number,String amount){
        return (Integer.valueOf(amount) > Integer.valueOf(repo.getByAmount(payer_mobile_number)));
       // return true;
    }



}


