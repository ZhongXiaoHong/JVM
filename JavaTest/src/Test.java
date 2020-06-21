
//TODO -Xms30m -Xmx30m -XX:+PrintGCDetails
//TODO 分别设置堆初始大小30m，最大30m,打印GC信息
public class Test {

    public static void main(String[] args){

        String[] array = new  String[31*1024*1024];
    }
}
