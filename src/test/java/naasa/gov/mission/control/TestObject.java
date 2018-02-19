package naasa.gov.mission.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TestObject implements Serializable {
    private long               creationTimeStamp = 0l;
    private String             objectName        = "";
    private List<LittleObject> littleFellas      = new ArrayList<>();
    private Properties         properties        = new Properties();

    @Override
    public String toString() {
        String state = "CreationTime = " + Long.toString(creationTimeStamp) + " ObjectName = " + objectName;
        for (LittleObject littleObject : littleFellas) {
            state += littleObject.toString();
        }

        for (Object key : properties.keySet()) {
            state += " k:: " + (String) key + " v ::" + properties.get(key);
        }

        return state;
    }

    public void addLittleObjects(LittleObject littleObject) {
        littleFellas.add(littleObject);
        properties.put(littleObject.getKey(), littleObject.getValue());
    }

    public TestObject(String name) {
        this.creationTimeStamp = System.currentTimeMillis();
        this.objectName = name;
    }

    public long getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(long creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public List<LittleObject> getLittleFellas() {
        return littleFellas;
    }

    public void setLittleFellas(List<LittleObject> littleFellas) {
        this.littleFellas = littleFellas;
    }

    public static class LittleObject implements Serializable {
        String key;
        String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String toString() {
            return " key = " + key + " value = " + value;
        }
    }
}
