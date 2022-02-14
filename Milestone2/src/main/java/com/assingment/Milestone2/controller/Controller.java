package com.assingment.Milestone2.controller;

import com.assingment.Milestone2.dto.TransactionSummaryDataTransferObject;
import com.assingment.Milestone2.dto.WalletDataTransferObject;
import com.assingment.Milestone2.Kafka.Producer;
import com.assingment.Milestone2.model.JwtRequest;
import com.assingment.Milestone2.model.JwtResponse;
import com.assingment.Milestone2.service.WalletService;
import com.assingment.Milestone2.utility.JWTUtility;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	Logger logger = LogManager.getLogger(Controller.class);


	@RequestMapping(value="/post",method = RequestMethod.GET)
	public void sendMessage(@RequestParam("mobilenumber") String msg) {
		producer.publishToTopic(msg);
	}


	@GetMapping("/wallet")
	public List<WalletDataTransferObject> list() {
		return service.listAll_wallet();
	}

	@PostMapping("/create_wallet")
	public ResponseEntity<Object> add(@RequestBody WalletDataTransferObject walletDTO) {


		logger.info("Creation wallet initiated");
		logger.debug("checking for valid entry");

		if(service.containsLetters(walletDTO.getMobilenumber()) ||
				service.containsLetters(walletDTO.getAmount()))
		{
			logger.error("Mobile Number contains letter");
			return new ResponseEntity<>("Enter correct details",HttpStatus.BAD_REQUEST);

		}


		logger.debug("Checking for duplicate entry");
		if (service.checkforDuplicateEntry(walletDTO))
		{
			logger.error("Duplicate entry found");
			return new ResponseEntity<>("Wallet already present",HttpStatus.MULTI_STATUS);

		}
		//producer.publishToTopic2("User created");
		logger.debug("Creating wallet");
		service.save_wallet(walletDTO);
		logger.debug("Completed");

		return new ResponseEntity<>("Success \n-"+walletDTO,HttpStatus.ACCEPTED);

	}


	@PostMapping("/dotransaction")
	public ResponseEntity<String> transfer(@RequestBody ObjectNode ndj) {
		logger.info("Transaction command initiated");


		String payer_phone_number = ndj.get("payer_phone_number").asText();
		String payee_phone_number = ndj.get("payee_phone_number").asText();
		String amount=ndj.get("amount").asText();
		logger.debug("Entries collected successfully");


		logger.debug("Checking for valid entries");
		if(service.containsLetters(payer_phone_number)
				|| service.containsLetters(payee_phone_number)
				|| service.containsLetters(amount)) {

			logger.error("Phone Number contains letters");
			return new ResponseEntity<String>("Enter correct details", HttpStatus.BAD_REQUEST);
		}

		logger.debug("Checking for valid funds");

		//checking funds
		if(service.checkForSufficientAmount(payer_phone_number,amount)) {
			logger.error("Insufficient funds");
			return new ResponseEntity<String>("Insufficient Funds", HttpStatus.BAD_REQUEST);
		}
		logger.debug("Deducting Amount from payer");
		service.deductAmount(payer_phone_number,amount); //deducting amount of payer

		logger.debug("Adding amount to payee");
		service.addAmount(payee_phone_number,amount);   //adding amount to payee

		t_d=Instant.now().toEpochMilli();
		String temp="Transaction Success\nTransaction ID:"+t_d;

		logger.debug("Saving Transaction with TransactionID");
		service.saveToTransactionSummary(payer_phone_number,payee_phone_number,amount,t_d);
		logger.debug("Completed");

		//pushing event to Kafka
		//producer.publishToTopic(temp);

		return new ResponseEntity<String>(temp,HttpStatus.ACCEPTED);


	}

	@RequestMapping(value="/transaction",method = RequestMethod.GET)
	public List<TransactionSummaryDataTransferObject> getTransactionSummary(@RequestParam("transactionID") String tranx_id)
	{
		logger.info("Getting Current transaction initiated");

		logger.debug("Current transaction ID-"+tranx_id);
		return service.get_currentTransaction(tranx_id);
	}

	@RequestMapping(value="/all_transactions",method = RequestMethod.GET)
	public List<List<?>> get_transactions(@RequestBody String mobilenumber)
	{
		logger.info("Getting all transactions for mobile number initiated");
		return service.get_All_transactions(mobilenumber);
	}

	@PostMapping("/authenticate")
	public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{
		logger.info("Authentication initiated");

		try {
			logger.debug("authenticating...");

			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							jwtRequest.getUsername(),
							jwtRequest.getPassword()
					)
			);
		} catch (BadCredentialsException e) {
			logger.error("Invalid credentials");
			throw new Exception("INVALID_CREDENTIALS", e);
		}

		logger.debug("loading user...");
		final UserDetails userDetails
				= service.loadUserByUsername(jwtRequest.getUsername());

		logger.debug("generating token");
		final String token =
				jwtUtility.generateToken(userDetails);

		logger.debug("Completed");
		return  new JwtResponse(token);
	}


	//basic api's
	@PutMapping("/wallet/{id}")
	public ResponseEntity<?> update(@RequestBody WalletDataTransferObject walletDTO, @PathVariable String id) {
		logger.info("Updating wallet operation initiated");

		try {
			logger.debug("Searching for wallet");

			WalletDataTransferObject existProduct = service.get_wallet(id);
			logger.debug("Wallet found");

			service.save_wallet(walletDTO);
			logger.debug("Completed");

			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
		catch (NoSuchElementException e) {
			logger.error("No wallet found");

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (NumberFormatException e) {
			logger.error("Invalid entry");
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/wallet/{id}")
	public void delete(@PathVariable String id) {
		logger.info("Deletion Id initiated");
		service.delete(id);
	}

	@GetMapping("/wallet/{id}")
	public ResponseEntity<WalletDataTransferObject> get(@PathVariable String id) {
		logger.info("Getting wallet operation initiated");

		try {
			logger.debug("Searching wallet");
			WalletDataTransferObject user = service.get_wallet(id);
			logger.debug("Completed");
			return new ResponseEntity<WalletDataTransferObject>(user, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			logger.error("No wallet found");
			return new ResponseEntity<WalletDataTransferObject>(HttpStatus.NOT_FOUND);
		}
	}



}
