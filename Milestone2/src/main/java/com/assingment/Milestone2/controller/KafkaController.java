package com.assingment.Milestone2.controller;

import com.assingment.Milestone2.model.Wallet;
import com.assingment.Milestone2.service.Producer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
		return service.listAll();
	}

	@GetMapping("/wallet/{id}")
	public ResponseEntity<Wallet> get(@PathVariable String id) {
		try {
			Wallet user = service.get(id);
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

		service.save(wallet);
			return "Success : "+wallet;

	}

	@PutMapping("/wallet/{id}")
	public ResponseEntity<?> update(@RequestBody Wallet wallet, @PathVariable String id) {
		try {
			Wallet existProduct = service.get(id);
			if(id.toString().equalsIgnoreCase(wallet.getMobilenumber().toString()))
				throw new NumberFormatException();

			service.save(wallet);
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

		//pushing event to Kafka
		producer.publishToTopic(temp);
		return temp;


	}

	public void deductAmount(String mobilenumber,String amount)
	{
		Wallet wallet= service.get(mobilenumber);
		wallet.setAmount(String.valueOf(Integer.parseInt(wallet.getAmount()) - Integer.parseInt(amount)));
		service.save(wallet);

	}
	public void addAmount(String mobilenumber,String amount)
	{
		Wallet wallet= service.get(mobilenumber);
		wallet.setAmount(String.valueOf(Integer.parseInt(wallet.getAmount()) + Integer.parseInt(amount)));
		service.save(wallet);
	}

}
