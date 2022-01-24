package com.assingment.Milestone2.controller;

import com.assingment.Milestone2.dao.TransactionRepository;
import com.assingment.Milestone2.dao.WalletRepository;
import com.assingment.Milestone2.model.Transaction;
import com.assingment.Milestone2.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class WalletService {

    @Autowired
    private WalletRepository wallet_repo;
    @Autowired
    private TransactionRepository trans_repo;


    public List<Wallet> listAll_wallet() {
        return wallet_repo.findAll();
    }
    public List<Transaction> getAll_transaction(String mobilenumber) {
        return trans_repo.getBytranxid(mobilenumber);
    }

    //Function for saving the wallet
    public void save_wallet(Wallet wallet) {
        wallet_repo.save(wallet);
    }

    //Function for finding the wallet by id
    public Wallet get_wallet(String walletname) {
        return wallet_repo.findById(walletname).get();
    }

    //Function for deleting the wallet
    public void delete(String walletname) {
        wallet_repo.deleteById(walletname);
    }

    public boolean checkforDuplicateEntry(Wallet wallet){
        List<Wallet> list=wallet_repo.findByMobilenumber(wallet.getMobilenumber());
        return !list.isEmpty();
    }
    public boolean checkForSufficientAmount(String payer_mobile_number,String amount){
        return (Integer.valueOf(amount) > Integer.valueOf(wallet_repo.getByAmount(payer_mobile_number)));
       // return true;
    }

    public Transaction getTransaction(String transactionid)
    {
        return trans_repo.findById(transactionid).get();
    }

    public void save_transaction(Transaction transactionid) {
        trans_repo.save(transactionid);
    }




}


