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
   
   
  *法区与堆都是线程共享的，为什么要做区分呢，不合二为一呢*
   
   
   
   
   
   
   
    







