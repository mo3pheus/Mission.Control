package nasa.gov.mission.control.positionAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PositionDataUtil {
    private static final String POSITION_DATA_FILENAME = "src/main/resources/PositionData/positionDataFile_";
    private BufferedWriter bufferedWriter = null;
    private File positionDataFile = null;

    public PositionDataUtil() throws IOException{
        positionDataFile = new File(POSITION_DATA_FILENAME + Long.toString(System.currentTimeMillis()) + ".csv");
        bufferedWriter = new BufferedWriter(new FileWriter(positionDataFile,true));
    }

    public void writeData(String dataLine) throws IOException {
        bufferedWriter.write(dataLine);
    }

    public void cleanUp() throws IOException {
        bufferedWriter.close();
    }
}
