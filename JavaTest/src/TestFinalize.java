public class TestFinalize {

    public static TestFinalize instance = null;

    public void isAlive(){
        System.out.println("老子还活着。。。。。。。");
    }

    //TODO 重写这个方法，具体如何搭救自己的方法，自己写
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("调用finalize,搭救自己。。。。。。");
        instance = this;
    }

    public static void main(String[] args) throws InterruptedException {

        instance = new TestFinalize();

        instance = null;
        System.gc();

        //TODO finalize会在一个单独的线程调用
        //TODO 但是这个线程优先级很低
        //TODO 这里让当前线程休眠，使得finalize有机会执行
        Thread.sleep(1000);

        if(instance!=null){
            instance.isAlive();
        }else{
            System.out.println("TestFinalize死了。。。。。。");
        }


        //TODO 再来一次  和上面完全一样的  证明finalize只会执行一次

        instance = null;
        System.gc();

        //TODO finalize会在一个单独的线程调用
        //TODO 但是这个线程优先级很低
        //TODO 这里让当前线程休眠，使得finalize有机会执行
        Thread.sleep(1000);

        if(instance!=null){
            instance.isAlive();
        }else{
            System.out.println("TestFinalize死了。。。。。。");
        }
    }
}
