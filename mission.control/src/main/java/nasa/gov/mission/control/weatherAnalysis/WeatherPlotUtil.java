package nasa.gov.mission.control.weatherAnalysis;

import space.exploration.communications.protocol.service.SeasonalWeather;
import space.exploration.communications.protocol.service.WeatherData;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WeatherPlotUtil {

    public static File generateDataFile(SeasonalWeather.SeasonalWeatherPayload seasonalWeatherPayload) throws IOException {
        File weatherDataFile = new File("weatherData.csv");

        List<String> lines = new ArrayList<>();
        for (WeatherData.WeatherPayload weatherPayload : seasonalWeatherPayload.getWeatherReportsList()) {
            lines.add(weatherPayload.getSol() + "," + weatherPayload.getMinTemp());
        }

        Path filePath = Paths.get("weatherData.csv");

        Files.write(filePath, lines, Charset.forName("UTF-8"));

        return filePath.toFile();
    }
}