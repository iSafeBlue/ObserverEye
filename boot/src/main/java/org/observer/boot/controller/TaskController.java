package org.observer.boot.controller;

import org.observer.base.annotation.Function;
import org.observer.base.annotation.Param;
import org.observer.base.annotation.Rule;
import org.observer.base.plugin.AndroidPlugin;
import org.observer.base.plugin.WebPlugin;
import org.observer.base.repository.DataRepository;
import org.observer.base.dto.DataDTO;
import org.observer.base.entity.User;
import org.observer.base.plugin.Plugin;
import org.observer.base.quartz.QuartzManager;
import org.observer.base.repository.TaskRepository;
import org.observer.base.dto.TaskDTO;
import org.observer.boot.query.TaskQuery;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/2/22 13:51
 */
@RequestMapping({"/task","/"})
@Controller
public class TaskController {

    @Autowired
    private WebApplicationContext webApplicationConnect;

    @Autowired
    private QuartzManager quartzManager;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private DataRepository dataRepository;

    @RequestMapping("startup")
    public String statup(@RequestParam Map<String,String> param,
                         @RequestParam String[] fun ,
                         TaskQuery query ,
                         HttpSession session , Model model) {

        User user = new User();

        param.remove("fun");
        param.remove("pluginname");
        param.remove("jobname");
        param.remove("minutes");
        param.remove("repeat");
        param.remove("email");

        if ( fun==null ) {
            model.addAttribute("msg","缺少功能参数");
            return "error";
        }

        String pluginname = query.getPluginname();

        try {
            Plugin bean = webApplicationConnect.getBean(pluginname, Plugin.class);

            SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(query.getMinutes()).withRepeatCount(query.getRepeat());

            TaskDTO t = new TaskDTO();
            t.setGroupname(user.getUsername());
            t.setUid(user.getId());
            t.setJobname(query.getJobname());
            t.setDate(new Date());
            t.setNoticer(query.getEmail());

            TaskDTO save = taskRepository.save(t);


            quartzManager.addJob(query.getJobname(),save.getId(),save.getGroupname(),bean.getClass(),fun,param,builder);

        }catch (Exception e){
            model.addAttribute("msg",e.getMessage());
            return "error";
        }

        return "redirect:datas";
    }

    @RequestMapping("datas")
    public String datas(HttpSession session ,Model model){
        User user = new User();

        List<TaskDTO> tasks = taskRepository.findAllByUid(user.getId());

        ArrayList<Object> result = new ArrayList<>();

        for (TaskDTO task : tasks) {
            HashMap<Object, Object> map = new HashMap<>();
            List<DataDTO> datas = dataRepository.findAllByTid(task.getId());
            map.put("task",task);
            map.put("data",datas);
            result.add(map);
        }
        model.addAttribute("result",result);
        return "datas";
    }

    @RequestMapping("/")
    public String main(Model model){
        Map<String, AndroidPlugin> androidPlugin = webApplicationConnect.getBeansOfType(AndroidPlugin.class);
        Map<String, WebPlugin> webPlugin = webApplicationConnect.getBeansOfType(WebPlugin.class);

        HashMap<String, String> ap = new HashMap<>();
        HashMap<String, String> wp = new HashMap<>();

        for (Map.Entry<String, AndroidPlugin> e : androidPlugin.entrySet()) {
            ap.put(e.getKey(),e.getValue().thisRule().name());
        }

        for (Map.Entry<String, WebPlugin> e : webPlugin.entrySet()) {
            wp.put(e.getKey(),e.getValue().thisRule().name());
        }

        model.addAttribute("ap",ap);
        model.addAttribute("wp",wp);
        return "plugins";
    }

    @RequestMapping("start/{key}")
    public String start(@PathVariable String key , Model model){

        Plugin bean = webApplicationConnect.getBean(key, Plugin.class);

        Rule rule = bean.getClass().getAnnotation(Rule.class);

        Param[] param = rule.param();
        List<Map<String,String>> maps = new ArrayList<>();
        for (Param p : param) {
            HashMap<String, String> map = new HashMap<>();
            map.put("key",p.key());
            map.put("desc",p.desc());
            maps.add(map);
        }
        ArrayList<Map> funcs = new ArrayList<>();
        for (Method method : bean.thisFunctions()) {
            Function function = method.getAnnotation(Function.class);
            HashMap<String, String> map = new HashMap<>();
            map.put("desc",function.desc());
            map.put("value",function.value());
            funcs.add(map);
        }

        model.addAttribute("funcs",funcs);
        model.addAttribute("param",maps);
        model.addAttribute("pluginname",key);

        return "start";
    }

}
