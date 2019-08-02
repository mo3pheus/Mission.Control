package naasa.gov.mission.control;

import com.google.protobuf.InvalidProtocolBufferException;
import communications.protocol.KafkaConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class ProducerGUI {
    private JButton moveRoverButton;
    private JButton lidarButton;
    private JButton specrometerButton;
    private JButton cameraButton;
    private JButton radarButton;
    private JButton weatherButton;
    private JButton seasonalWeatherButton;
    private JButton sclkInfoButton;
    private JButton danSpectrometerButton;
    private JButton exitButton;
    private JPanel rootLayout;
    private JButton shutdownButton;
    private JButton startButton;
    private JButton SamButton;
    private JButton sclkButton;

    private static InputStream fis;
    private static Properties kafkaProperties;
    private static Transmitter transmitter;
    public static void main(String[] args) {
        Driver.configureLogging(true);
        fis = Producer.class.getResourceAsStream("/kafka1.properties");
        kafkaProperties = new Properties();
        transmitter = new Transmitter(KafkaConfig.getKafkaConfig("Mission.Control"));
        try {
            kafkaProperties.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Curiosity Rover Simulator");
        frame.setContentPane(new ProducerGUI().rootLayout);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public ProducerGUI() {

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        moveRoverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MoveDialog.main(null);}
        });
        //startButton.addActionListener(new ActionListener()){};
        lidarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try {
                    transmitter.transmitMessage(CommandBuilder.buildLidarCommand());
                    SentDialog.main(null);
                } catch (Exception err) {
                    FailedDialog.main(null);
                    err.printStackTrace();
                }
            }
        });
        specrometerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    transmitter.transmitMessage(CommandBuilder.buildScienceMission());
                    SentDialog.main(null);
                } catch (Exception err) {
                    FailedDialog.main(null);
                    err.printStackTrace();
                }
            }
        });
        SamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SAMDialog.main(null);}
        });
        sclkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SpaceclockDialog.main(null);}
        });
        cameraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    transmitter.transmitMessage(CommandBuilder.buildCameraCommand(getRandomCamId()));
                    SentDialog.main(null);
                } catch (Exception err) {
                    FailedDialog.main(null);
                    err.printStackTrace();
                }
            }
        });


        radarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    transmitter.transmitMessage(CommandBuilder.buildRadarCommand());
                    SentDialog.main(null);
                } catch (Exception err) {
                    FailedDialog.main(null);
                    err.printStackTrace();
                }
            }
        });
        seasonalWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    transmitter.transmitMessage(CommandBuilder.buildSeasonalWeatherCommand());
                    SentDialog.main(null);
                } catch (Exception err) {
                    FailedDialog.main(null);
                    err.printStackTrace();
                }
            }
        });

        danSpectrometerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    transmitter.transmitMessage(CommandBuilder.buildDANSensorCommand());
                    SentDialog.main(null);
                } catch (Exception err) {
                    FailedDialog.main(null);
                    err.printStackTrace();
                }
            }
        });
        shutdownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    transmitter.transmitMessage(CommandBuilder.buildGracefulShutdownCommand());
                    SentDialog.main(null);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        });
    }
    private static String getRandomCamId() {
        String[] camIds = {"FHAZ", "NAVCAM", "MAST", "CHEMCAM", "MAHLI", "MARDI", "RHAZ"};
        int      index  = ThreadLocalRandom.current().nextInt(0, 7);
        return camIds[index];
    }
}
