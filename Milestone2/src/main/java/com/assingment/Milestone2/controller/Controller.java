package com.assingment.Milestone2.controller;

import com.assingment.Milestone2.dto.TransactionSummaryDataTransferObject;
import com.assingment.Milestone2.dto.WalletDataTransferObject;
import com.assingment.Milestone2.Kafka.Producer;
import com.assingment.Milestone2.service.WalletService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class Controller {

	public static String timestamp;

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
	public List<WalletDataTransferObject> list() {
		return service.listAll_wallet();
	}

	@GetMapping("/wallet/{id}")
	public ResponseEntity<WalletDataTransferObject> get(@PathVariable String id) {
		try {
			WalletDataTransferObject user = service.get_wallet(id);

			return new ResponseEntity<WalletDataTransferObject>(user, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<WalletDataTransferObject>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/create_wallet")
	public ResponseEntity<String> add(@RequestBody WalletDataTransferObject walletDTO) {



		//displaying if user makes invalid entry then simply exit with message
		if(service.containsLetters(walletDTO.getMobilenumber()) ||
				service.containsLetters(walletDTO.getAmount()))
			return new ResponseEntity<String>("Enter correct details",HttpStatus.BAD_REQUEST);


		//displaying if user make duplicate entry
		if (service.checkforDuplicateEntry(walletDTO))
			return new ResponseEntity<String>("Wallet already present",HttpStatus.MULTI_STATUS);
		producer.publishToTopic2("User created");

		service.save_wallet(walletDTO);
		return new ResponseEntity<String>("Success with wallet number-"+walletDTO.getMobilenumber(),HttpStatus.ACCEPTED);

	}

	@PutMapping("/wallet/{id}")
	public ResponseEntity<?> update(@RequestBody WalletDataTransferObject walletDTO, @PathVariable String id) {
		try {
			WalletDataTransferObject existProduct = service.get_wallet(id);
			if(id.toString().equalsIgnoreCase(walletDTO.getMobilenumber().toString()))
				throw new NumberFormatException();

			service.save_wallet(walletDTO);
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
	public ResponseEntity<String> transfer(@RequestBody ObjectNode ndj) {

		String payer_phone_number = ndj.get("payer_phone_number").asText();
		String payee_phone_number = ndj.get("payee_phone_number").asText();
		String amount=ndj.get("amount").asText();

		//displaying if user makes invalid entry then simply exit with message
		if(service.containsLetters(payer_phone_number)
				|| service.containsLetters(payee_phone_number)
				|| service.containsLetters(amount))
			return new ResponseEntity<String>("Enter correct details",HttpStatus.BAD_REQUEST);

		//checking funds
		if(service.checkForSufficientAmount(payer_phone_number,amount))
			return new ResponseEntity<String>("Insufficient Funds",HttpStatus.BAD_REQUEST);

		service.deductAmount(payer_phone_number,amount); //deducting amount of payer
		service.addAmount(payee_phone_number,amount);   //adding amount to payee

		long t_d=Instant.now().toEpochMilli();
		timestamp = String.valueOf(t_d);
		String temp="Transaction Success\nTransaction ID:"+t_d;

		//saving transcation
		service.saveToTransaction(payer_phone_number,payee_phone_number,amount,t_d);

		//pushing event to Kafka
		producer.publishToTopic(temp);

		return new ResponseEntity<String>(temp,HttpStatus.ACCEPTED);


	}




	@RequestMapping(value="/transaction",method = RequestMethod.GET)
	public List<TransactionSummaryDataTransferObject> sendTransaction(@RequestParam("mobilenumber") String mobilenumber)
	{

		return service.getAll_transaction(mobilenumber);
	}


}
