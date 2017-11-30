package nasa.gov.mission.control.positionAnalysis;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import space.exploration.communications.protocol.spice.MSLRelativePositions;
import space.exploration.spice.utilities.PositionUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PositionPlot {
    private static final String           TIME_FORMAT      = "yyyy-MM-dd~HH:mm:ss";
    private static final String           START_TIME       = "2016-01-01~6:45:00";
    private static final String           END_TIME         = "2016-01-01~12:45:00";
    private static       PositionDataUtil positionDataUtil = null;

    private static DateTime ephemerisTime = null;

    public static void main(String[] args) throws IOException {
        DateTimeFormatter clockFormatter = DateTimeFormat.forPattern(TIME_FORMAT);

        DateTime startTime = clockFormatter.parseDateTime(START_TIME).withZone(DateTimeZone.UTC);
        DateTime clockTime = new DateTime(startTime);
        positionDataUtil = new PositionDataUtil();
        PositionUtils positionUtils = new PositionUtils();

        // go through one day and plot time vs angularSeparation
        System.out.println("======================================================================");
        while (clockTime.isBefore(startTime.plusHours(48))) {
            positionUtils.setUtcTime(clockFormatter.print
                    (clockTime.getMillis()));
            MSLRelativePositions.MSLRelPositionsPacket mslRelPositionsPacket = positionUtils.getPositionPacket();
            System.out.println(clockFormatter.print(clockTime.getMillis()) + "," + mslRelPositionsPacket
                    .getEphemerisTime() + ","
                                       + mslRelPositionsPacket.getOwltEarthMSL() + ","
                                       + mslRelPositionsPacket.getAngSepHGAEarth());
            positionDataUtil.writeData(clockFormatter.print(clockTime.getMillis()) + "," + mslRelPositionsPacket
                    .getEphemerisTime() + ","
                                               + mslRelPositionsPacket.getOwltEarthMSL() + ","
                                               + mslRelPositionsPacket.getAngSepHGAEarth() + "\n");
            clockTime = clockTime.plusMinutes(5);
        }
        positionDataUtil.cleanUp();
        System.out.println("======================================================================");
    }
}
