package com.assingment.Milestone2.controller;

import com.assingment.Milestone2.model.Transaction;
import com.assingment.Milestone2.model.Wallet;
import com.assingment.Milestone2.service.Producer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class KafkaController {

	@Autowired
	Producer producer;
	@Autowired
	private WalletService service;

	@RequestMapping(value="/post",method = RequestMethod.GET)
	public void sendMessage(@RequestParam("mobilenumber") String msg) {
		producer.publishToTopic(msg);
	}
	//

	@GetMapping("/wallet")
	public List<Wallet> list() {
		return service.listAll_wallet();
	}

	@GetMapping("/wallet/{id}")
	public ResponseEntity<Wallet> get(@PathVariable String id) {
		try {
			Wallet user = service.get_wallet(id);
			return new ResponseEntity<Wallet>(user, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Wallet>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/create_wallet")
	public String add(@RequestBody Wallet wallet) {



		//displaying if user makes invalid entry then simply exit with message
		//will be done by testcases




		//displaying if user make duplicate entry
		if (service.checkforDuplicateEntry(wallet))
			return "wallet id already present";
		producer.publishToTopic2("User created");

		service.save_wallet(wallet);
			return "Success : "+wallet;

	}

	@PutMapping("/wallet/{id}")
	public ResponseEntity<?> update(@RequestBody Wallet wallet, @PathVariable String id) {
		try {
			Wallet existProduct = service.get_wallet(id);
			if(id.toString().equalsIgnoreCase(wallet.getMobilenumber().toString()))
				throw new NumberFormatException();

			service.save_wallet(wallet);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (NumberFormatException e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/wallet/{id}")
	public void delete(@PathVariable String id) {
		service.delete(id);
	}

	@PostMapping("/transaction")
	public String transfer(@RequestBody ObjectNode ndj) {

		String payer_phone_number = ndj.get("payer_phone_number").asText();
		String payee_phone_number = ndj.get("payee_phone_number").asText();
		String amount=ndj.get("amount").asText();

		if(service.checkForSufficientAmount(payer_phone_number,amount))
			return "Insufficient Funds";

		deductAmount(payer_phone_number,amount); //decing amount of payer
		addAmount(payee_phone_number,amount);   //adding amount to payee

		long T_ID= System.currentTimeMillis();
		String temp="Transaction Success. \n Transaction ID:"+T_ID;

		//saving transcation
		saveToTransaction(payer_phone_number,payee_phone_number,amount,T_ID);
		//pushing event to Kafka
		producer.publishToTopic(temp);
		return temp;


	}

	private void saveToTransaction(String payer,String payee,String amount,long T_D) {
		Transaction transaction=new Transaction();
		transaction.setAmount(amount);
		transaction.setPayment_from_mobilenumber(payer);
		transaction.setPayment_to_mobilenumber(payee);
		transaction.setTranx_id(String.valueOf(T_D));
		transaction.setStatus("Success");
		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		transaction.setDate(timestamp);
		service.save_transaction(transaction);

	}

	@RequestMapping(value="/transaction",method = RequestMethod.GET)
	public List<Transaction> sendTranscation(@RequestParam("mobilenumber") String mobilenumber)
	{

		return service.getAll_transaction(mobilenumber);
	}

	public void deductAmount(String mobilenumber,String amount)
	{
		Wallet wallet= service.get_wallet(mobilenumber);
		wallet.setAmount(String.valueOf(Integer.parseInt(wallet.getAmount()) - Integer.parseInt(amount)));
		service.save_wallet(wallet);

	}
	public void addAmount(String mobilenumber,String amount)
	{
		Wallet wallet= service.get_wallet(mobilenumber);
		wallet.setAmount(String.valueOf(Integer.parseInt(wallet.getAmount()) + Integer.parseInt(amount)));
		service.save_wallet(wallet);
	}

}
