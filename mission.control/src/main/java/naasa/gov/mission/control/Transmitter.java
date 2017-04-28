/**
 * 
 */
package naasa.gov.mission.control;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import space.exploration.mars.rover.InstructionPayloadOuterClass.InstructionPayload;
import space.exploration.mars.rover.InstructionPayloadOuterClass.InstructionPayload.TargetPackage;
import space.exploration.mars.rover.kernel.ModuleDirectory.Module;

/**
 * @author sanketkorgaonkar
 *
 */
public class Transmitter {
	private Producer<String, byte[]>	earthChannel;
	private InputStream					inputStream;
	private Properties					kafkaProperties;
	
	public Transmitter(KafkaShipmentBuilder kafkaShipmentBuilder) {
		/*
		 * Set up Kafka producer
		 */
		try {
			System.out.println(kafkaShipmentBuilder.getPropertyFileLocation());
			inputStream = new FileInputStream(kafkaShipmentBuilder.getPropertyFileLocation());
			kafkaProperties = new Properties();
			kafkaProperties.load(inputStream);
			kafkaProperties.put("sourceTopic", kafkaShipmentBuilder.sourceTopic);
			earthChannel = new Producer<String, byte[]>(new ProducerConfig(kafkaProperties));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Transmitter(Properties comsConfig) {
		kafkaProperties = comsConfig;
		earthChannel = new Producer<String, byte[]>(new ProducerConfig(kafkaProperties));
	}

	public void transmitMessage() throws Exception {
		InstructionPayload.Builder iBuilder = InstructionPayload.newBuilder();
		iBuilder.setTimeStamp(System.currentTimeMillis());
		iBuilder.setSOS(false);

		TargetPackage.Builder tBuilder = TargetPackage.newBuilder();
		tBuilder.setAction("ScanArea");
		tBuilder.setRoverModule(Module.SENSOR_LIDAR.getValue());
		tBuilder.setEstimatedPowerUsage(20);
		iBuilder.addTargets(tBuilder.build());

		transmitMessage(iBuilder.build().toByteArray());
		System.out.println(iBuilder.build());
	}

	public void transmitMessage(byte[] message) throws InterruptedException, InvalidProtocolBufferException {
		for (int i = 0; i < 1; i++) {
			earthChannel.send(new KeyedMessage<String, byte[]>(kafkaProperties.getProperty("source.topic"), message));
			System.out.println(" Sending canned interrupt messages to " + kafkaProperties.getProperty("source.topic"));
			Thread.sleep(1500l);
		}
	}

	public void cleanUp() throws IOException {
		this.inputStream.close();
		this.earthChannel.close();
	}

	public static class KafkaShipmentBuilder {
		private String	sourceTopic;
		private String	propertyFileLocation;

		public KafkaShipmentBuilder withPropertyFileAt(String fileLocation) {
			this.propertyFileLocation = fileLocation;
			return this;
		}

		public KafkaShipmentBuilder withSourceTopic(String sourceTopic) {
			this.sourceTopic = sourceTopic;
			return this;
		}

		public KafkaShipmentBuilder build() throws Exception {
			return this;
		}

		public String getPropertyFileLocation() {
			return propertyFileLocation;
		}
	}
}
