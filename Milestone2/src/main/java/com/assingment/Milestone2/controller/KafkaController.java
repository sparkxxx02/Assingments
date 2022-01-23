package com.assingment.Milestone2.controller;

import com.assingment.Milestone2.service.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafkaapp")
public class KafkaController {

	@Autowired
	Producer producer;
	
	@RequestMapping(value="/post",method = RequestMethod.GET)
	public void sendMessage(@RequestParam("msg") String msg) {
		producer.publishToTopic(msg);
	}
}
