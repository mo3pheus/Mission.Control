package naasa.gov.mission.control;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FailedDialog extends JDialog {
    private JPanel failedContentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    public FailedDialog() {
        setContentPane(failedContentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public static void main(String[] args) {
        FailedDialog dialog = new FailedDialog();
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
