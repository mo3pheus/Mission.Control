package naasa.gov.mission.control;

import space.exploration.mars.rover.InstructionPayloadOuterClass.InstructionPayload;
import space.exploration.mars.rover.InstructionPayloadOuterClass.InstructionPayload.TargetPackage;
import space.exploration.mars.rover.kernel.ModuleDirectory.Module;
import space.exploration.mars.rover.robot.RobotPositionsOuterClass.RobotPositions;

public class CommandBuilder {

    public static byte[] buildLidarCommand() {
        InstructionPayload.Builder iBuilder = InstructionPayload.newBuilder();
        iBuilder.setTimeStamp(System.currentTimeMillis());
        iBuilder.setSOS(false);

        TargetPackage.Builder tBuilder = TargetPackage.newBuilder();
        tBuilder.setAction("ScanArea");
        tBuilder.setRoverModule(Module.SENSOR_LIDAR.getValue());
        tBuilder.setEstimatedPowerUsage(20);
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

        tBuilder.setAuxiliaryData(RobotPositions.newBuilder().addPositions(targetPosition).build()
                .toByteString());

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
        tBuilder.setRoverModule(Module.CAMERA_SENSOR.getValue());
        tBuilder.setEstimatedPowerUsage(10);

        iBuilder.addTargets(tBuilder.build());
        return iBuilder.build().toByteArray();
    }
}
