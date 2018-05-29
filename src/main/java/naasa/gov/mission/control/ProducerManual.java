package naasa.gov.mission.control;

import communications.protocol.KafkaConfig;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class ProducerManual {
    public static void main(String[] args) throws Exception {
        Driver.configureLogging(true);
        InputStream fis             = Producer.class.getResourceAsStream("/kafka1.properties");
        Properties  kafkaProperties = new Properties();
        kafkaProperties.load(fis);

        Transmitter transmitter = new Transmitter(KafkaConfig.getKafkaConfig("Mission.Control"));

        int choice = 0;
        while (choice != 12) {
            System.out.println("0. Send Move Message");
            System.out.println("1. Send Lidar Message");
            System.out.println("2. Send Spectrometer Message");
            System.out.println("3. Send Camera Command");
            System.out.println("4. Send Radar command");
            System.out.println("5. Send Weather Request");
            System.out.println("6. Send Seasonal Weather Request");
            System.out.println("7. Send SclkInformationCommand");
            System.out.println("8: Send SclkSyncCommand");
            System.out.println("9. Send DanSpectrometerCommand");
            System.out.println("10. Graceful Shutdown.");
            System.out.println("11. Software Update.");
            System.out.println("12. Exit");
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
                    transmitter.transmitMessage(CommandBuilder.buildSeasonalWeatherCommand());
                }
                case 7: {
                    transmitter.transmitMessage(CommandBuilder.buildSclkInfoCommand());
                }
                break;
                case 8: {
                    System.out.println("Please enter the utcDate in the following format yyyy-mm-dd~hh:mm:ss ::");
                    Scanner utcDateScanner = new Scanner(System.in);
                    String  utcDate        = utcDateScanner.nextLine();
                    transmitter.transmitMessage(CommandBuilder.buildSclkSyncCommand(utcDate));
                }
                break;
                case 9: {
                    transmitter.transmitMessage(CommandBuilder.buildDANSensorCommand());
                }
                break;
                case 10: {
                    transmitter.transmitMessage(CommandBuilder.buildGracefulShutdownCommand());
                }
                break;
                case 11: {
                    byte[] msg = CommandBuilder.buildSoftwareUpdateCommand();
                    transmitter.transmitMessage(msg);
                }
                break;
                case 12: {
                    System.out.println("This is NASA Mission Control coms signing off.");
                }
                break;
                default: {
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
