package naasa.gov.mission.control;

import communications.protocol.KafkaConfig;

import java.sql.Connection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Producer {

    public static void main(String[] args) throws Exception {
        Driver.configureLogging(false);
        Transmitter transmitter = new Transmitter(KafkaConfig.getKafkaConfig("Mission.Control"));
        long        stTime      = System.currentTimeMillis();

        while (TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - stTime) < 15) {
            int choice = ThreadLocalRandom.current().nextInt(0, 7);
            //int choice = 3;
            System.out.println("Selected choice = " + choice);
            switch (choice) {
                case 0: {
                    int x = ThreadLocalRandom.current().nextInt(1, 27);
                    int y = ThreadLocalRandom.current().nextInt(1, 27);
                    transmitter.transmitMessage(CommandBuilder.buildMoveCommand(x * 25, y * 25));
                }
                break;
                case 1: {
                    transmitter.transmitMessage(CommandBuilder.buildLidarCommand());
                }
                break;
                case 2: {
                    transmitter.transmitMessage(CommandBuilder.buildScienceMission());
                }
                break;
                case 3: {
                    transmitter.transmitMessage(CommandBuilder.buildCameraCommand(getRandomCamId()));
                }
                break;
                case 4: {
                    transmitter.transmitMessage(CommandBuilder.buildRadarCommand());
                }
                break;
                case 5: {
                    transmitter.transmitMessage(CommandBuilder.buildSeasonalWeatherCommand());
                }
                break;
                case 6: {
                    transmitter.transmitMessage(CommandBuilder.buildSclkInfoCommand());
                }
                break;
                case 7:{
                    transmitter.transmitMessage(CommandBuilder.buildSclkSyncCommand());
                }
                break;
                case 8: {
                    System.out.println("This is NASA Mission Control coms signing off.");
                }
                break;
                default: {
                }
            }
            Thread.sleep(1 * 60000);
            //Thread.sleep(45);
        }
    }

    private static String getRandomCamId() {
        String[] camIds = {"FHAZ", "NAVCAM", "MAST", "CHEMCAM", "MAHLI", "MARDI", "RHAZ"};
        int      index  = ThreadLocalRandom.current().nextInt(0, 7);
        return camIds[index];
    }

}
