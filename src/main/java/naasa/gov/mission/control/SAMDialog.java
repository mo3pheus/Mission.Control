package naasa.gov.mission.control;


import communications.protocol.KafkaConfig;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SAMDialog extends JDialog {
    private JPanel SAMDialogPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField sol;

    //Frame frame =new JFrame();
    public SAMDialog() {
        setContentPane(SAMDialogPane);
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
        SAMDialogPane.registerKeyboardAction(new ActionListener() {
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
        System.out.println(sol.getText());
        int x = Integer.parseInt(sol.getText());
        try {
            if(x > 0 )
                transmitter.transmitMessage(CommandBuilder.buildSampleAnalysis(x));

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
        SAMDialog dialog = new SAMDialog();
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }
}