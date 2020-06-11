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
   **栈帧** 一个方法对应一个栈帧，存放在虚拟机栈中
   
   
    







