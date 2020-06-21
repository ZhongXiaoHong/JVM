import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


//TODO -XX:MetaspaceSize=10M -XX:MaxMetaspaceSize=10m
//TODO  设置方法区大小最大10M
public class Test2 {

    public static void main(String[] args){

        while(true){



            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Test3.class);
            enhancer.setUseCache(false);//TODO  不使用缓存，每一次都重新创建Test3对应的代理类字节码
            enhancer.setCallback(new MethodInterceptor() {
                public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
                    return arg3.invokeSuper(arg0, arg2);
                }
            });
            enhancer.create();


        }


    }
}