package org.observer.base.appium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.http.HttpClient;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/2/21 12:26
 */
public class Appium extends  AndroidDriver  {

    public static DesiredCapabilities genCap(String deviceName, String platformVersion, String appPackage, String appActivity, String appWaitActivity) throws MalformedURLException {

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.BROWSER_NAME, "");
        cap.setCapability("platformName", "Android"); //指定测试平台
        cap.setCapability("deviceName", deviceName); //指定测试机的ID,通过adb命令`adb devices`获取
        cap.setCapability("platformVersion", platformVersion);
        //将上面获取到的包名和Activity名设置为值
        cap.setCapability("appPackage", appPackage);
        cap.setCapability("appActivity", appActivity);

        //A new session could not be created的解决方法
        cap.setCapability("appWaitActivity",appWaitActivity);
        //每次启动时覆盖session，否则第二次后运行会报错不能新建session
        cap.setCapability("sessionOverride", true);
        return cap;
    }


    public Appium(HttpCommandExecutor executor, Capabilities capabilities) {
        super(executor, capabilities);
    }

    public Appium(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    public Appium(URL remoteAddress, HttpClient.Factory httpClientFactory, Capabilities desiredCapabilities) {
        super(remoteAddress, httpClientFactory, desiredCapabilities);
    }

    public Appium(AppiumDriverLocalService service, Capabilities desiredCapabilities) {
        super(service, desiredCapabilities);
    }

    public Appium(AppiumDriverLocalService service, HttpClient.Factory httpClientFactory, Capabilities desiredCapabilities) {
        super(service, httpClientFactory, desiredCapabilities);
    }

    public Appium(AppiumServiceBuilder builder, Capabilities desiredCapabilities) {
        super(builder, desiredCapabilities);
    }

    public Appium(AppiumServiceBuilder builder, HttpClient.Factory httpClientFactory, Capabilities desiredCapabilities) {
        super(builder, httpClientFactory, desiredCapabilities);
    }

    public Appium(HttpClient.Factory httpClientFactory, Capabilities desiredCapabilities) {
        super(httpClientFactory, desiredCapabilities);
    }

    public Appium(Capabilities desiredCapabilities) {
        super(desiredCapabilities);
    }


    public void back(int count){
        for (int i = 0; i < count; i++) {
            this.pressKey(new KeyEvent(AndroidKey.BACK));
        }
    }

}
