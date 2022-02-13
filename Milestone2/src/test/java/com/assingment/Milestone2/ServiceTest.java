package com.assingment.Milestone2;

import com.assingment.Milestone2.dao.ReceiversRepository;
import com.assingment.Milestone2.dao.SendersRepository;
import com.assingment.Milestone2.dao.TransactionRepository;
import com.assingment.Milestone2.dao.WalletRepository;
import com.assingment.Milestone2.dto.TransactionSummaryDataTransferObject;
import com.assingment.Milestone2.dto.WalletDataTransferObject;
import com.assingment.Milestone2.model.Receivers;
import com.assingment.Milestone2.model.Senders;
import com.assingment.Milestone2.model.Transaction_summary;
import com.assingment.Milestone2.model.Wallet;
import com.assingment.Milestone2.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private WalletService service;

    @MockBean
    private WalletRepository walletRepo;
    @MockBean
    private SendersRepository senderRepo;
    @MockBean
    private ReceiversRepository receiversRepo;

    @MockBean
    private TransactionRepository txnRepo;


    private static Wallet wallet;
    private static Transaction_summary txn_entity;

    @BeforeEach
    void create() throws IOException {
        String userReq = "src/test/resources/CreateWallet.json";
        String requestJSON = new String(Files.readAllBytes(Paths.get(userReq)));
        wallet = new ObjectMapper().readValue(requestJSON, Wallet.class);
    }

    @Test
    void testSaveUser() {
        Mockito.when(walletRepo.save(wallet)).thenReturn(wallet);
        assertEquals(wallet,service.save_wallet2(wallet));
    }

    @Test
    void testListAllTransaction() throws IOException {

        String recReq = "src/test/resources/ReceiversTest.json";
        String requestJSON = new String(Files.readAllBytes(Paths.get(recReq)));
        Receivers rec = new ObjectMapper().readValue(requestJSON, Receivers.class);

        String sendReq = "src/test/resources/SendersTest.json";
        requestJSON = new String(Files.readAllBytes(Paths.get(sendReq)));
        Senders send = new ObjectMapper().readValue(requestJSON, Senders.class);
        List<Receivers> rec_list = new ArrayList<>();
        rec_list.add(rec);
        List<Senders> send_list= new ArrayList<>();
        send_list.add(send);

        List<List<?>> tranxlist = new ArrayList<>();
        tranxlist.add(rec_list);
        tranxlist.add(send_list);

        String mobileTest="8791615604";
        Mockito.when(senderRepo.getByUserPhonenumber(mobileTest)).thenReturn(send_list);
        Mockito.when(receiversRepo.getByUserPhonenumber(mobileTest)).thenReturn(rec_list);
        Mockito.when(service.get_All_transactions(mobileTest)).thenReturn(tranxlist);

        assertEquals(tranxlist.size(),service.get_All_transactions(mobileTest).size());
        }


    @Test
    void getTxnById() throws IOException {

        String userReq = "src/test/resources/TransactionReqSummary.json";
        String requestJSON = new String(Files.readAllBytes(Paths.get(userReq)));
        txn_entity = new ObjectMapper().readValue(requestJSON, Transaction_summary.class);

        // Way1 by DTO
        TransactionSummaryDataTransferObject txn_dto =service.convertEntityToDto_transaction(txn_entity);
        List<TransactionSummaryDataTransferObject> txn_dtoList=new ArrayList<>();
        txn_dtoList.add(txn_dto);

        Mockito.when(txnRepo.getListBytranxid(txn_entity.getTranx_id())).thenReturn(service.convertDtoToEntity_transaction(txn_dto));
        assertEquals(txn_dtoList,service.get_currentTransaction("001"));

        /*
        //Way2 by Enitity
        Mockito.when(txnRepo.findByTxnId("001")).thenReturn(txn_entity);
        assertEquals(txn_dto,service.getTransaction("001"));
        */


    }

    @Test
    void listAll_wallet() {
        Wallet wallet2=new Wallet();
        wallet2.setMobilenumber("8791615604");
        wallet2.setAmount("100");
        List<Wallet> walletList=new ArrayList<>();
        walletList.add(wallet);
        walletList.add(wallet2);

        Mockito.when(walletRepo.findAll()).thenReturn(walletList);
        assertEquals(2,service.listAll_wallet().size());
    }

    @Test
    void get_wallet() {
        WalletDataTransferObject walletDto= service.convertEntityToDto_wallet(wallet);
        Mockito.when(walletRepo.findByMobilenumber(wallet.getMobilenumber())).thenReturn(wallet);
        assertEquals(walletDto,service.get_wallet(wallet.getMobilenumber()));
    }


}
