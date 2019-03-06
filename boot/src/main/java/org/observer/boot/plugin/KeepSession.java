package org.observer.boot.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
@Rule(value = "keepSession", name = "维持会话不被销毁", param = {
        @Param(key = "url", desc = "请求地址" ),
        @Param(key = "cookie", desc = "你的cookie")
})
public class KeepSession extends WebPlugin {

    @Function(desc = "xss拿到cookie后，通过此插件可让会话在一段时间内不被服务器自动销毁", value = "keep")
    public String fun() throws MalformedURLException {

        String url = this.getParam().get("url");

        String cookie = this.getParam().get("cookie");

        String body = request.url(url).cookie(cookie).timeout(30000).get().body();

        Document parse = Jsoup.parse(body);

        Elements title = parse.getElementsByTag("title");

        return title.text();
    }

}
