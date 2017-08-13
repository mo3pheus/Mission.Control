public class CustomClassLoader extends ClassLoader {

    public void loadAClass(String className, String methodToCall) throws Exception {
        ClassLoader cl = this.getClass().getClassLoader();
        Class targetClass = cl.loadClass(className);

        Object      o  = targetClass.newInstance();
        ((IsRadarSensor) (o)).scanArea();
    }

}
