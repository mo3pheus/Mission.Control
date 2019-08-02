package naasa.gov.mission.control;

import communications.protocol.KafkaConfig;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LogsDialog extends JDialog {
    private JPanel LogsDialogPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField startdate;
    private JTextField enddate;

    //JFrame frame =new JFrame();
    public LogsDialog() {
        setContentPane(LogsDialogPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        LogsDialogPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() throws IOException {
        // add your code here
        InputStream fis             = Producer.class.getResourceAsStream("/kafka1.properties");
        Properties kafkaProperties = new Properties();
        kafkaProperties.load(fis);

        Transmitter transmitter = new Transmitter(KafkaConfig.getKafkaConfig("Mission.Control"));

        String x = (startdate.getText().toString());
        String y = (enddate.getText().toString());

        try {
            if(x !="" && y !="")
            {byte[] msg     = CommandBuilder.buildLogRequestCommand(x,y);
            transmitter.transmitMessage(msg);}
        } catch (InterruptedException err) {
            err.printStackTrace();
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        LogsDialog dialog = new LogsDialog();
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }
}
