package com.cloudcompilerr.receiver.services;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaReceiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReceiver.class);

	private CountDownLatch latch = new CountDownLatch(1);

	public CountDownLatch getLatch() {
		return latch;
	}

	@KafkaListener(topics = "${kafka.topic.topicname}")
	public void receive(String payload) {
		LOGGER.info("received payload from topic='{}'", payload);
		latch.countDown();
	}
}
