import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


//TODO -Xms30m -Xmx30m -XX:+PrintGCDetails
//TODO 分别设置堆初始大小30m，最大30m,打印GC信息
public class TestSSolfReference {

    String tag = "测试软引用垃圾回收";

    InerClass inClazz = new InerClass("内部Class","内部的");

    @Override
    public String toString() {
        return "TestSSolfReference{" +
                "tag='" + tag + '\'' +
                ", inClazz=" + inClazz +
                '}';
    }

    public static void main(String[] args) {

        TestSSolfReference  obj = new TestSSolfReference();

        SoftReference<TestSSolfReference> objSolf = new SoftReference<TestSSolfReference>(obj);

        obj = null;
        System.out.println("OBJ ："+objSolf.get());
        System.gc();
        System.out.println("手动触发发生一次GC");
        System.out.println("OBJ ："+objSolf.get());


        InerClass inclazz = objSolf.get().inClazz;

        List<byte[]> list = new LinkedList<>();
        try {
            for(int i=0;i<50;i++){
                list.add(new byte[1024*1024]);//TODO 每次创建1M的数组
            }
        }catch (Throwable e){
            System.out.println("异常："+objSolf.get()+"----"+inclazz);
        }



    }


}

class InerClass{

    String name;

    String flag;

    public InerClass(String name, String flag) {
        this.name = name;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "InerClass{" +
                "name='" + name + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
