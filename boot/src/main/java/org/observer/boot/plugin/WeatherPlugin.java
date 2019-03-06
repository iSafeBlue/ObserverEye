package org.observer.boot.plugin;

import org.apache.http.HttpRequest;
import org.javaweb.core.net.HttpURLRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.observer.base.annotation.Function;
import org.observer.base.annotation.Param;
import org.observer.base.annotation.Rule;
import org.observer.base.plugin.WebPlugin;

import java.net.MalformedURLException;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/3/5 17:23
 */
@Rule(value = "weatherPlugin", name = "监控天气的变化示例插件", param = {})
public class WeatherPlugin extends WebPlugin {

    private String url  = "http://www.weather.com.cn/weather/101010100.shtml";
    @Function(desc = "监控天气变化", value = "test")
    public String test() throws MalformedURLException {

        String body = this.request.url(url).get().body();

        Document parse = Jsoup.parse(body);

        Element hidden_title = parse.getElementById("hidden_title");

        String val = hidden_title.val();

        return val;
    }


}
