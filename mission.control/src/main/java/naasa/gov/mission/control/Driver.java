package naasa.gov.mission.control;

import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class Driver 
{
    public static void main( String[] args ) throws Exception
    {
        FileInputStream fis = new FileInputStream(new File(args[0]));
        if(fis!= null){
        	Properties kafkaProperties = new Properties();
        	kafkaProperties.load(fis);
        	
        	Receiver receiver = new Receiver(kafkaProperties, args[1]);
        	receiver.start();
        }
    }
}
