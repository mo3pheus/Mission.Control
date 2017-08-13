

public class NewRadar implements IsRadarSensor {
    private final String RADAR_NAME = "newRadarMan!";


    public void scanArea() {
        System.out.println("This is the newRadar class with name = " + RADAR_NAME + " and it will now scan the area!");
    }

    public String getName() {
        return RADAR_NAME;
    }
}