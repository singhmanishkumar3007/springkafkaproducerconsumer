package com.cloudcompilerr.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cloudcompilerr.SpringKafkaApplication;
import com.cloudcompilerr.producer.services.KafkaSender;
import com.cloudcompilerr.receiver.services.KafkaReceiver;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SpringKafkaApplication.class })
@DirtiesContext
@ActiveProfiles("test")
public class SpringKafkaApplicationTest {

	private static final String HELLOWORLD_TOPIC = "manish-test.t";

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, HELLOWORLD_TOPIC);

	@Autowired
	private KafkaReceiver receiver;

	@Autowired
	private KafkaSender sender;

	@Test
	public void testReceive() throws Exception {
		sender.send(HELLOWORLD_TOPIC, "Hello Spring Kafka!");

		receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
		assertThat(receiver.getLatch().getCount()).isEqualTo(0);
	}
}
