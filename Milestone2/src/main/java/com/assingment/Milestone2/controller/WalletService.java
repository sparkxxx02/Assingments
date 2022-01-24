package com.assingment.Milestone2.controller;

import com.assingment.Milestone2.dao.TransactionRepository;
import com.assingment.Milestone2.dao.WalletRepository;
import com.assingment.Milestone2.model.Transaction;
import com.assingment.Milestone2.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public boolean containsLetters(String str)
        {
            // Regex to check string
            // contains only digits
            String regex = "[0-9]+";

            // Compile the ReGex
            Pattern p = Pattern.compile(regex);

            // If the string is empty
            // return false
            if (str == null) {
                return false;
            }

            // Find match between str and amount
            // and regular expression
            // using Pattern.matcher()
            Matcher m = p.matcher(str);

            // Return if the string
            // matched the ReGex
            return !m.matches() ;
        }

}


