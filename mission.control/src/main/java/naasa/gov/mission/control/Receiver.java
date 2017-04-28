/**
 * 
 */
package naasa.gov.mission.control;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import space.exploration.mars.rover.bootstrap.MatrixCreation;
import space.exploration.mars.rover.communication.RoverStatusOuterClass.RoverStatus;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author sanketkorgaonkar
 *
 */
public class Receiver extends Thread {
	final static String	clientId			= "Curiosity";
	final static String	TOPIC				= "secure_com_to_earth_channel_0";
	ConsumerConnector	consumerConnector	= null;

	public Receiver() throws Exception {
		Properties properties = new Properties();
		properties.put("zookeeper.connect", "localhost:2181");
		properties.put("group.id", "test-coms-zion-controlRoom");
		ConsumerConfig consumerConfig = new ConsumerConfig(properties);
		consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
		MatrixCreation.configureLogging();
		Properties matrixConfig = new Properties();
		FileInputStream propFile = new FileInputStream(
				"/Users/sanketkorgaonkar/Documents/CodeRepos/AdvancedMatrix/advanced.matrix/src/main/resources/mazeDefinition.properties");
		matrixConfig.load(propFile);
	}

	public Receiver(Properties comsConfig) {
		ConsumerConfig consumerConfig = new ConsumerConfig(comsConfig);
		consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
	}

	@Override
	public void run() {
		System.out.println("Listening to Mars...");
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(TOPIC, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
				.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(TOPIC).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		while (it.hasNext())
			try {
				RoverStatus received = (RoverStatus.parseFrom(it.next().message()));
				System.out.println(received);
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
	}
}
