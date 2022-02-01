package com.assingment.Milestone2.service;

import com.assingment.Milestone2.dao.ReceiversRepository;
import com.assingment.Milestone2.dao.SendersRepository;
import com.assingment.Milestone2.dao.TransactionRepository;
import com.assingment.Milestone2.dao.WalletRepository;
import com.assingment.Milestone2.dto.ReceiversDataTransferObject;
import com.assingment.Milestone2.dto.SendersDataTransferObject;
import com.assingment.Milestone2.dto.TransactionSummaryDataTransferObject;
import com.assingment.Milestone2.dto.WalletDataTransferObject;
import com.assingment.Milestone2.model.Receivers;
import com.assingment.Milestone2.model.Senders;
import com.assingment.Milestone2.model.Transaction_summary;
import com.assingment.Milestone2.model.Wallet;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class WalletService implements UserDetailsService {


    @Autowired
    private WalletRepository wallet_repo;
    @Autowired
    private TransactionRepository trans_repo;
    @Autowired
    private SendersRepository sendersRepository;
    @Autowired
    private ReceiversRepository receiversRepository;



    @Autowired
    private ModelMapper modelMapper;


    private WalletDataTransferObject convertEntityToDto_wallet(Wallet wallet){
        return modelMapper.map(wallet,WalletDataTransferObject.class);
    }
    private Wallet convertDtoToEntity_wallet(WalletDataTransferObject walletDataTransferObject){
        return modelMapper.map(walletDataTransferObject,Wallet.class);
    }
    private TransactionSummaryDataTransferObject convertEntityToDto_transaction(Transaction_summary transaction_summary){
        return  modelMapper.map(transaction_summary,TransactionSummaryDataTransferObject.class);
    }
    private Transaction_summary convertDtoToEntity_transaction(TransactionSummaryDataTransferObject transactionSummaryDataTransferObject){
        return modelMapper.map(transactionSummaryDataTransferObject,Transaction_summary.class);
    }
    private ReceiversDataTransferObject convertEntityToDto_receivers(Receivers receivers){
        return modelMapper.map(receivers,ReceiversDataTransferObject.class);
    }
    private Receivers convertDtoToEntity_receivers(ReceiversDataTransferObject receiversDataTransferObject){
        return modelMapper.map(receiversDataTransferObject,Receivers.class);
    }
    private SendersDataTransferObject convertEntityToDto_senders(Senders senders){
        return modelMapper.map(senders,SendersDataTransferObject.class);
    }
    private Senders convertDtoToEntity_senders(SendersDataTransferObject sendersDataTransferObject){
        return modelMapper.map(sendersDataTransferObject,Senders.class);
    }

    public List<WalletDataTransferObject> listAll_wallet() {
        return wallet_repo.findAll()
                .stream()
                .map(this::convertEntityToDto_wallet)
                .collect(Collectors.toList());
    }
    public List<TransactionSummaryDataTransferObject> get_currentTransaction(String tranx_id) {

        return trans_repo.getBytranxid(tranx_id)
                .stream()
                .map(this::convertEntityToDto_transaction)
                .collect(Collectors.toList());
    }

    public List<List<?>> get_All_transactions(String mobilenumber) {
        List<Senders> sendersList=  sendersRepository.getByUserPhonenumber(mobilenumber);
        List<Receivers> receiversList= receiversRepository.getByUserPhonenumber(mobilenumber);

        List<List<?>> list = new ArrayList<>();
        list.add(sendersList);
        list.add(receiversList);
        return list;
    }

    //Function for saving the wallet
    public void save_wallet(WalletDataTransferObject walletDTO) {
        Wallet wallet= convertDtoToEntity_wallet(walletDTO);
        wallet_repo.save(wallet);
    }

    //Function for finding the wallet by id
    public WalletDataTransferObject get_wallet(String walletname) {
        Wallet temp= wallet_repo.findById(walletname).get();
        return convertEntityToDto_wallet(temp);
    }

    //Function for deleting the wallet
    public void delete(String walletname) {
        wallet_repo.deleteById(walletname);
    }

    public boolean checkforDuplicateEntry(WalletDataTransferObject walletDTO){
        List<Wallet> list=wallet_repo.findByMobilenumber(walletDTO.getMobilenumber());
        return !list.isEmpty();
    }
    public boolean checkForSufficientAmount(String payer_mobile_number,String amount){
        return (Integer.parseInt(amount) > Integer.parseInt(wallet_repo.getByAmount(payer_mobile_number)));
       // return true;
    }

    public TransactionSummaryDataTransferObject getTransaction(String transactionid)
    {
        return convertEntityToDto_transaction(trans_repo.findById(transactionid).get());
    }

    public void save_transaction(TransactionSummaryDataTransferObject transactionSummaryDataTransferObject) {
        trans_repo.save(convertDtoToEntity_transaction(transactionSummaryDataTransferObject));
    }

    public boolean containsLetters(String str)
        {
            String regex = "[0-9]+";
            Pattern p = Pattern.compile(regex);

            // If the string is empty
            // return false
            if (str == null) {
                return false;
            }

            // Find match between str and amount and regular expression
            Matcher m = p.matcher(str);

            // Return if the string matched the ReGex
            return !m.matches() ;
        }

    public void SavingToDbSender(SendersDataTransferObject sendersDTO) {

        sendersRepository.save(convertDtoToEntity_senders(sendersDTO));

    }
    public void SavingToDbReceivers(ReceiversDataTransferObject receiversDTO) {
        receiversRepository.save(convertDtoToEntity_receivers(receiversDTO));
    }

    public void SaveToSender(String payer, String payee, String amount,long T_D) {
        SendersDataTransferObject sendersDTO=new SendersDataTransferObject();
        sendersDTO.setAmount_sent(amount);
        String timestamp = String.valueOf(Instant.ofEpochMilli(T_D)
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
        sendersDTO.setCreated_timestamp(timestamp);
        sendersDTO.setModified_timestamp(timestamp);
        sendersDTO.setUser_phonenumber(payer);
        sendersDTO.setSent_to(payee);
        sendersDTO.setTransaction_id(String.valueOf(T_D));
        SavingToDbSender(sendersDTO);
    }
    public void SaveToReceiver(String payer, String payee, String amount,long T_D) {
        ReceiversDataTransferObject receiversDTO=new ReceiversDataTransferObject();
        receiversDTO.setAmount_received(amount);
        String timestamp = String.valueOf(Instant.ofEpochMilli(T_D)
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
        receiversDTO.setCreated_timestamp(timestamp);
        receiversDTO.setModified_timestamp(timestamp);
        receiversDTO.setUser_phonenumber(payee);
        receiversDTO.setReceived_from(payer);
        receiversDTO.setTransaction_id(String.valueOf(T_D));
        SavingToDbReceivers(receiversDTO);
    }
    public void deductAmount(String mobilenumber,String amount)
    {
        WalletDataTransferObject walletDTO= get_wallet(mobilenumber);
        walletDTO.setAmount(String.valueOf(Integer.parseInt(walletDTO.getAmount()) - Integer.parseInt(amount)));
        save_wallet(walletDTO);

    }
    public void addAmount(String mobilenumber,String amount)
    {
        WalletDataTransferObject walletDTO= get_wallet(mobilenumber);
        walletDTO.setAmount(String.valueOf(Integer.parseInt(walletDTO.getAmount()) + Integer.parseInt(amount)));
        save_wallet(walletDTO);
    }
    public void saveToTransactionSummary(String payer, String payee, String amount, long T_D) {
        TransactionSummaryDataTransferObject transactionSummaryDTO =new TransactionSummaryDataTransferObject();
        transactionSummaryDTO.setAmount(amount);
        transactionSummaryDTO.setPayment_from_mobilenumber(payer);
        transactionSummaryDTO.setPayment_to_mobilenumber(payee);
        transactionSummaryDTO.setTranx_id(String.valueOf(T_D));
        transactionSummaryDTO.setStatus("Success");
        String timestamp = String.valueOf(Instant.ofEpochMilli(T_D)
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
        transactionSummaryDTO.setDate(timestamp);
        save_transaction(transactionSummaryDTO);


        SaveToSender(payer,payee,amount,T_D);
        SaveToReceiver(payer,payee,amount,T_D);

    }



    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        //Logic to get the user form the Database

        return new User("admin","password",new ArrayList<>());
    }
}


