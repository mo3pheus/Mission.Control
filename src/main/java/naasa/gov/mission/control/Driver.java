package naasa.gov.mission.control;

import communications.protocol.KafkaConfig;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Hello world!
 */
public class Driver {
    public static void configureLogging(boolean debug) {
        FileAppender fa = new FileAppender();

        if (!debug) {
            fa.setThreshold(Level.toLevel(Priority.INFO_INT));
            fa.setFile("statusLogs/houstonLogs_" + Long.toString(System.currentTimeMillis()) + ".log");
        } else {
            fa.setThreshold(Level.toLevel(Priority.DEBUG_INT));
            fa.setFile("analysisLogs/houstonLogs_" + Long.toString(System.currentTimeMillis()) + ".log");
        }

        fa.setLayout(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN));

        fa.activateOptions();
        org.apache.log4j.Logger.getRootLogger().addAppender(fa);
    }

    public static void main(String[] args) throws Exception {
        configureLogging(Boolean.parseBoolean(args[1]));
        Receiver receiver = new Receiver(KafkaConfig.getKafkaConfig("Mission.Control"), args[0]);
        receiver.start();
    }
}
