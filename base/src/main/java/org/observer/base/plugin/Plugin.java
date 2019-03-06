package org.observer.base.plugin;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.observer.base.annotation.*;
import org.observer.base.dto.DataDTO;
import org.observer.base.dto.TaskDTO;
import org.observer.base.notice.MailNotice;
import org.observer.base.repository.DataRepository;
import org.observer.base.repository.TaskRepository;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/2/22 10:56
 */
public abstract class Plugin implements Job {


    public static final String FUNCTIONS = "functions";
    public static final String PARAM = "param";

    private Map<String,String> param;

    private List<String> function;

    public abstract boolean init() throws Exception;
    public abstract void close();

    @Autowired
    private DataRepository dataRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            if (!init())
                return;
            String groupName = jobExecutionContext.getJobDetail().getJobDataMap().getString("groupName");
            int taskId = jobExecutionContext.getJobDetail().getJobDataMap().getInt("taskId");

            JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();

            if (dataMap.containsKey(PARAM)){
                Map<String,String> param = (Map<String, String>) dataMap.get(PARAM);
                setParam(param);
            }

            Method before = thisBefore();

            if (before!=null){
                before.invoke(this,null);
            }


            if (dataMap.containsKey(FUNCTIONS))
            {
                List<String> funs  = (List<String>) dataMap.get(FUNCTIONS);
                setFunction(funs);

                for (Method method : thisFunctions()) {
                    Function f = method.getAnnotation(Function.class);
                    for (String fun : funs) {
                        if (fun.equals(f.value())){
                            String invoke = (String) method.invoke(this, null);
                            if (StringUtils.isNotBlank(invoke)){

                                List<DataDTO> datas = dataRepository.findAllByFunValAndGroupNameAndTid(f.value(), groupName, taskId);

                                Collections.sort(datas, new Comparator<DataDTO>() {

                                    @Override
                                    public int compare(DataDTO o1, DataDTO o2) {
                                        int flag = o2.getDate().compareTo(o1.getDate());
                                        return flag;
                                    }
                                });

                                DataDTO newData = new DataDTO();
                                newData.setDate(new Date());
                                newData.setFunDesc(f.desc());
                                newData.setFunVal(f.value());
                                newData.setGroupName(groupName);
                                newData.setTid(taskId);
                                newData.setResult(invoke);
                                if (datas.isEmpty()){
                                    dataRepository.save(newData);
                                    System.out.println("新插入:"+newData);
                                    this.notice(taskId,newData.toString());
                                }else{
                                    DataDTO frist = datas.get(0);
                                    if (invoke.equals(frist.getResult())){
                                        //..
                                        System.out.println("两值相等，不做操作");
                                    }else{
                                        dataRepository.save(newData);

                                        System.out.println("新插入:"+newData);

                                        this.notice(taskId,newData.toString());
                                    }

                                }

                            }else{
                                System.out.println("没有抓取到内容 "+new Date());
                            }


                        }
                    }
                }


            }

            Method after = thisAfter();

            if (after!=null){
                after.invoke(this, null);
            }

            close();

        } catch (Exception e) {
            close();
            throw new JobExecutionException(e);
        }

    }

    private final void notice( int taskid , String content){

        TaskDTO task = taskRepository.findById(taskid).get();
        if (ObjectUtils.allNotNull(task.getNoticer(),task)){

            String noticer = task.getNoticer();

            for (int i = 0; i < 3; i++) {
                if (this.notice(noticer,content.toString())){
                    break;
                }
            }

        }

    }
    private final boolean notice(String email,String content){
        String title = "您有一条来自观察者监控的新消息";

        try {
            MailNotice.sendEmail(email,title,content);
        }catch (Exception e){
            return false;
        }
        return true;
    }


    public App thisApp(){
        return this.getClass().getAnnotation(App.class);
    }

    public Method thisBefore(){
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)){
                return method;
            }
        }
        return null;
    }

    public Method thisAfter(){
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(After.class)){
                return method;
            }
        }
        return null;
    }

    public Rule thisRule(){
        return this.getClass().getAnnotation(Rule.class);
    }

    public List<Method> thisFunctions(){
        ArrayList<Method> methods = new ArrayList<>();
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Function.class)){
                methods.add(method);
            }
        }
        return methods;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    public List<String> getFunction() {
        return function;
    }

    public void setFunction(List<String> function) {
        this.function = function;
    }
}
