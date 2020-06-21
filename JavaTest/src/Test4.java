
//TODO 虚拟机参数   -XX:MaxDirectMemorySize=100m
//TODO  设置之际内存最大值

import java.nio.ByteBuffer;

public class Test4 {


    public static void main(String[] args) {

        //TODO 使用nio会操作直接内存
        //TODO 申请101M直接内存
        ByteBuffer bb = ByteBuffer.allocateDirect(101*1024*1024);

    }
}
