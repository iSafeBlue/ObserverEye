package org.observer.base.plugin;

import net.dongliu.requests.RequestBuilder;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;
import org.javaweb.core.net.HttpURLRequest;
import org.observer.base.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/2/22 10:56
 */
public abstract  class WebPlugin extends Plugin {

    protected Session simpleRequest;

    protected HttpURLRequest request;

    @Autowired
    private DataRepository dataRepository;


    @Override
    public boolean init() throws Exception {
        try {
            simpleRequest  = Requests.session();

            request = new HttpURLRequest();

        }catch (Exception e){
            return false;
        }
        return true;
    }


    @Override
    public void close() {

    }
}

