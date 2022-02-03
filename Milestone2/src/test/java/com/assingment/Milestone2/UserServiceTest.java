package com.assingment.Milestone2;

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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private WalletService service;

    @MockBean
    private WalletRepository walletRepo;

    @MockBean
    private TransactionRepository txnRepo;

    private static Wallet wallet;

    @BeforeEach
    void create() throws IOException {
        String userReq = "src/test/resources/UserReq.json";
        String requestJSON = new String(Files.readAllBytes(Paths.get(userReq)));
        wallet = new ObjectMapper().readValue(requestJSON, Wallet.class);
    }

    @Test
    void testSaveUser() {
        Mockito.when(walletRepo.save(wallet)).thenReturn(wallet);
        assertEquals(wallet,service.save_wallet(service.convertEntityToDto_wallet(wallet)));
    }

    @Test
    void testListAllTransaction() throws IOException {

        String recReq = "src/test/resources/ReceiversTest.json";
        String requestJSON = new String(Files.readAllBytes(Paths.get(recReq)));
        Receivers rec = new ObjectMapper().readValue(requestJSON, Receivers.class);

        String sendReq = "src/test/resources/SendersTest.json";
        requestJSON = new String(Files.readAllBytes(Paths.get(sendReq)));
        Senders send = new ObjectMapper().readValue(requestJSON, Senders.class);
        List<Receivers> rec_list = null;
        rec_list.add(rec);
        List<Senders> send_list;

        List<List<?>> tranxlist = new ArrayList<>();
        tranxlist.add(wallet);
        tranxlist.add(wallet2);

        Mockito.when(walletRepo.findAll()).thenReturn(tranxlist);
        assertEquals(tranxlist,service.get());
    }


    @Test
    void testTxnById() {

        TransactionSummaryDataTransferObject txn = new TransactionSummaryDataTransferObject();
        txn.setTranx_id("001");
        txn.setStatus("Success");
        txn.setAmount("100");
        txn.setDate(String.valueOf(new Date()));
        txn.setPayment_from_mobilenumber("9027256094");
        txn.setPayment_to_mobilenumber("8791615604");
        Mockito.when(txnRepo.findByTxnId("001")).thenReturn(service.convertDtoToEntity_transaction(txn));
        assertEquals(txn,service.getTransaction("001"));
    }

}
