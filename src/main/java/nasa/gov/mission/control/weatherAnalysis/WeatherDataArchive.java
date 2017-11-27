package nasa.gov.mission.control.weatherAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class WeatherDataArchive {

    public static final String FILENAME_PREFIX = "weatherDataCuriosityActual_";

    private String weatherData = null;

    public WeatherDataArchive(String weatherData) {
        this.weatherData = weatherData;
    }

    public void archiveWeatherData() throws IOException {
        URL    fileURL   = WeatherDataArchive.class.getResource("/WeatherDataArchive/");
        String urlString = "src/main/resources/WeatherDataArchive/" + FILENAME_PREFIX
                + Long.toString(System.currentTimeMillis()) + ".log";
        System.out.println(urlString);
        File           weatherDataFile = new File(urlString);
        BufferedWriter bufferedWriter  = new BufferedWriter(new FileWriter(weatherDataFile));
        bufferedWriter.write(weatherData);
        bufferedWriter.close();
    }
}