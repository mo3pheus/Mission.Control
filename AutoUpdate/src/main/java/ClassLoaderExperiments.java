public class ClassLoaderExperiments {

    public static void main(String[] args){
        System.out.println("Hello World!");

        CustomClassLoader customClassLoader = new CustomClassLoader();
        try {
            customClassLoader.loadAClass("Radar", "something");
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            customClassLoader.loadAClass("NewRadar", "something");
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
