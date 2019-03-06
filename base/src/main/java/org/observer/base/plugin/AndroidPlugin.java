package org.observer.base.plugin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dongliu.requests.Requests;
import org.apache.commons.lang3.StringUtils;
import org.observer.base.annotation.App;
import org.observer.base.annotation.Function;
import org.observer.base.appium.Appium;
import org.observer.base.repository.DataRepository;
import org.observer.base.dto.DataDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/2/22 10:56
 */
public abstract class AndroidPlugin extends Plugin {

    protected Appium driver ;


    public WebElement waitElement(By by) throws InterruptedException {
        try {
            WebElement webElement = waitElement(30, by);
            if (webElement!=null)
                return webElement;
        }catch (Exception e){
            Thread.sleep(20000);
        }
        return waitElement(30,by);
    }

    public WebElement waitElement(int maxSleep , By by)  {
        WebDriverWait wait = new WebDriverWait(driver, maxSleep);
        WebElement e =  wait.until(new ExpectedCondition<WebElement>(){
            @Override
            public WebElement apply(WebDriver d) {
                return d.findElement(by);
            }});

        return e;
    }
    private int retry = 0;
    @Override
    public boolean init() throws MalformedURLException, InterruptedException {

        if (hasSession()){
            startUpApp();
            return true;
        }else if(retry<10){
            retry++;
            Thread.sleep(10000);
            init();
        }else if (retry==10){
            startUpApp();
            return true;
        }
        return false;
    }

    public boolean hasSession(){
        String url = hub.concat("/sessions");
        String text = Requests.get(url).send().readToText();
        JSONObject json = JSONObject.parseObject(text);
        JSONArray value = json.getJSONArray("value");
        return value.isEmpty();
    }

    @Override
    public void close() {
        if (driver!=null && hasSession() ){
            driver.quit();
        }
    }


    @Value("${appium.hub}")
    private String hub;

    @Value("${appium.platform.version}")
    private String platformVersion;

    @Value("${appium.device.name}")
    private String deviceName ;

    public void startUpApp() throws MalformedURLException {

        App app = thisApp();
        driver = new Appium(
                new URL(hub),
                Appium.genCap(deviceName,platformVersion,app.packageName(),app.activity(),app.waitActivity()));

    }

}
