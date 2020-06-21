public class Test5 {

    public static void main(String[] args) {

        //TODO 如果没有优化的，则虚拟机栈会压入两个栈帧，一个是main栈帧，一个是compare栈帧
        boolean result = compare(1,2);
        //TODO  上面的调用可以编译优化成以下
        //TODO   以下优化后可以少一个栈帧入栈，只需压入main栈帧
        //TODO   这样的优化叫方法内联————将目标方法原封不动的移到调用处来

        boolean result2 = 1>2;

    }

    public static boolean compare(int a,int b){

        return a>b;
    }
}
