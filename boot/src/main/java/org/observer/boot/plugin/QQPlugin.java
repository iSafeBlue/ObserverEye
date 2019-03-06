package org.observer.boot.plugin;

import org.apache.commons.lang3.StringUtils;
import org.javaweb.core.net.HttpResponse;
import org.javaweb.core.net.HttpURLRequest;
import org.observer.base.annotation.*;
import org.observer.base.plugin.AndroidPlugin;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.List;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/2/22 13:37
 */
@Rule(value = "qqPlugin", name = "qq动态监控插件", param = {
        @Param(key = "number", desc = "监控者QQ账号"),
        @Param(key = "password", desc = "监控者QQ密码"),
        @Param(key = "target", desc = "监控目标账号")})
@App(packageName = "com.tencent.mobileqq", activity = "com.tencent.mobileqq.activity.SplashActivity", waitActivity = "com.tencent.mobileqq.activity.LoginActivity")
public class QQPlugin extends AndroidPlugin{

    @Before
    public void before(){
        System.out.println("before");
    }

    @Function(desc = "在线状态", value = "online")
    public String getOnline() throws InterruptedException {

        waitElement( By.id("com.tencent.mobileqq:id/btn_login")).click();

        Thread.sleep(500);

        List<WebElement> e = driver.findElementsByClassName("android.widget.EditText");
        WebElement user = e.get(0);
        WebElement pass = e.get(1);
        user.sendKeys(getParam().get("number"));
        pass.sendKeys(getParam().get("password"));

        driver.findElementById("com.tencent.mobileqq:id/login").click();

        WebElement search = waitElement(By.id("com.tencent.mobileqq:id/et_search_keyword"));
        search.click();

        search.sendKeys(getParam().get("target"));

        List<WebElement> ee = driver.findElementsById("com.tencent.mobileqq:id/name");

        for (WebElement webElement : ee) {
            String text = webElement.getText();
            if (StringUtils.equals(text,"("+getParam().get("target")+")")){
                webElement.click();

                WebElement sub = waitElement(By.id("com.tencent.mobileqq:id/title_sub"));

                String result = sub.getText();

                return result;
            }

        }

        return null;
    }


    @After
    public void after(){
        System.out.println("after");
    }


}
