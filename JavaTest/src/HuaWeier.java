public class HuaWeier {

    public final static String IOS_ENGINEER = "ios 工程师";//TODO  常量

    public static String ANDROID_ENGINEER = "android 工程师";//TODO  静态变量

    private String name;
    private String engineerType;
    private int age;


    public HuaWeier(String name, String engineerType, int age) {
        this.name = name;
        this.engineerType = engineerType;
        this.age = age;
    }


    public static void main(String[] args) throws InterruptedException {

        HuaWeier xiaoming = new HuaWeier("小明", IOS_ENGINEER, 33);

        for (int i = 0; i < 20; i++) {//TODO 进行20次垃圾回收
            System.gc();
        }

        HuaWeier xiaohong = new HuaWeier("小红", IOS_ENGINEER, 22);

        //TODO 当前线程休眠很久很久
        Thread.sleep(Integer.MAX_VALUE);

    }
}



