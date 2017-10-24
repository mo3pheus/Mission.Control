package naasa.gov.mission.control;

import space.exploration.mars.rover.InstructionPayloadOuterClass.InstructionPayload;
import space.exploration.mars.rover.InstructionPayloadOuterClass.InstructionPayload.TargetPackage;
import space.exploration.mars.rover.kernel.ModuleDirectory.Module;
import space.exploration.mars.rover.robot.RobotPositionsOuterClass.RobotPositions;
import space.exploration.mars.rover.service.WeatherQueryOuterClass;

import java.util.Scanner;

public class CommandBuilder {

    public static byte[] buildLidarCommand() {
        InstructionPayload.Builder iBuilder = InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        TargetPackage.Builder tBuilder = TargetPackage.newBuilder();
        tBuilder.setAction("ScanArea");
        tBuilder.setRoverModule(Module.SENSOR_LIDAR.getValue());
        tBuilder.setEstimatedPowerUsage(70);
        iBuilder.addTargets(tBuilder.build());
        return iBuilder.build().toByteArray();
    }

    public static byte[] buildWeatherCommand() {
        InstructionPayload.Builder iBuilder = InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        String terrestrialStartDate = "2017-09-01";
        String terrestrialEndDate   = "2017-09-22";

        WeatherQueryOuterClass.WeatherQuery.Builder wQueryBuilder = WeatherQueryOuterClass.WeatherQuery.newBuilder();
        wQueryBuilder.setTerrestrialEndDate(terrestrialEndDate).setTerrestrialStartDate(terrestrialStartDate);

        TargetPackage.Builder tBuilder = TargetPackage.newBuilder();
        tBuilder.setAction("GetSeasonalWeather");
        tBuilder.setAuxiliaryData(wQueryBuilder.build().toByteString());
        tBuilder.setRoverModule(Module.WEATHER_SENSOR.getValue());
        tBuilder.setEstimatedPowerUsage(70);
        iBuilder.addTargets(tBuilder.build());
        return iBuilder.build().toByteArray();
    }

    public static byte[] buildSeasonalWeatherCommand() {
        InstructionPayload.Builder iBuilder = InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        TargetPackage.Builder tBuilder = TargetPackage.newBuilder();
        tBuilder.setAction("GetSeasonalWeather");
        tBuilder.setRoverModule(Module.WEATHER_SENSOR.getValue());
        tBuilder.setEstimatedPowerUsage(70);
        iBuilder.addTargets(tBuilder.build());
        return iBuilder.build().toByteArray();
    }

    @Deprecated
    public static byte[] buildMoveCommand() {
        InstructionPayload.Builder iBuilder = InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        TargetPackage.Builder tBuilder = TargetPackage.newBuilder();
        tBuilder.setAction("Move");
        tBuilder.setRoverModule(Module.PROPULSION.getValue());
        tBuilder.setEstimatedPowerUsage(40);

        RobotPositions.Point targetPosition = RobotPositions.Point.newBuilder().setX(0).setY(50).build();

        /*tBuilder.setAuxiliaryData(RobotPositions.newBuilder().addPositions(targetPosition).build()
                                          .toByteString());*/
        tBuilder.setAuxiliaryData(null);

        iBuilder.addTargets(tBuilder.build());
        return iBuilder.build().toByteArray();
    }

    public static byte[] buildMoveCommand(int x, int y) {
        InstructionPayload.Builder iBuilder = InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        TargetPackage.Builder tBuilder = TargetPackage.newBuilder();
        tBuilder.setAction("Move");
        tBuilder.setRoverModule(Module.PROPULSION.getValue());
        tBuilder.setEstimatedPowerUsage(40);

        RobotPositions.Point targetPosition = RobotPositions.Point.newBuilder().setX(x).setY(y).build();

        tBuilder.setAuxiliaryData(RobotPositions.newBuilder().addPositions(targetPosition).build()
                                          .toByteString());

        iBuilder.addTargets(tBuilder.build());
        return iBuilder.build().toByteArray();
    }

    public static byte[] buildScienceMission() {
        InstructionPayload.Builder iBuilder = InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        TargetPackage.Builder tBuilder = TargetPackage.newBuilder();
        tBuilder.setAction("Explore");
        tBuilder.setRoverModule(Module.SCIENCE.getValue());
        tBuilder.setEstimatedPowerUsage(30);

        iBuilder.addTargets(tBuilder.build());
        return iBuilder.build().toByteArray();
    }

    public static byte[] buildCameraCommand() {
        InstructionPayload.Builder iBuilder = InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        TargetPackage.Builder tBuilder = TargetPackage.newBuilder();
        tBuilder.setAction("ClickCamera");
        tBuilder.setRoverSubModule(getCamId());
        tBuilder.setRoverModule(Module.CAMERA_SENSOR.getValue());
        tBuilder.setEstimatedPowerUsage(20);

        iBuilder.addTargets(tBuilder.build());
        return iBuilder.build().toByteArray();
    }

    public static byte[] buildCameraCommand(String camId) {
        InstructionPayload.Builder iBuilder = InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        TargetPackage.Builder tBuilder = TargetPackage.newBuilder();
        tBuilder.setAction("ClickCamera");
        tBuilder.setRoverSubModule(camId);
        tBuilder.setRoverModule(Module.CAMERA_SENSOR.getValue());
        tBuilder.setEstimatedPowerUsage(20);

        iBuilder.addTargets(tBuilder.build());
        return iBuilder.build().toByteArray();
    }

    public static byte[] buildRadarCommand() {
        InstructionPayload.Builder iBuilder = InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        TargetPackage.Builder tBuilder = TargetPackage.newBuilder();
        tBuilder.setAction("PerformRadarScan");
        tBuilder.setRoverModule(Module.RADAR.getValue());
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
