package naasa.gov.mission.control;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Producer {
    public static void main(String[] args) throws Exception {
        InputStream fis             = Producer.class.getResourceAsStream("/kafka.properties");
        Properties  kafkaProperties = new Properties();
        kafkaProperties.load(fis);

        Transmitter transmitter = new Transmitter(kafkaProperties);

        int choice = 0;
        while (choice != 5) {
            System.out.println("0. Send Move Message");
            System.out.println("1. Send Lidar Message");
            System.out.println("2. Send Scientific Message");
            System.out.println("3. Send Camera Command");
            System.out.println("4. Send Radar command");
            System.out.println("5. Exit");
            System.out.println("Please enter your choice");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();

            switch (choice) {
                case 0: {
                    System.out.println("Please enter the co-oridnates::");
                    Scanner  pointScanner = new Scanner(System.in);
                    String   point        = pointScanner.nextLine();
                    String[] points       = point.split(",");
                    int      x            = Integer.parseInt(points[0]);
                    int      y            = Integer.parseInt(points[1]);
                    transmitter.transmitMessage(CommandBuilder.buildMoveCommand(x, y));
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
                    transmitter.transmitMessage(CommandBuilder.buildCameraCommand());
                }
                ;
                break;
                case 4: {
                    transmitter.transmitMessage(CommandBuilder.buildRadarCommand());
                }
                break;
                case 5: {
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
