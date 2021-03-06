---
layout: post
title: "java之动态代理"
date: 2016-08-01 11:55:11 +0800
comments: true
published: true
categories: java
---
<!--more-->
## 1.java中的代理
{% img right [class names] https://www.ibm.com/developerworks/cn/java/j-lo-proxy1/image001.png [width] [height] [title text [代理模式]] %}
某些情况下，我们无法或者不希望直接对一个对象进行访问时，我们就会用到设计模式中的**代理模式**(如右图)，通常情况下，为了保持行为的一致性，代理类和委托类拥有相同的实现接口,在使用委托类的地方都是可以用代理类来替换；代理类负责预处理消息，并转发消息给委托类、以及对委托类执行完后返回消息的处理；可以这样理解<span style="color:orange;">*委托类负责过程的具体实现，代理类负责过程实现前后的处理*</span>.这样能有效的控制对委托类的直接访问，在设计上获得了更大的灵活性.
**可以根据程序运行时，代理类是否存在将代理分为静态代理和动态代理**

### 1.1静态代理
根据定义，静态代理是指代理类在runtime之前已经存在，不管代理类是开发编辑的还是由编译器在程序编译的过程中生成的，这都属于静态代理.
<span style="color:orange;">公共接口</span>

```java [Subject.java]
public interface Subject {
    void doSomething();
}

```

<span style="color:orange;">委托类</span>
```java [RealSubject.java]
public class RealSubject implements Subject {
    @Override
    public void doSomething() {
        println("RealSubject doSomething");
    }
}
```
<span style="color:orange;">代理类</span>

``` java [ProxySubject.java]
public class ProxySubject implements Subject {
    private Subject real;

    public ProxySubject(Subject real) {
        this.real = real;
    }

    @Override
    public void doSomething() {
        println("ProxySubject before real doSomething");
        real.doSomething();
        println("ProxySubject end real doSomething");
    }

    public void doOtherthing() {
        println("ProxySubject doOtherthing");
    }
}

```

<span style="color:orange;">访问类</span>

``` java [Client.java]
public class Client {

    public static void main(String[] args) {
        RealSubject real = new RealSubject();
        ProxySubject proxy = new ProxySubject(real);

        proxy.doSomething();
        proxy.doOtherthing();
    }
}
/*
ProxySubject before real doSomething
RealSubject doSomething
ProxySubject end real doSomething
ProxySubject doOtherthing
 */
 
```

由上述代码可以看出，业务(委托类的doSomething方法)的具体实现只关注业务本身，代理类的使用隐藏了业务具体实现，使得业务的重用性更高；然而在现实开发过程中，这种实现是非常少见的，一般情况，业务类会是多个接口的实现，而代理的过程中也需要实现这些接口，且根据不同业务封装，可能就会产生多个代理类，导致类泛滥的情况.再比如由于接口封装一些业务实现不是public，而这时又因为功能需求，不得不访问这些接口的时候，这种代理方式就显得比较鸡肋了；这些情况下，就需要依靠java的反射和动态代理来解决了.

### 1.2动态代理
相关的类和接口
[java.lang.reflect.Proxy](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/lang/reflect/Proxy.java?av=f)
提供了一系列的static方法，用来创建代理类或对象，这个类是所有动态代理类的父类.[参见官方api](http://docs.oracle.com/javase/7/docs/api/java/lang/reflect/Proxy.html)
[java.lang.reflect.InvocationHandler](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/lang/reflect/InvocationHandler.java?av=f)
这是调用处理器接口，只有一个invoke方法，处理动态代理类对象方法的调用，每个动态代理类都会关联一个InvocationHandler.[参见官方api](http://docs.oracle.com/javase/7/docs/api/java/lang/reflect/InvocationHandler.html)

<span style="color:orange;">代码清单：</span>动态代理对象创建过程

``` java [StubClient.java]
public class StubClient {
    public static void main(String[] args) throws Exception {
        StubClient client = new StubClient();
        //(2)通过为 Proxy 类指定 ClassLoader 对象和一组 interface 来创建动态代理类
        Class<?> proxyClass = Proxy.getProxyClass(Stub.class.getClassLoader(), new Class<?>[]{Subject.class});
        //(3)通过反射机制获得动态代理类的构造函数，其唯一参数类型是调用处理器接口类型
        Constructor<?> proxyClassConstructor = proxyClass.getConstructor(new Class<?>[]{InvocationHandler.class});
        //(4)通过构造函数创建动态代理类实例，构造时调用处理器对象作为参数被传入
        Object o = proxyClassConstructor.newInstance(client.handler);
        //(5)通过动态代理对象调用
        Subject subject = (Subject) o;
        //(6)输出委托类的结果
        subject.doSomething();
    }

    //(1)通过实现 InvocationHandler 接口创建自己的调用处理器
    private InvocationHandler handler = new InvocationHandler() {
        //创建委托类对象实例
        private RealSubject real = new RealSubject();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //调用委托类的方法
            return method.invoke(real, args);
        }
    };
}
/*
输出：
RealSubject doSomething
*/

```

<span style="color:orange;">代码清单：</span>简化创建过程

``` java
//简化上述(2)(3)(4)步骤
Object o = Proxy.newProxyInstance(Stub.class.getClassLoader(), new Class<?>[] {Subject.class}, client.handler);

```

## 2.动态代理类的属性

> - 如果所有的代理接口都是public的，那么代理类就是public、final的，切不是abstract的
- 动态代理类的名称以"$ProxyN"开头,N是代理类的唯一编号.
- 动态代理类都继承于java.lang.reflect.Proxy
- 动态代理类实现了其创建时指定的接口，且保持接口指定的顺序
- 如果动态代理类实现了一个非public接口，那么它将定义和接口相同的包名；否则代理类的包是不确定的，默认是com.sun.proxy,运行时，包密封性不防止特定包成功定义代理类；如果都不是，动态代理类将由同一个类加载器和相同的包与特定签名定义.
- 动态代理类实现了其创建时指定的所有接口，调用代理类Class对象的getInterfaces将返回和创建时指定接口顺序相同的列表，调用 getMethods方法返回所有接口方法的数组对象，调用getMethod会返回代理类接口中期望的method.
- 调用Proxy.isProxyClass方法时,传入Proxy.getProxyClass返回的Class或者Proxy.newProxyInstance返回对象的Class，都会返回true，否则返回false.
- 代理类的java.security.ProtectionDomain是由系统根类加载器加载的，代理类的代码也是系统信任的代码生成的，此保护域通常被授予java.security.AllPermission
- 每一个代理类都有一个public的，含有一个InvocationHandler实现为参数的构造方法，设置了调用处理器接口，就不必使用反射api访问构造方法，通过Proxy.newProxyInstance可以产生和Proxy.getProxyClass和调用句柄相同的调用构造函数行为.

## 3.动态代理实例的属性

> - 给定一个代理实例proxy，Foo实现的接口之一，表达式 proxy instanceof Foo 返回true,(Foo) proxy能成功转换.
- 每个代理实例都关联一个InvocationHandler， 通过Proxy.getInvocationHandler方法，将返回代理类关联的InvocationHandler.
- 代理类实例调用其代理接口中所声明的方法时，这些方法将被编码，并最终由调用处理器(InvocationHandler)的invoke方法执行.
- 代理类根类java.lang.Object中的hashCode,equals和toString方法，也会被分派到调用处理其的invoke方法执行；可能的原因有：一是因为这些方法为 public 且非 final 类型，能够被代理类覆盖；二是因为这些方法往往呈现出一个类的某种特征属性，具有一定的区分度，所以为了保证代理类与委托类对外的一致性，这三个方法也应该被分派到委托类执行。
- 当代理的一组接口有重复声明的方法且该方法被调用时，代理类总是从排在最前面的接口中获取方法对象并分派给调用处理器，而无论代理类实例是否正在以该接口（或继承于该接口的某子接口）的形式被外部引用，因为在代理类内部无法区分其当前的被引用类型。

## 4.获取动态代理类时需要注意哪些？

``` java [Proxy.java]
public static Class<?> getProxyClass(ClassLoader loader, Class<?>... interfaces)

```
通过指定的ClassLoader loader和有序的interfaces，ClassLoader将动态生成实现有序interfaces的代理类，如果这个ClassLoader已经定义过相同有序接口实现的代理类，那么将不在重复定义.

> - 所有interfaces中的对象必须都是接口，否则会抛出异常
- interfaces中的接口不能重复
- 所有接口相对指定的ClassLoader必须是可见的
- 所有的非public接口必须在同一个包中，否这不能成功生成实现所有接口的代理类.
- 代理类的接口数目不能超过65535，这个是JVM所限定的

当不满足上述限定中的一条或多条时，将会抛出IllegalArgumentException异常，如果interfaces中的接口对象一个或多个是null，也将抛出NullPointerException.
<span style="color:orange;">注意：</span>代理类指定的接口的顺序是很重要的，否则不通顺序的相同接口数组将会导致生成不同的代理类

## 5.从源码中理解动态代理类的生成
上面我们讲述了动态代理的使用，动态代理类的属性，动态代理实例的属性，以及获取动态代理类时需要注意的事项，下面我们从源码角度去观察这些东西

<span style="color:orange;">代码清单：</span>Proxy的重要变量

``` java
//构造器参数类型
private static final Class<?>[] constructorParams = { InvocationHandler.class };
//代理类缓存
private static final WeakCache<ClassLoader, Class<?>[], Class<?>> proxyClassCache = new WeakCache<>(new KeyFactory(), new ProxyClassFactory());
//关联的调用处理器
protected InvocationHandler h;

```

<span style="color:orange;">代码清单：</span>Proxy的构造方法

``` java
//私有构造函数，禁止外部调用
private Proxy() {}
// 通过子类指定一个调用处理器接口
protected Proxy(InvocationHandler h) {
    doNewInstanceCheck();
    this.h = h;
}

```

<span style="color:orange;">代码清单：</span>Proxy.newProxyInstance函数分析

``` java
    public static Object newProxyInstance(ClassLoader loader,
                                          Class<?>[] interfaces,
                                          InvocationHandler h)throws IllegalArgumentException {
        ....
        //(1)权限检查和校验
        checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
        ....
        //(2)查找已经存在的或动态生成生成代理类
        Class<?> cl = getProxyClass0(loader, intfs);
        ...
        //(3)通过反射调用含有调用处理器参数的构造函数生成动态代理类的实例对象并返回
        return newInstance(cons, ih);
    }

```

<span style="color:orange;">代码清单：</span>Proxy.getProxyClass函数分析

``` java
    public static Class<?> getProxyClass(ClassLoader loader,
                                         Class<?>... interfaces)throws IllegalArgumentException {
        ...
        //(1)权限检查和校验
        checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
        ...
        //(2)返回查找已经存在的或动态生成生成代理类
        return getProxyClass0(loader, intfs);
    }

```

<span style="color:orange;">代码清单：</span>Proxy.checkProxyAccess函数分析

``` java
/*
caller 调用Proxy.getProxyClass或Proxy.newProxyInstance接口的类
loader 调用接口时传入的ClassLoader
interfaces 调用接口时传入的接口列表
*/
private static void checkProxyAccess(Class<?> caller,
                                         ClassLoader loader,
                                         Class<?>... interfaces) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            ClassLoader ccl = caller.getClassLoader();
            if (loader == null && ccl != null) {
                if (!ProxyAccessHelper.allowNullLoader) {
                    //如果传入的ClassLoader是null，则要检查"getClassLoader"权限
                    sm.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
                }
            }
            /*
            如果接口列表中有一个接口不是public的，那么代理类应该由该接口的ClassLoader加载定义，如果caller的ClassLoader和接口的ClassLoader不相同，那么虚拟机将在
            生成代理类的defineClass0方法(参见java虚拟机类加载机制)中抛出IllegalAccessError
            */
            ReflectUtil.checkProxyPackageAccess(ccl, interfaces);
        }
    }
    
```

<span style="color:orange;">代码清单：</span>Proxy.getProxyClass0函数分析

``` java
/*
该函数作用是返回代理类的Class对象，在调用这个方法之前必须要调用checkProxyAccess方法来检查相应的权限
*/
private static Class<?> getProxyClass0(ClassLoader loader,
                                           Class<?>... interfaces) {
        if (interfaces.length > 65535) { //接口列表大小的限制
            throw new IllegalArgumentException("interface limit exceeded");
        }
        //如果通过指定的loader定义实现有序接口列表的代理类已经存在于缓存，那么返回缓存中的拷贝，否则通过ProxyClassFactory创建代理类
        return proxyClassCache.get(loader, interfaces);
    }
    
```
接下来就是代理类从缓存中获取代理类，jdk1.7中的缓存机制略显复杂，没有去深入研究，后期如有可能再补上跳过如下流程代码分析

> - 通过WeakCache的get方法获得代理类的Class对象
- 删除无效缓存，弱key和强subkey的缓存等（略过...）
- 通过WeakCache的内部类Factory的get方法调用Proxy.ProxyClassFactory的apply方法得到代理类的Class对象

<span style="color:orange;">代码清单：</span>ProxyClassFactory分析

``` java
private static final class ProxyClassFactory
        implements BiFunction<ClassLoader, Class<?>[], Class<?>> {
        // 所有代理类的前缀
        private static final String proxyClassNamePrefix = "$Proxy";

        // 生成代理类名称下一个唯一的编号，如$ProxyN
        private static final AtomicLong nextUniqueNumber = new AtomicLong();

        @Override
        public Class<?> apply(ClassLoader loader, Class<?>[] interfaces) {

            Map<Class<?>, Boolean> interfaceSet = new IdentityHashMap<>(interfaces.length);
            for (Class<?> intf : interfaces) {
                /*
                 * 校验代理接口对指定的ClassLoader是否可见，不可见抛出异常
                 */
                Class<?> interfaceClass = null;
                try {
                    interfaceClass = Class.forName(intf.getName(), false, loader);
                } catch (ClassNotFoundException e) {
                }
                if (interfaceClass != intf) {
                    throw new IllegalArgumentException(
                        intf + " is not visible from class loader");
                }
                /*
                 * 校验interface Class是否是接口，不是则抛出异常.
                 */
                if (!interfaceClass.isInterface()) {
                    throw new IllegalArgumentException(
                        interfaceClass.getName() + " is not an interface");
                }
                /*
                 * 校验接口类是否重复，如果重复则抛出异常.
                 */
                if (interfaceSet.put(interfaceClass, Boolean.TRUE) != null) {
                    throw new IllegalArgumentException(
                        "repeated interface: " + interfaceClass.getName());
                }
            }

            String proxyPkg = null;     // 声明代理类的包名

            /*
             * 记录是否所有的非public接口是否在相同的包下，如果是则代理类的包名是非public接口的包名，否则抛出异常.
             */
            for (Class<?> intf : interfaces) {
                int flags = intf.getModifiers();
                if (!Modifier.isPublic(flags)) {
                    String name = intf.getName();
                    int n = name.lastIndexOf('.');
                    String pkg = ((n == -1) ? "" : name.substring(0, n + 1));
                    if (proxyPkg == null) {
                        proxyPkg = pkg;
                    } else if (!pkg.equals(proxyPkg)) {
                        throw new IllegalArgumentException(
                            "non-public interfaces from different packages");
                    }
                }
            }

            if (proxyPkg == null) {
                // 如果没有非public的代理接口，包名就用com.sun.proxy
                proxyPkg = ReflectUtil.PROXY_PACKAGE + ".";
            }

            /*
             * 生成选定的代理类名称,如$ProxyN.
             */
            long num = nextUniqueNumber.getAndIncrement();
            String proxyName = proxyPkg + proxyClassNamePrefix + num;

            /*
             *通过ProxyGenerator类生成指定的代理类字节数组.
             */
            byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
                proxyName, interfaces);
            try {
                //通过指定ClassLoader生成代理类的Class对象
                return defineClass0(loader, proxyName,
                                    proxyClassFile, 0, proxyClassFile.length);
            } catch (ClassFormatError e) {
                /*
                 * 一些其他参数方面影响了代理类的创建异常.
                 */
                throw new IllegalArgumentException(e.toString());
            }
        }
    }
    
```

至此，动态代理的Class对象生成进入结尾，Proxy的isProxyClass方法和getInvocationHandler方法就比较清晰明显了，请读者自行分析.

## 6.动态代理类生成
事物往往不像其看起来的复杂，需要的是我们能够化繁为简，这样也许就能有更多拨云见日的机会.
<span style="color:orange;">代码清单：</span>代理类中方法调用的分派转发推演实现

``` java
public final class Proxy$0 extends java.lang.reflect.Proxy implements com.thinkdevos.java.dynamicproxy.Subject {
    private static Method m0; //为了代理调用高效，这里缓存了接口方法的实例
    .....
    private static Method mN; //同上还需要缓存hashCode,toString,equals方法实例
    ....

    static {//给静态变量mN赋值
        try {
            Class<?> subClass = Class.forName("com.thinkdevos.java.dynamicproxy.Subject");
            Method m0 = subClass.getMethod("doSomething");
            ....
        } catch (Exception e) {
            ...
        }
    }
    public Proxy$0(java.lang.reflect.InvocationHandler handler) {
        super(handler);
    }

    public final void doSomething() {
        try {
            //通过调用处理器调用
            handler.invode(this, mN, new Object[] {...});
        } catch(Exception e) {
            ....
        }
    }

    public final int hashCode() {
        try {
            handler.invode(this, mN, new Object[] {...});
        } catch(Exception e) {
            ....
        }
    }
}

```

<span style="color:orange;">代码清单：</span>代理类中方法调用的分派转发推演异常细化
用调用处理器调用方法时，在捕获方法本身抛出的异常后，还有可能有未知异常抛出，对于不支持的异常，必须抛 UndeclaredThrowableException 运行时异常.

``` java
    public final void doSomething() {
        try {
            //通过调用处理器调用
            handler.invode(this, mN, new Object[] {...});
        } catch(Exception e) {
            ....
        } catch(Throwable thr) {
            throw new UndeclaredThrowableException(e);
        }
    }
    
```

这样我们就完成了对动态代理类的推演实现。
下面我们就实例验证一番，通过如下代码生成字节码文件
<span style="color:orange;">工具类：</span>通过ProxyGenerator.generateProxyClass生成代理类字节数组并保存到文件中

``` java [ProxyUtils.java]
public class ProxyUtils {

    /**
     * Save proxy class to path
     *
     * @param path           path to save proxy class
     * @param proxyClassName name of proxy class
     * @param interfaces     interfaces of proxy class
     * @return
     */
    public static boolean saveProxyClass(String path, String proxyClassName, Class[] interfaces) {
        if (proxyClassName == null || path == null) {
            return false;
        }

        // get byte of proxy class
        byte[] classFile = ProxyGenerator.generateProxyClass(proxyClassName, interfaces);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(classFile);
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

```

<span style="color:orange;">运行生成Proxy$0.class</span>

``` java [ProxyClass.java]
public class ProxyClass {
    public static void main(String[] args) {
        ProxyUtils.saveProxyClass("/home/borney/tmp/Proxy$0.class", "Proxy$0",
            new Class<?>[]{Subject.class});
    }
}

```

通过'<span style="color:orange;">javap -p Proxy$0</span>'查看字节码文件基本信息

``` java
public final class Proxy$0 extends java.lang.reflect.Proxy implements com.thinkdevos.java.dynamicproxy.Subject {
  private static java.lang.reflect.Method m1;
  private static java.lang.reflect.Method m0;
  ...
  public Proxy$0(java.lang.reflect.InvocationHandler) throws ;
  ...
  public final int hashCode() throws ;
  public final void doSomething() throws ;
  ...
  static {} throws ;
}

```

通过'<span style="color:orange;">javap -v Proxy$0</span>'查看详细信息，我们主要看下调用处理器对doSomething调用和异常捕获处理(注释是自己加的，字节码文件中没有注释)

``` java
public final void doSomething() throws ;
    descriptor: ()V
    flags: ACC_PUBLIC, ACC_FINAL
    Code:
      stack=10, locals=2, args_size=1
         0: aload_0
         1: getfield      #16                 // Field java/lang/reflect/Proxy.h:Ljava/lang/reflect/InvocationHandler;
         4: aload_0
         5: getstatic     #60                 // Field m3:Ljava/lang/reflect/Method;
         8: aconst_null
         /*
         通过调用处理器调用方法
         */
         9: invokeinterface #28,  4           // InterfaceMethod java/lang/reflect/InvocationHandler.invoke:(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;
        14: pop
        15: return
        16: athrow
        17: astore_1
        /*
        抛出UndeclaredThrowableException异常
        */
        18: new           #42                 // class java/lang/reflect/UndeclaredThrowableException
        21: dup
        22: aload_1
        23: invokespecial #45                 // Method java/lang/reflect/UndeclaredThrowableException."<init>":(Ljava/lang/Throwable;)V
        26: athrow
      Exception table:
         from    to  target type
             0    16    16   Class java/lang/Error
             0    16    16   Class java/lang/RuntimeException
             0    16    17   Class java/lang/Throwable
    Exceptions:
      throws

```

## 7.动态代理的不足之处
动态代理只能支持接口的代理，这也是因为java的继承性本质所限制的，因为所有的动态代理类都继承了Proxy类，所以再也无法同时继承其他类.然而，我们不可否认动态代理设计的伟大之处，世上所有的事物都不可能完美.

后感：第一次写东西，东拼西凑的看了看，再加上自己的理解，希望能对大家有所帮助,并欢迎指正.
本文[[示例代码]](https://github.com/borneywpf/java-demos/tree/master/java/src/com/thinkdevos/java/dynamicproxy)

------

##参考资料

[Java 动态代理机制分析及扩展，第 1 部分](https://www.ibm.com/developerworks/cn/java/j-lo-proxy1/)
<!--[Java 动态代理机制分析及扩展，第 2 部分](https://www.ibm.com/developerworks/cn/java/j-lo-proxy2/)-->
[公共技术点之 Java 动态代理](http://a.codekk.com/detail/Android/Caij/%E5%85%AC%E5%85%B1%E6%8A%80%E6%9C%AF%E7%82%B9%E4%B9%8B%20Java%20%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86)
