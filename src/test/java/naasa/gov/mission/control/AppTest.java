package naasa.gov.mission.control;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    private class ImageComponent extends JComponent{
        private BufferedImage image = null;
        public ImageComponent(BufferedImage image){
            this.image = image;
        }

       public void paint(Graphics g){
           Graphics2D g2 = (Graphics2D) g;

           g2.drawImage(image, 10, 10, this);
           g2.finalize();
       }

    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {

        BufferedImage imag = null;
        try {
            imag = ImageIO.read(new File(AppTest.class.getResource("/mars002.jpg").getPath()));
            JFrame frame = new JFrame();
            ImageComponent iComponent = new ImageComponent(imag);
            frame.setBounds(0, 0, 1000, 1000);
            frame.getContentPane().add(iComponent);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
//            Graphics g = imag.getGraphics();
//            g.drawImage(imag, 0,0, null);
//            JFrame frame = new JFrame();
//            frame.setBounds(0, 0, 1000, 1000);
//            frame.setVisible(true);
//            Graphics g = frame.getGraphics();
//            if (g == null || imag == null ) {
//                System.out.println("Graphics was null or image was null");
//            }
//            g.drawImage(imag, 0, 0, null);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            //frame.pack();
//            frame.setVisible(true);
            Thread.sleep(3000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
