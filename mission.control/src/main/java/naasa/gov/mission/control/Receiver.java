/**
 *
 */
package naasa.gov.mission.control;

import com.google.protobuf.InvalidProtocolBufferException;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import naasa.gov.mission.control.naasa.gov.mission.control.util.ImageUtil;
import space.exploration.mars.rover.communication.RoverStatusOuterClass;
import space.exploration.mars.rover.dataUplink.CameraPayload;
import space.exploration.mars.rover.diagnostics.HeartBeatOuterClass;
import space.exploration.mars.rover.kernel.ModuleDirectory.Module;
import space.exploration.mars.rover.propulsion.TelemetryPacketOuterClass;
import space.exploration.mars.rover.radar.RadarContactListOuterClass;
import space.exploration.mars.rover.spectrometer.SpectrometerScanOuterClass.SpectrometerScan;

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
    final static   String SEPARATOR       =
            "============================================================================";
    final static   String clientId        = "Curiosity";
    final static   String TOPIC           = "curiosity_to_earth_3";
    private static String dataArchivePath = null;

    ConsumerConnector consumerConnector = null;

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
        consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        Receiver.dataArchivePath = dataArchivePath;
    }

    public static void writeImageToFile(BufferedImage bufferedImage) throws Exception {
        if (bufferedImage == null) {
            throw new RuntimeException("BufferedImage was null");
        }

        String      fileName    = dataArchivePath+"/curiosityImage" + Long.toString(System.currentTimeMillis()) + ".jpg";
        System.out.println(fileName);
        File outputFile = new File(fileName);
        ImageIO.write(bufferedImage, "jpg", outputFile);
    }

    @Override
    public void run() {
        System.out.println("Listening to Mars on topic : " + TOPIC);
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
                .createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]>      stream       = consumerMap.get(TOPIC).get(0);
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

                if (received.getModuleReporting() == Module.SCIENCE.getValue()) {
                    SpectrometerScan scan = SpectrometerScan.parseFrom(received.getModuleMessage());
                    printMessage(received.toString());
                    printMessage(scan.toString());
                } else if (received.getModuleReporting() == Module.CAMERA_SENSOR.getValue()) {

                    CameraPayload.CamPayload camPayload = CameraPayload.CamPayload.parseFrom(received.getModuleMessage());
                    //System.out.println(received.toString());

                    for (String metaData : camPayload.getImageDataMap().keySet()) {
                        System.out.println("MetaData::");
                        printMessage(metaData);
                        byte[] imageBytes = camPayload.getImageDataMap().get(metaData).toByteArray();
                        if (imageBytes != null) {
                            try {
                                BufferedImage imag = ImageIO.read(new ByteArrayInputStream(imageBytes));
                                writeImageToFile(imag);
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
                } else if (received.getModuleReporting() == Module.DIAGNOSTICS.getValue()) {
                    printMessage(received.toString());
                    HeartBeatOuterClass.HeartBeat heartBeat = HeartBeatOuterClass.HeartBeat.parseFrom(received
                                                                                                              .getModuleMessage().toByteArray());
                    scetTime = heartBeat.getSCET();
                    printMessage(heartBeat.toString());
                } else if (received.getModuleReporting() == Module.RADAR.getValue()) {
                    printMessage(received.toString());
                    RadarContactListOuterClass.RadarContactList list = RadarContactListOuterClass.RadarContactList
                            .parseFrom(received
                                               .getModuleMessage().toByteArray());
                    printMessage(list.toString());
                } else if (received.getModuleReporting() == Module.PROPULSION.getValue()) {
                    TelemetryPacketOuterClass.TelemetryPacket telemetryPacket = TelemetryPacketOuterClass
                            .TelemetryPacket.parseFrom(received.getModuleMessage());
                    printMessage(telemetryPacket.toString());
                } else {
                    printMessage(received.toString());
                }

                System.out.println("ERT = " + System.currentTimeMillis());
                System.out.println("OWLT = " + (System.currentTimeMillis() - scetTime));
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
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
