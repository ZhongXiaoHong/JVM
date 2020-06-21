import java.lang.reflect.Proxy;

public class Test6 {

    public void dosth(int x) throws Exception {

        int y = x + 5;

        Thread.sleep(Integer.MAX_VALUE);

    }

    public static void main(String[] args) throws Exception {

        Test6 test = new Test6();
        test.dosth(100);


    }
}
