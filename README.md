# JVM
深入理解JVM原理


## JVM功能
- 跨平台：JVM将字节码翻译成不同平台操作系统能认识的机器码
- 跨语言：像kotlin、groovy都可以运行在jvm上


## JVM内存模型
![](https://github.com/ZhongXiaoHong/JVM/blob/master/C109C00B-7026-4e45-94FB-443F97B68467.png)

- 程序计数器

   定义：指向当前线程正在执行的字节码指令地址
   
   存在意义：时间片轮转，会导致线程的切换执行，所以需要记录上次执行到的地方方便线程再次获得时间片时接着上
            次执行的地方继续执行
   
   特殊之处：JVM内存区域中唯一不会OOM的地方，
   
- 虚拟栈（JAVA栈）

![](https://github.com/ZhongXiaoHong/JVM/blob/master/6666666666666666666666.png)

   定义：存储当前线程正在运行的方法所需的数据、指令、返回地址。
   
   **栈帧:** 一个方法对应一个栈帧，存放在虚拟机栈中
   
   局部变量表：存储局部变量，主要存放8大基本数据类型+Object对象引用
   
   操作数栈：存放方法执行的操作数（详解见下文）
   
   动态链接：多态分派（详解见下文）
   
   完成出口：方法调用正常完成后返回到哪里
   
   注意：虚拟机栈是有大小限制的，通过-Xss 参数可以来设置大小， 比如-Xss1024K 设置成1024KB
   
   
   **虚拟机栈执行方法过程详解：**
   
   ![图1](https://github.com/ZhongXiaoHong/JVM/blob/master/999999999999999.jpg)
   
   1.执行main方法，创建一个栈帧入栈，当前线程虚拟机栈如下：
   
   ![图2](https://github.com/ZhongXiaoHong/JVM/blob/master/111111111111111111111111111111111.jpg)
      
   2.执行work方法，为work方法创建一个栈帧入栈,当前线程虚拟机栈如下：
   
   ![图3](https://github.com/ZhongXiaoHong/JVM/blob/master/aaaaaaaaaaaa.jpg)
   
   3.执行int x = 1,这一个操作分为两步指令，如图一所示分别是iconst_1表示将int型1入操作数栈，istore_1将操作
   数栈顶int数值存入局部变量表下标为1的位置，详细如下：
   
   执行iconst_1:
    ![图4](https://github.com/ZhongXiaoHong/JVM/blob/master/wwwwwwwwwwwwwwww.jpg)

   执行istore_1：
   
   ![图4](https://github.com/ZhongXiaoHong/JVM/blob/master/eeeeeeeeeeeeeeee3.jpg)
   
   **动态链接是什么下文使用代码解释**
   ```java
    Person person = new Man();
    person.wc();
    person = new Woman();
    person.wc();
   ```
   上文person到底要在运行时执行哪个子类的多态的wc方法，如何确定？就是通过动态链接来完成的。
   
   - 本地方法栈
   本地方法栈保存的是native方法的信息，当JVM创建的线程调用native方法的时候，JVM不会在虚拟机栈中创建栈帧，只是简单的动态链接并直接调用native方法。程序计数器是不会在记录的，因为这个方法不是java方法，虚拟机规范也不做强制规定，像hotspot直接把本地方法栈和虚拟机栈合二为一。
   
   - 方法区
   存放类信息、常量、静态变量、即时编译期编译后的代码
   
   - 堆
   主要存放对象（几乎所有）、数组
   
   
  > 法区与堆都是线程共享的，为什么要做区分呢，不合二为一呢
  
  因为堆存放的是对象、数组，是需要频繁回收的，而方法区存放的是类信息、常量、静态变量、即时编译期编译后的代码是不易被回收的，把两者区分设计完全是一种动静分离的思想，把静态的数据放在方法区，把经常要动态创建回收的对象数组放在堆，这样垃圾回收更加高效
  
  > 如何设置java堆大小
  
  -Xmx 堆内存可分配大小上限
  -Xms 堆内存初始分配大小
  
  > 方法区在不同版本上的实现
  
  小于等于JDK1.7 叫永久代，大小是受堆大小的影响
  
  大于等于JDK1.8 叫元空间，元空间可以使用机器内存，不受限制，但是可能会挤压到堆内存的大小。比如上文说到的Java堆大小设置参数，比如-Xmx10G  -Xms2G ,假设机器内存20G,元空间使用了15G,那么实际上此时堆最大可以用的内存只有20-15 = 5G,就算设置-Xmx10G这个上限也没用。
  
  方法区是各种版本都通用的叫法
  
  
 > 直接内存
 
也叫堆外内存，不是虚拟机运行时数据区的一部分，也不是JVM规范中定义的内存区域，但是如果使用了NIO,直接内存会被频繁使用，在java堆内可以用directByteBuffer对象直接引用计算，且这块内存不受java堆大小限制，但受机器总内存限制，大小可设置，默认与堆内存最大值一样，因此也会出现OOM 

> 可视化深入底层理解运行时数据区

```java
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

```
设置参数，运行代码，参数如下：

-Xms30m -Xmx30m -XX:+UseConcMarkSweepGC -XX:-UseCompressedOops

堆最大内存设置为30M,堆初始内存设置为30M,使用CMS垃圾回收器，最后设置内存指针不压缩

###在IDEA运行时如何设置JVM参数，详细过程如下：

 ![](https://github.com/ZhongXiaoHong/JVM/blob/master/8888888888888888888888888888.png)
 
 ![]( https://github.com/ZhongXiaoHong/JVM/blob/master/777777777.jpg)
 
 设置完成后运行，以下动态展示运行时数据区的变化：
 
 1.申请内存
 
 2.类加载，class进入方法区
 
  ![](https://github.com/ZhongXiaoHong/JVM/blob/master/616111.jpg)
  
  3.静态变量，常量进入方法区
  
  ![](https://github.com/ZhongXiaoHong/JVM/blob/master/616222.jpg)
  
  4.运行main方法，为main方法创建一个栈帧，main方法栈帧进入虚拟机栈
  
  ![]( https://github.com/ZhongXiaoHong/JVM/blob/master/616333.jpg)
  
  5.创建对象： HuaWeier xiaoming = new HuaWeier("小明", IOS_ENGINEER, 33)，
  
  xiaoming这个对象引用进入栈帧的局部变量表，对象本身进入堆
  
  ![](https://github.com/ZhongXiaoHong/JVM/blob/master/619050.jpg)
  

  
  
 
   
   
  
  
 

  

   
   
   
   
   
   
   
    







