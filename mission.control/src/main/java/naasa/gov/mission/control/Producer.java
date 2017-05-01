package naasa.gov.mission.control;

import java.io.InputStream;
import java.util.Properties;

public class Producer {
	public static void main(String[] args) throws Exception {
		InputStream fis = Producer.class.getResourceAsStream("/kafka.properties");
		Properties kafkaProperties = new Properties();
		kafkaProperties.load(fis);

		Transmitter transmitter = new Transmitter(kafkaProperties);
		transmitter.transmitMessage();
	}
}
