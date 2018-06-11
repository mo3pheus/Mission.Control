package naasa.gov.mission.control;

import com.google.protobuf.ByteString;
import communications.protocol.ModuleDirectory;
import encryption.EncryptionUtil;
import space.exploration.communications.protocol.InstructionPayloadOuterClass;
import space.exploration.communications.protocol.robot.RobotPositionsOuterClass;
import space.exploration.communications.protocol.security.SecureMessage;
import space.exploration.communications.protocol.service.WeatherQueryOuterClass;
import space.exploration.communications.protocol.softwareUpdate.SwUpdatePackageOuterClass;

import java.io.File;
import java.util.Scanner;

import static communications.protocol.ModuleDirectory.SCLK_COMMAND;
import static communications.protocol.ModuleDirectory.SCLK_SYNC;

public class CommandBuilder {

    //public static final String CERT_FILE = "src/main/resources/certificates/client.ser";

    public static final String CERT_FILE = "src/main/resources/certificates/clientOriginal.ser";
    private static byte[] signAndEncryptMessage(InstructionPayloadOuterClass.InstructionPayload instructionPayload) {
        byte[] message = null;

        try {
            message = EncryptionUtil.encryptData("mission.control@Houston", new File(CERT_FILE), instructionPayload
                    .toByteArray()).toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static byte[] buildSoftwareUpdateCommand() {
        String jarFile = "https://storage.googleapis.com/rover_artifacts/softwareUpdates/mars" +
                ".rover-1.8-SOLSHOT-shaded.jar";
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();

        SwUpdatePackageOuterClass.SwUpdatePackage.Builder sBuilder = SwUpdatePackageOuterClass.SwUpdatePackage
                .newBuilder();
        sBuilder.setJarFileLocation(jarFile);
        sBuilder.setLaunchScriptLocation("https://storage.googleapis" +
                                                 ".com/rover_artifacts/softwareUpdates/softwareLaunch.sh");
        sBuilder.setJarFileName("mars.rover-1.8-SOLSHOT-shaded.jar");
        sBuilder.setVersion(1.9d);
        sBuilder.setScriptFileName("softwareLaunch.sh");
        tBuilder.setAuxiliaryData(sBuilder.build().toByteString());

        tBuilder.setAction("Update Software");
        tBuilder.setRoverModule(ModuleDirectory.Module.KERNEL.getValue());
        iBuilder.addTargets(tBuilder.build());

        InstructionPayloadOuterClass.InstructionPayload instructionPayload = iBuilder.build();
        //System.out.println(instructionPayload.toString());

        return signAndEncryptMessage(instructionPayload);
    }

    public static byte[] buildLidarCommand() {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction("ScanArea");
        tBuilder.setRoverModule(ModuleDirectory.Module.SENSOR_LIDAR.getValue());
        tBuilder.setEstimatedPowerUsage(70);
        iBuilder.addTargets(tBuilder.build());

        return signAndEncryptMessage(iBuilder.build());
    }

    public static byte[] buildWeatherCommand() {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        WeatherQueryOuterClass.WeatherQuery.Builder wQueryBuilder = WeatherQueryOuterClass.WeatherQuery.newBuilder();

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction("GetWeather");
        //tBuilder.setAuxiliaryData(null);
        tBuilder.setRoverModule(ModuleDirectory.Module.WEATHER_SENSOR.getValue());
        tBuilder.setEstimatedPowerUsage(70);
        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    public static byte[] buildSeasonalWeatherCommand() {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction("GetSeasonalWeather");
        tBuilder.setRoverModule(ModuleDirectory.Module.WEATHER_SENSOR.getValue());
        tBuilder.setEstimatedPowerUsage(70);
        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    public static byte[] buildSclkInfoCommand() {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction(SCLK_COMMAND);
        tBuilder.setRoverModule(ModuleDirectory.Module.SPACECRAFT_CLOCK.getValue());

        /* clock has a separate internal battery. Check clock lifeSpan for its duration. */
        tBuilder.setEstimatedPowerUsage(0);
        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    public static byte[] buildDANSensorCommand() {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction("Shoot Neutrons");
        tBuilder.setRoverModule(ModuleDirectory.Module.DAN_SPECTROMETER.getValue());

        /* clock has a separate internal battery. Check clock lifeSpan for its duration. */
        tBuilder.setEstimatedPowerUsage(70);
        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    public static byte[] buildSclkSyncCommand(String utcDate) {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction(SCLK_SYNC);
        tBuilder.setUtcTime(utcDate);
        tBuilder.setRoverModule(ModuleDirectory.Module.SPACECRAFT_CLOCK.getValue());
        tBuilder.setEstimatedPowerUsage(0);
        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    public static byte[] buildGracefulShutdownCommand() {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction("Graceful Shutdown.");
        tBuilder.setRoverModule(ModuleDirectory.Module.KERNEL.getValue());
        tBuilder.setEstimatedPowerUsage(0);
        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    @Deprecated
    public static byte[] buildMoveCommand() {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction("Move");
        tBuilder.setRoverModule(ModuleDirectory.Module.PROPULSION.getValue());
        tBuilder.setEstimatedPowerUsage(40);

        RobotPositionsOuterClass.RobotPositions.Point targetPosition = RobotPositionsOuterClass.RobotPositions.Point
                .newBuilder().setX(0).setY(50).build();

        /*tBuilder.setAuxiliaryData(RobotPositions.newBuilder().addPositions(targetPosition).build()
                                          .toByteString());*/
        tBuilder.setAuxiliaryData(null);

        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    public static byte[] buildMoveCommand(int x, int y) {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction("Move");
        tBuilder.setRoverModule(ModuleDirectory.Module.PROPULSION.getValue());
        tBuilder.setEstimatedPowerUsage(40);

        RobotPositionsOuterClass.RobotPositions.Point targetPosition = RobotPositionsOuterClass.RobotPositions.Point
                .newBuilder().setX(x).setY(y).build();

        tBuilder.setAuxiliaryData(RobotPositionsOuterClass.RobotPositions.newBuilder().addPositions(targetPosition)
                                          .build()
                                          .toByteString());

        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    public static byte[] buildScienceMission() {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction("Explore");
        tBuilder.setRoverModule(ModuleDirectory.Module.SCIENCE.getValue());
        tBuilder.setEstimatedPowerUsage(30);

        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    public static byte[] buildCameraCommand() {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction("ClickCamera");
        tBuilder.setRoverSubModule(getCamId());
        tBuilder.setRoverModule(ModuleDirectory.Module.CAMERA_SENSOR.getValue());
        tBuilder.setEstimatedPowerUsage(20);

        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    public static byte[] buildCameraCommand(String camId) {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction("ClickCamera");
        tBuilder.setRoverSubModule(camId);
        tBuilder.setRoverModule(ModuleDirectory.Module.CAMERA_SENSOR.getValue());
        tBuilder.setEstimatedPowerUsage(20);

        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    public static byte[] buildRadarCommand() {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction("PerformRadarScan");
        tBuilder.setRoverModule(ModuleDirectory.Module.RADAR.getValue());
        tBuilder.setEstimatedPowerUsage(20);

        iBuilder.addTargets(tBuilder.build());
        return signAndEncryptMessage(iBuilder.build());
    }

    public static String getCamId() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter one of the following cam Ids:: (CaseSensitive)"
                                   + "FHAZ, NAVCAM, MAST, CHEMCAM, MAHLI, MARDI or RHAZ"
        );
        return scanner.nextLine();
    }
}
