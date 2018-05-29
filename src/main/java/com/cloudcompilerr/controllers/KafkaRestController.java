package com.cloudcompilerr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudcompilerr.producer.services.KafkaSender;

@RestController
public class KafkaRestController {

	@Autowired
	private KafkaSender kafkaProducerService;

	@Value("${kafka.topic.topicname}")
	private String topicName;

	@GetMapping(value = "/producer")
	public String producer(@RequestParam("message") String message) {

		kafkaProducerService.send(topicName, message);
		return "Message sent to the Kafka Topic " + topicName + "Successfully";
	}

}
