
# ObserverEye

ObserverEye（观察者）是一个用于监控 Android & WEB 应用的轻量级开源程序。

Android 部分使用 Appium 实现，任务调度使用 Quartz ，基础框架使用 SpringBoot，持久层使用 JPA & HSQLDB嵌入式数据库。

# 如何使用

## 安装
- 安装 JDK 1.8
- 安装 Maven
- 安装 Appium 1.8+
- 安装 Android 模拟器（如：蓝叠、海马、夜神）
- 安装 Android SDK（用于辅助开发插件）

## 运行
- git clone 本项目
- 修改 application.properties 文件中的配置
    - `database.dir` 数据库文件存放路径
    - `email.from` 邮件发件人
    - `email.username` 邮箱服务器用户名
    - `email.password` 邮箱服务器密码
    - `email.host` 邮箱服务器
    - `appium.hub` Appium 的服务中心URL
    - `appium.platform.version` 你的安卓模拟器版本号
    - `appium.device.name` 安卓模拟器在 `adb devices` 中的设备名

- 在项目根目录下执行`mvn clean package`命令编译打包 或 在 IDEA 中打开本项目并编译执行 `ObserverEyeBootApplication.java` 

# 插件

## Android 插件


在 boot 项目中的 `org.observer.boot.plugin` 包下创建类。

e.g : QQPlugin.java

注意：
- 需继承 AndroidPlugin 类
- 在类上使用 `@Rule` 和 `@App` 注解
- 通过 driver 对象的方法来操控 App

### 关于 @Rule 注解

该注解共有三个参数，使用该注解则认为这是一个插件，默认需填写 name 和 param。

```
    String value() default "";

    String name() ;

    Param[] param();
```


- name ：插件名

- param ：需要前台传入的参数。里面存放 @Param 注解 e.g: `@Rule(param = {@Param(...),@Param(...)})`


### 关于 @App 注解

这是 Android 插件独有的注解，其中包含三个参数
```
    String packageName();  // App 的包名
    String activity();      // App 的 Activty 名
    String waitActivity();  // App 的 等待 Activty 名
```

e.g: `@App(packageName = "com.tencent.mobileqq", activity = "com.tencent.mobileqq.activity.SplashActivity", waitActivity = "com.tencent.mobileqq.activity.LoginActivity")`


包名和activity可通过 `adb shell dumpsys activity activities` and `adb logcat |grep ActivityManager` 等命令查询。

### 关于@Function @Before @After 注解

这些都是在方法上注明的

@Function 注解用来声明插件的一个功能，value不可以是中文，有多个Function 会优先按照方法在代码中的顺序执行。

@After 在功能执行后调用

@Before 在功能执行前调用

## Web 插件

在 boot 项目中的 `org.observer.boot.plugin` 包下创建类。

e.g: KeepSession.java

注意：
- 需要继承 WebPlugin 类
- 使用 @Rule 注解
- http协议网站可使用 simpleRequest 对象发送请求，[http/https] 使用 request 对象发送请求。


# 一些想法

最开始写这个程序是因为学习 Appium，后来衍生出一些应用在日常的想法。

比如：

- 监控某个 QQ 好友在一段时间内的在线状态（何时使用WIFI、4G 或 电脑）
- 监控某个 QQ / 微信好友的步数状态变化来判断他可能在做什么
- 监控某个社交账号上的最新一条动态的变化
- 监控网页中的某处功能变化，便于在第一时间知道网站改版，发现新漏洞
- 在XSS攻击完成后，通过不断请求来保持SESSION不被服务器销毁
- ...
