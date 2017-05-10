/**
 *
 */
package naasa.gov.mission.control;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import space.exploration.mars.rover.bootstrap.MatrixCreation;

import com.google.protobuf.InvalidProtocolBufferException;
import space.exploration.mars.rover.communication.RoverStatusOuterClass;
import space.exploration.mars.rover.kernel.ModuleDirectory.Module;
import space.exploration.mars.rover.spectrometer.SpectrometerScanOuterClass.SpectrometerScan;

import javax.imageio.ImageIO;
import javax.swing.*;


/**
 * @author sanketkorgaonkar
 */
public class Receiver extends Thread {
    final static String clientId = "Curiosity";
    final static String TOPIC    = "secure_com_to_earth_channel_3";
    ConsumerConnector consumerConnector = null;

    public Receiver() throws Exception {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "localhost:2181");
        properties.put("group.id", "test-coms-zion-controlRoom");
        ConsumerConfig consumerConfig = new ConsumerConfig(properties);
        consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        MatrixCreation.configureLogging();
        Properties matrixConfig = new Properties();
        FileInputStream propFile = new FileInputStream(
                "/Users/sanketkorgaonkar/Documents/CodeRepos/AdvancedMatrix/advanced.matrix/src/main/resources/mazeDefinition.properties");
        matrixConfig.load(propFile);
    }

    public Receiver(Properties comsConfig) {
        ConsumerConfig consumerConfig = new ConsumerConfig(comsConfig);
        consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
    }

    @Override
    public void run() {
        System.out.println("Listening to Mars...");
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
                .createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]>      stream       = consumerMap.get(TOPIC).get(0);
        ConsumerIterator<byte[], byte[]> it           = stream.iterator();
        int                              messageCount = 0;
        while (it.hasNext()) {
            System.out.println("======================== MESSAGE ================================== " + messageCount);
            try {
                RoverStatusOuterClass.RoverStatus received = (RoverStatusOuterClass.RoverStatus
                        .parseFrom(it.next().message()));
                System.out.println(received);

                if (received.getModuleReporting() == Module.SCIENCE.getValue()) {
                    SpectrometerScan scan = SpectrometerScan.parseFrom(received.getModuleMessage());
                    System.out.println(scan);
                } else if (received.getModuleReporting() == Module.CAMERA_SENSOR.getValue()) {
                    byte[] imageBytes = received.getModuleMessage().toByteArray();
                    try {
                        BufferedImage imag = ImageIO.read(new ByteArrayInputStream(imageBytes));
                        JFrame frame = new JFrame();
                        frame.setBounds(0,0, 1000,1000);
                        Graphics g = frame.getGraphics();
                        Graphics2D g2 = (Graphics2D)g;
                        g2.drawImage(imag, null, 0,0);
                                        } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("ERT = " + System.currentTimeMillis());
                System.out.println("OWLT = " + (System.currentTimeMillis() - received.getSCET()));
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            messageCount++;
        }
    }
}
