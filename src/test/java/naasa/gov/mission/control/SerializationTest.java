package naasa.gov.mission.control;

import java.io.*;

public class SerializationTest {
    public static void writeObjects(String[] args) throws Exception {
        TestObject testObject  = new TestObject("Sanket");
        TestObject testObject1 = new TestObject("Anil");

        TestObject.LittleObject littleFella1 = new TestObject.LittleObject();
        littleFella1.setKey("key");
        littleFella1.setValue("value");
        testObject.addLittleObjects(littleFella1);

        littleFella1 = new TestObject.LittleObject();
        littleFella1.setKey("anotherKey");
        littleFella1.setValue("someOtherValue");
        testObject1.addLittleObjects(littleFella1);

        FileOutputStream   fileOutputStream   = new FileOutputStream(new File("serializationTest.ser"));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(testObject);
        objectOutputStream.writeObject(testObject1);


        objectOutputStream.close();
    }

    public static void readObjects(String[] args) {
        int i = 0;
        try {
            FileInputStream   fileInputStream   = new FileInputStream("serializationTest.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object            testObject        = null;
            while ((testObject = objectInputStream.readObject()) != null) {
                TestObject t = (TestObject) testObject;
                i++;
                System.out.println(t);
                System.out.println("Object Number " + i);
                System.out.println("============================");
            }
        } catch (EOFException e) {
            System.out.println("End of File has been reached.");
        } catch (IOException io) {
            io.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            writeObjects(args);
            Thread.sleep(2000);
            readObjects(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
