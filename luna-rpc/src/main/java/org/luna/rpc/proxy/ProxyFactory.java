package org.luna.rpc.proxy;

/**
 * Created by kaiba on 2016/5/24.
 */
public class ProxyFactory {

    /** 动态代理类的类名后缀 */
    private final static String PROXY_CLASS_NAME_SUFFIX = "$stub_";

    /** 动态代理类的类名索引，防止类名重复 */
    private static int proxyClassIndex = 1;

    public static <T> T getStub(Class<T> interfaceClass){
//        try{
//            ClassPool cp = ClassPool.getDefault();
//            String interfaceName = interfaceClass.getName();
//            //动态指定代理类的类名
//            String proxyClassName = interfaceName + PROXY_CLASS_NAME_SUFFIX + proxyClassIndex++;
//            //要实现的接口的包名+接口名
//            String interfaceNamePath = interfaceName;
//
//            CtClass ctInterface = cp.getCtClass(interfaceClass.getName());
//            CtClass cc = cp.makeClass(proxyClassName);
//            cc.addInterface(ctInterface);
//            Method[] methods = interfaceClass.getMethods();
//            for(int i = 0; i < methods.length; i++) {
//                Method method = methods[i];
//                dynamicImplementsMethodsFromInterface(classToProxy, cc, method, interceptorHandlerImplClass, i);
//            }
//            return (Object)cc.toClass().newInstance();
//        }catch (Exception e){
//
//        }

        return null;
    }

//    /**
//     * 动态实现接口里的方法
//     * @param classToProxy String 要动态代理的接口的实现类的类名, e.g test.StudentInfoServiceImpl
//     * @param implementer CtClass 动态代理类的包装
//     * @param methodToImpl Method 动态代理类里面要实现的接口方法的包装
//     * @param interceptorClass Class 用户提供的拦截器实现类
//     * @param methodIndex int 要实现的方法的索引
//     * @throws CannotCompileException
//     */
//    private static void dynamicImplementsMethodsFromInterface(String classToProxy, CtClass implementer, Method methodToImpl, Class interceptorClass, int methodIndex) throws CannotCompileException {
//        String methodCode = generateMethodCode(classToProxy, methodToImpl, interceptorClass, methodIndex);
//        CtMethod cm = CtNewMethod.make(methodCode, implementer);
//        implementer.addMethod(cm);
//    }
}
