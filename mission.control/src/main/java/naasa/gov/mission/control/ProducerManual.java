package naasa.gov.mission.control;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class ProducerManual {
    public static void main(String[] args) throws Exception {
        InputStream fis             = Producer.class.getResourceAsStream("/kafka.properties");
        Properties  kafkaProperties = new Properties();
        kafkaProperties.load(fis);

        Transmitter transmitter = new Transmitter(kafkaProperties);

        int choice = 0;
        while (choice != 6) {
            System.out.println("0. Send Move Message");
            System.out.println("1. Send Lidar Message");
            System.out.println("2. Send Scientific Message");
            System.out.println("3. Send Camera Command");
            System.out.println("4. Send Radar command");
            System.out.println("5. Send Weather Request");
            System.out.println("6. Exit");
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
                    transmitter.transmitMessage(CommandBuilder.buildCameraCommand(getRandomCamId()));
                }
                ;
                break;
                case 4: {
                    transmitter.transmitMessage(CommandBuilder.buildRadarCommand());
                }
                break;
                case 5: {
                    transmitter.transmitMessage(CommandBuilder.buildWeatherCommand());
                }
                break;
                case 6: {
                    System.out.println("This is NASA Mission Control coms signing off.");
                }
            }
        }

    }

    private static String getRandomCamId() {
        String[] camIds = {"FHAZ", "NAVCAM", "MAST", "CHEMCAM", "MAHLI", "MARDI", "RHAZ"};
        int      index  = ThreadLocalRandom.current().nextInt(0, 7);
        return camIds[index];
    }
}
