package naasa.gov.mission.control;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Producer {
	public static void main(String[] args) throws Exception {
		InputStream fis = Producer.class.getResourceAsStream("/kafka.properties");
		Properties kafkaProperties = new Properties();
		kafkaProperties.load(fis);

		Transmitter transmitter = new Transmitter(kafkaProperties);

		int choice = 0;
		while (choice != 3) {
			System.out.println("0. Send Move Message");
			System.out.println("1. Send Lidar Message");
			System.out.println("2. Send Scientific Message");
			System.out.println("3. Exit");
			System.out.println("Please enter your choice");
			Scanner scanner = new Scanner(System.in);
			choice = scanner.nextInt();

			switch (choice) {
			case 0: {
				transmitter.transmitMessage(CommandBuilder.buildMoveCommand());
			}
				;
				break;
			case 1: {
				transmitter.transmitMessage(CommandBuilder.buildLidarCommand());
			}
				;
				break;
			case 2: {
				transmitter.transmitMessage(CommandBuilder.buildScienceMission());
			}
				;
				break;
			case 3: {
				scanner.close();
				System.out.println("This is NASA Mission Control coms signing off.");
			}
				;
				break;
			default: {
			}
				;
			}
		}

	}
}
