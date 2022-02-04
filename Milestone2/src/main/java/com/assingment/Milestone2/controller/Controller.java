package com.assingment.Milestone2.controller;

import com.assingment.Milestone2.dto.TransactionSummaryDataTransferObject;
import com.assingment.Milestone2.dto.WalletDataTransferObject;
import com.assingment.Milestone2.Kafka.Producer;
import com.assingment.Milestone2.model.JwtRequest;
import com.assingment.Milestone2.model.JwtResponse;
import com.assingment.Milestone2.service.WalletService;
import com.assingment.Milestone2.utility.JWTUtility;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class Controller {


	@Autowired
	Producer producer;
	@Autowired
	private WalletService service;

	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private AuthenticationManager authenticationManager;

	public static long t_d;

	@RequestMapping(value="/post",method = RequestMethod.GET)
	public void sendMessage(@RequestParam("mobilenumber") String msg) {
		producer.publishToTopic(msg);
	}

	//

	@GetMapping("/wallet")
	public List<WalletDataTransferObject> list() {
		return service.listAll_wallet();
	}

	@PostMapping("/create_wallet")
	public ResponseEntity<Object> add(@RequestBody WalletDataTransferObject walletDTO) {



		//displaying if user makes invalid entry then simply exit with message
		if(service.containsLetters(walletDTO.getMobilenumber()) ||
				service.containsLetters(walletDTO.getAmount()))
			return new ResponseEntity<>("Enter correct details",HttpStatus.BAD_REQUEST);


		//displaying if user make duplicate entry
		if (service.checkforDuplicateEntry(walletDTO))
			return new ResponseEntity<>("Wallet already present",HttpStatus.MULTI_STATUS);
		//producer.publishToTopic2("User created");

		service.save_wallet(walletDTO);

		return new ResponseEntity<>("Success \n-"+walletDTO,HttpStatus.ACCEPTED);

	}


	@PostMapping("/dotransaction")
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

		t_d=Instant.now().toEpochMilli();
		String temp="Transaction Success\nTransaction ID:"+t_d;

		//saving transcation by transx_id
		service.saveToTransactionSummary(payer_phone_number,payee_phone_number,amount,t_d);

		//pushing event to Kafka
		//producer.publishToTopic(temp);

		return new ResponseEntity<String>(temp,HttpStatus.ACCEPTED);


	}

	@RequestMapping(value="/transaction",method = RequestMethod.GET)
	public List<TransactionSummaryDataTransferObject> getTransactionSummary(@RequestParam("transactionID") String tranx_id)
	{
		System.out.println("getting tranx_id"+tranx_id);
		return service.get_currentTransaction(tranx_id);
	}

	@RequestMapping(value="/all_transactions",method = RequestMethod.GET)
	public List<List<?>> get_transactions(@RequestBody String mobilenumber)
	{

		return service.get_All_transactions(mobilenumber);
	}

	@PostMapping("/authenticate")
	public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							jwtRequest.getUsername(),
							jwtRequest.getPassword()
					)
			);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}

		final UserDetails userDetails
				= service.loadUserByUsername(jwtRequest.getUsername());

		final String token =
				jwtUtility.generateToken(userDetails);

		return  new JwtResponse(token);
	}


	//basic api's
	@PutMapping("/wallet/{id}")
	public ResponseEntity<?> update(@RequestBody WalletDataTransferObject walletDTO, @PathVariable String id) {
		try {
			WalletDataTransferObject existProduct = service.get_wallet(id);

			service.save_wallet(walletDTO);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
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

	@GetMapping("/wallet/{id}")
	public ResponseEntity<WalletDataTransferObject> get(@PathVariable String id) {
		try {
			WalletDataTransferObject user = service.get_wallet(id);

			return new ResponseEntity<WalletDataTransferObject>(user, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<WalletDataTransferObject>(HttpStatus.NOT_FOUND);
		}
	}



}
