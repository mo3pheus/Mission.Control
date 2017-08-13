public class Radar implements IsRadarSensor{
    private final String RADAR_NAME = "radar1";

    public String getName() {
        return RADAR_NAME;
    }

    public void scanArea() {
        System.out.println("Radar name is = " + RADAR_NAME + " and is now scanning the area.");
    }
}
