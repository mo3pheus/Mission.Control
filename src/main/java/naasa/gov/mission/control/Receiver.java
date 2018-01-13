/**
 *
 */
package naasa.gov.mission.control;

import com.google.protobuf.InvalidProtocolBufferException;
import communications.protocol.ModuleDirectory;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import naasa.gov.mission.control.naasa.gov.mission.control.util.ImageUtil;
import nasa.gov.mission.control.weatherAnalysis.WeatherDataArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.exploration.communications.protocol.communication.RoverStatusOuterClass;
import space.exploration.communications.protocol.diagnostics.HeartBeatOuterClass;
import space.exploration.communications.protocol.propulsion.TelemetryPacketOuterClass;
import space.exploration.communications.protocol.radar.RadarContactListOuterClass;
import space.exploration.communications.protocol.service.CameraPayload;
import space.exploration.communications.protocol.service.DanRDRData;
import space.exploration.communications.protocol.service.WeatherRDRData;
import space.exploration.communications.protocol.spacecraftClock.SpacecraftClock;
import space.exploration.communications.protocol.spectrometer.SpectrometerScanOuterClass;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author sanketkorgaonkar
 */
public class Receiver extends Thread {
    final static   String SEPARATOR              =
            "============================================================================";
    final static   String clientId               = "Curiosity";
    final static   String TUNED_CHANNEL_PROPERTY = "source.topic";
    private static String dataArchivePath        = null;
    private        String tunedChannel           = "";
    private        Logger logger                 = LoggerFactory.getLogger(Receiver.class);

    ConsumerConnector consumerConnector = null;

    @Deprecated
    public Receiver() throws Exception {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "localhost:2181");
        properties.put("group.id", "test-coms-zion-controlRoom");
        ConsumerConfig consumerConfig = new ConsumerConfig(properties);
        consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        Properties matrixConfig = new Properties();

        FileInputStream propFile = new FileInputStream(
                "/Users/sanketkorgaonkar/Documents/CodeRepos/AdvancedMatrix/advanced" +
                        ".matrix/src/main/resources/mazeDefinition.properties");
        matrixConfig.load(propFile);
    }

    public Receiver(Properties comsConfig, String dataArchivePath) {
        ConsumerConfig consumerConfig = new ConsumerConfig(comsConfig);
        tunedChannel = comsConfig.getProperty(TUNED_CHANNEL_PROPERTY);
        consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        Receiver.dataArchivePath = dataArchivePath;
    }

    public static void writeImageToFile(BufferedImage bufferedImage) throws Exception {
        if (bufferedImage == null) {
            throw new RuntimeException("BufferedImage was null");
        }

        String fileName = dataArchivePath + "/curiosityImage" + Long.toString(System.currentTimeMillis()) + ".jpg";
        System.out.println(fileName);
        File outputFile = new File(fileName);
        ImageIO.write(bufferedImage, "jpg", outputFile);
    }

    @Override
    public void run() {
        System.out.println("Listening to Mars on topic : " + tunedChannel);
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(tunedChannel, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
                .createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]>      stream       = consumerMap.get(tunedChannel).get(0);
        ConsumerIterator<byte[], byte[]> it           = stream.iterator();
        int                              messageCount = 0;
        long                             scetTime     = 0l;
        while (it.hasNext()) {
            System.out.println("\n");
            System.out.println(SEPARATOR);
            System.out.println("RECEIVED MESSAGE:: " + messageCount);
            System.out.println(SEPARATOR);
            try {
                RoverStatusOuterClass.RoverStatus received = (RoverStatusOuterClass.RoverStatus
                        .parseFrom(it.next().message()));
                scetTime = received.getSCET();

                logger.info("Message received from " + received.getModuleName());

                if (received.getModuleReporting() == ModuleDirectory.Module.SCIENCE.getValue()) {
                    SpectrometerScanOuterClass.SpectrometerScan scan = SpectrometerScanOuterClass.SpectrometerScan
                            .parseFrom(received.getModuleMessage());
                    printMessage(received.toString());
                    printMessage(scan.toString());
                } else if (received.getModuleReporting() == ModuleDirectory.Module.CAMERA_SENSOR.getValue()) {

                    CameraPayload.CamPayload camPayload = CameraPayload.CamPayload.parseFrom(received.getModuleMessage());
                    //System.out.println(received.toString());

                    for (String metaData : camPayload.getImageDataMap().keySet()) {
                        System.out.println("MetaData::");
                        System.out.println(metaData);
                        byte[] imageBytes = camPayload.getImageDataMap().get(metaData).toByteArray();
                        if (imageBytes != null) {
                            try {
                                BufferedImage imag = ImageIO.read(new ByteArrayInputStream(imageBytes));
                                //writeImageToFile(imag);
                                JFrame frame = new JFrame();
                                frame.setBounds(0, 0, 1000, 1000);
                                frame.getContentPane().add(new ImageUtil(imag));
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                frame.setVisible(true);
                                Thread.sleep(30000);
                                frame.dispose();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (received.getModuleReporting() == ModuleDirectory.Module.DIAGNOSTICS.getValue()) {
                    printMessage(received.toString());
                    HeartBeatOuterClass.HeartBeat heartBeat = HeartBeatOuterClass.HeartBeat.parseFrom(received
                                                                                                              .getModuleMessage().toByteArray());
                    scetTime = received.getSCET();
                    printMessage(heartBeat.toString());
                    printMessage("OneWayLightTime = " + Double.toString(received.getMslPositionsPacket()
                                                                                .getOwltMSLEarth()));
                } else if (received.getModuleReporting() == ModuleDirectory.Module.RADAR.getValue()) {
                    printMessage(received.toString());
                    RadarContactListOuterClass.RadarContactList list = RadarContactListOuterClass.RadarContactList
                            .parseFrom(received
                                               .getModuleMessage().toByteArray());
                    printMessage(list.toString());
                } else if (received.getModuleReporting() == ModuleDirectory.Module.PROPULSION.getValue()) {
                    TelemetryPacketOuterClass.TelemetryPacket telemetryPacket = TelemetryPacketOuterClass
                            .TelemetryPacket.parseFrom(received.getModuleMessage());
                    printMessage(telemetryPacket.toString());
                } else if (received.getModuleReporting() == ModuleDirectory.Module.WEATHER_SENSOR.getValue()) {
                    WeatherRDRData.WeatherEnvReducedData weatherPayload = WeatherRDRData.WeatherEnvReducedData
                            .parseFrom(received.getModuleMessage());
                    printMessage(weatherPayload.toString());
                    //printMessage(received.toString());
                    WeatherDataArchive weatherDataArchive = new WeatherDataArchive(weatherPayload.toString());
                    weatherDataArchive.archiveWeatherData();
                } else if (received.getModuleReporting() == ModuleDirectory.Module.SPACECRAFT_CLOCK.getValue()) {
                    SpacecraftClock.SclkPacket sclkPacket = SpacecraftClock.SclkPacket.parseFrom(received.getModuleMessage());
                    printMessage(sclkPacket.toString());
                    printMessage(received.toString());
                } else if( received.getModuleReporting() == ModuleDirectory.Module.DAN_SPECTROMETER.getValue()){
                    DanRDRData.DANDerivedData danDerivedData = DanRDRData.DANDerivedData.parseFrom(received.getModuleMessage());
                    printMessage(danDerivedData.toString());
                } else {
                    printMessage(received.toString());
                }
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            }
            messageCount++;
        }
    }

    private void printMessage(String message) {
        try {
            for (char c : message.toCharArray()) {
                System.out.print(c);
                Thread.sleep(15);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
