package nasa.gov.mission.control.weatherAnalysis;

import java.io.IOException;

public class WeatherDataArchiveTest {

    public static void main(String[] args){
        WeatherDataArchive weatherDataArchive = new WeatherDataArchive("This is my weather data.");
        try {
            weatherDataArchive.archiveWeatherData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
