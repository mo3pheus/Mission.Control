package naasa.gov.mission.control;


import communications.protocol.ModuleDirectory;
import space.exploration.communications.protocol.InstructionPayloadOuterClass;
import space.exploration.communications.protocol.robot.RobotPositionsOuterClass;
import space.exploration.communications.protocol.service.WeatherQueryOuterClass;

import java.util.Scanner;

import static communications.protocol.ModuleDirectory.SCLK_COMMAND;
import static communications.protocol.ModuleDirectory.SCLK_SYNC;

public class CommandBuilder {

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
        return iBuilder.build().toByteArray();
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
        return iBuilder.build().toByteArray();
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
        return iBuilder.build().toByteArray();
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
        return iBuilder.build().toByteArray();
    }

    public static byte[] buildSclkSyncCommand() {
        InstructionPayloadOuterClass.InstructionPayload.Builder iBuilder = InstructionPayloadOuterClass
                .InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        InstructionPayloadOuterClass.InstructionPayload.TargetPackage.Builder tBuilder = InstructionPayloadOuterClass
                .InstructionPayload.TargetPackage.newBuilder();
        tBuilder.setAction(SCLK_SYNC);
        tBuilder.setUtcTime("2016-08-27~22:58:00");
        tBuilder.setRoverModule(ModuleDirectory.Module.SPACECRAFT_CLOCK.getValue());
        tBuilder.setEstimatedPowerUsage(0);
        iBuilder.addTargets(tBuilder.build());
        return iBuilder.build().toByteArray();
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
        return iBuilder.build().toByteArray();
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
        return iBuilder.build().toByteArray();
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
        return iBuilder.build().toByteArray();
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
        return iBuilder.build().toByteArray();
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
        return iBuilder.build().toByteArray();
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
        return iBuilder.build().toByteArray();
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
        return iBuilder.build().toByteArray();
    }

    public static String getCamId() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter one of the following cam Ids:: (CaseSensitive)"
                                   + "FHAZ, NAVCAM, MAST, CHEMCAM, MAHLI, MARDI or RHAZ"
        );
        return scanner.nextLine();
    }
}
