package naasa.gov.mission.control.analytics;

import space.exploration.kernel.diagnostics.LogResponse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    public static void saveLogFiles(String archiveLocation, LogResponse.LogResponsePacket logResponsePacket) throws
                                                                                                             IOException {
        for (LogResponse.LogResponsePacket.LogFile logFile : logResponsePacket.getLogFilesList()) {
            writeFile(archiveLocation + "/" + logFile.getFilename(), logFile.getContent());
        }
    }

    private static void writeFile(String filename, String content) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));
        bufferedWriter.write(content);
        bufferedWriter.close();
    }

}
