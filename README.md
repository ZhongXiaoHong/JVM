# Android知识体系构建

[传送门](https://github.com/ZhongXiaoHong/construct_android_dev_skill_tree)

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
   
   特殊之处：JVM内存区域中唯一不会OOM的地方，只能指向java方法的字节码指令，执行native方法时程序计数器为null
   
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
  
  6.垃圾回收20次，xiaoming对象进入老年代
  
  ![](https://github.com/ZhongXiaoHong/JVM/blob/master/619054.jpg)
  
  7.创建对象： HuaWeier xiaohong = new HuaWeier("小红", IOS_ENGINEER, 22)
  
   xiaohong这个对象引用进入栈帧的局部变量表，对象本身进入堆
    
  ![](https://github.com/ZhongXiaoHong/JVM/blob/master/619057.jpg)
    
 8.调用  Thread.sleep(Integer.MAX_VALUE)让当前线程休眠很久，此时JVM运行时数据区域内存状态应该是第7步所示那样，
 
 如何验证这一点，可以使用内存可视化工具HSDB去查看
 
 **如何打开HSDB**
 
 首先定位到以下目录
 
 ![](https://github.com/ZhongXiaoHong/JVM/blob/master/619106.png)
 
 打开命令窗口，输入命令：
 
 java -cp .\sa-jdi.jar sun.jvm.hotspot.HSDB
 
会出现下面工具：

![](https://github.com/ZhongXiaoHong/JVM/blob/master/619112.jpg)

**查看上文示例程序的内存状态**

首先获取上文示例程序所在进程的进程号，如下：

![](https://github.com/ZhongXiaoHong/JVM/blob/master/619115.jpg)

绑定进程，填入进程号

![](https://github.com/ZhongXiaoHong/JVM/blob/master/619118.png)

![](https://github.com/ZhongXiaoHong/JVM/blob/master/619120.png)

![](https://github.com/ZhongXiaoHong/JVM/blob/master/6162322.png)

如上图，选中main线程，点击顶部第二个图标，查看stack memory（虚拟机栈内存）如下：

![](https://github.com/ZhongXiaoHong/JVM/blob/master/QQ%E6%88%AA%E5%9B%BE20200619233328.png)

上图有两个栈帧，一个是本地方法栈帧即Sleep方法栈帧，一个是java方法main方法栈帧，

从这里也可以看到HotSpots中是将虚拟机栈和本地方法栈合二为一的

> 查看方法区中的HuaWeier.class,以及两个HuaWeier对象详情

![](https://github.com/ZhongXiaoHong/JVM/blob/master/620001.png)

![](https://github.com/ZhongXiaoHong/JVM/blob/master/620005.png)

双击Huaweier那一行，如下：

![](https://github.com/ZhongXiaoHong/JVM/blob/master/620009.png)

选中一个对象，点击下方的“Inspect”按钮可以查看对象详情：

![](https://github.com/ZhongXiaoHong/JVM/blob/master/620010.png)

![](https://github.com/ZhongXiaoHong/JVM/blob/master/620011.png)

在同以下这个图对比地址

![](https://github.com/ZhongXiaoHong/JVM/blob/master/QQ%E6%88%AA%E5%9B%BE20200619233328.png)

可以知道小明这个对象已经进入了老年代，

小红这个对象还在新生代

> 查看堆详情

![](https://github.com/ZhongXiaoHong/JVM/blob/master/620018.png)

![](https://github.com/ZhongXiaoHong/JVM/blob/master/620020.png)

上图展示了堆中内存是分代划分，红色圈中部分是新生代，三个红色箭头分别指向Eden、from、To

蓝色圈中部分是老年代

从上面可以看到堆中内存是连续的，下图讲堆中每个地址标上，如下：


![](https://github.com/ZhongXiaoHong/JVM/blob/master/620031.png)


> 总结：栈 VS  堆

从功能上看：

虚拟机中以栈帧的形式存储方法的调用过程，并存储方法调用过程的基本数据类型以及对象的引用，变量出了作用域也就是出栈会自动释放

而堆是用来存储Java中的对象的，无论是成员变量、局部变量还是类变量，他们指向的对象都是存储在堆中

从线程独享与否看：

栈内存属于每一个线程私有，每一个线程都有一个栈内存，存储的变量只能被其所属线程看见，可以理解为栈内存是线程的私有内存

堆内存中的对象对所有线程可见，堆内存对象可以被所有线程访问。

从空间大小看：

栈内存要比堆小的多，栈的深度是有限的，可能发生stackoverflow

> 内存溢出

**栈溢出**

比如调用没有递归中止的递归方法，会抛出statckoverflow

**堆溢出**

```java
//TODO -Xms30m -Xmx30m -XX:+PrintGCDetails
//TODO 分别设置堆初始大小30m，最大30m,打印GC信息
public class Test {

    public static void main(String[] args){

        String[] array = new  String[31*1024*1024];
    }
}

```
设置虚拟机参数，运行程序

![](https://github.com/ZhongXiaoHong/JVM/blob/master/620056.png)

堆最大设置30M,创建String[]申请31M，堆溢出：


![](https://github.com/ZhongXiaoHong/JVM/blob/master/620057.png)

**方法区溢出**

**本机直接内存（堆外 ）溢出**


> 拓展：栈能不能存放对象

经过逃逸分析，满足条件的，栈还是可以存放对象的，下面对这一点做详细说明：

**逃逸分析**

简单来理解，逃逸分析是指在JIT编译时期（即时编译时期），分析对象的动态作用域。以下面例子说明：

```java
public static StringBuffer craeteStringBuffer(String s1, String s2) {
    StringBuffer sb = new StringBuffer();
    sb.append(s1);
    sb.append(s2);
    return sb;
}

```
以上sb对象在方法中被定义，它被return了，所以可能被外界的方法引用并改变，虽然是一个局部变量，但是他的作用域就不是在方法内，

这样的情况被称为逃逸到方法外部，如果这个对象没有逃逸出方法，那么可能对象的堆内存分配会被优化成栈内存分配，但这只是可能。

作用：减少内存堆的分配压力





01-07-02
















 
 
  
  
  

  
  
 
   
   
  
  
 

  

   
   
   
   
   
   
   
    







