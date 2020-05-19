package com.maowudi.demo.xspring.mvcframework.servlet;

import com.maowudi.demo.xspring.mvcframework.annotation.XAutowired;
import com.maowudi.demo.xspring.mvcframework.annotation.XController;
import com.maowudi.demo.xspring.mvcframework.annotation.XRequestMapping;
import com.maowudi.demo.xspring.mvcframework.annotation.XService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class XDispatcherServlet extends HttpServlet {

    public XDispatcherServlet(){super();}

    private String location = "contextConfigLocation";

    private Properties p = new Properties();

    private ArrayList<String> classNames = new ArrayList<String>();

    private Map<String,Object> ioc = new HashMap<String,Object>();

    private Map<String, Method> handlerMapping = new HashMap<String, Method>();
    /**
     * 初始化
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("-------------------------->开始初始化");
        //加载配置
         doLoadConfig(config.getInitParameter(location));
         //扫描所以相关类
        doScanner(p.getProperty("scanPackage"));
        //初始化类
        doInstance();
        //注入
        doAutowired();
        //构造mapping
        initHandlerMapping();
    }

    /**
     * 将GPRequestMapping中配置的信息和Method进行关联，并保存这些关系。
     */
    private void initHandlerMapping() {
        System.out.println("------------>进行请求url与method匹配");

        if(ioc.isEmpty())return;

        for(Map.Entry<String,Object> entry: ioc.entrySet()){
            Class<?> aClass = entry.getValue().getClass();
            //判断是否是Controller类
            if(!aClass.isAnnotationPresent(XController.class)){
                continue;
            }
            //获取主路径
            String baseUrl = "";
            if (aClass.isAnnotationPresent(XRequestMapping.class)){
                XRequestMapping annotation = aClass.getAnnotation(XRequestMapping.class);
                baseUrl = annotation.value();
            }
            //获取类的所有方法
            Method[] methods = aClass.getMethods();
            //获取method的路径
            for (Method method : methods){
                if (method.isAnnotationPresent(XRequestMapping.class)){
                    XRequestMapping annotation = method.getAnnotation(XRequestMapping.class);
                    String url = ("/"+baseUrl+"/"+annotation.value()).replaceAll("/+","/");
                    handlerMapping.put(url,method);
                }
            }
        }
    }

    /**
     * 加载配置
     * @param location
     */
    private void doLoadConfig(String location){
        System.out.println("----------->加载配置文件");
        InputStream stream = null;
        try {
            stream = this.getClass().getClassLoader().getResourceAsStream(location);
            p.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream!=null){
                    stream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * 扫描Class文件
     * @param packageName
     */
    private void doScanner(String packageName){
        System.out.println("------------->扫描满足条件的class文件");
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file: dir.listFiles()){
            if (file.isDirectory()){
                doScanner(packageName+"."+file.getName());
            }else {
                classNames.add(packageName + "."+file.getName().replaceAll(".class","").trim());
            }
        }
    }

    /**
     * 首字母转小写
     * @param str
     * @return
     */
    public String lowerFirstCase(String str){
        char[] chars = str.toCharArray();
        chars[0]=Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }

    /**
     * 初始化类
     */
    private void doInstance(){
        System.out.println("------------->进行初始化类IOC");
        if(classNames.size()==0) return;
        try {
            for (String className : classNames) {
                Class<?> aClass = aClass = Class.forName(className);
                if (aClass.isAnnotationPresent(XController.class)) {
                    //默认小写
                    String beanName = lowerFirstCase(aClass.getName());
                    ioc.put(beanName,aClass.newInstance());
                }else if (aClass.isAnnotationPresent(XService.class)){
                    XService xservice = aClass.getAnnotation(XService.class);
                    String beanName = xservice.value();
                    //设置了name
                    if (!"".equals(beanName)){
                        ioc.put(beanName,aClass.newInstance());
                        continue;
                    }
                    //未设置
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (Class<?> i : interfaces){
                        ioc.put(lowerFirstCase(i.getName()),aClass.newInstance());
                    }
                }else {
                    continue;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注入
     */
    private void doAutowired(){
        System.out.println("----------->进行依赖注入DI");
        if(ioc.isEmpty())return;
        for (Map.Entry<String,Object> entry : ioc.entrySet()){
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields){
                if(!field.isAnnotationPresent(XAutowired.class)) continue;
                XAutowired xAutowired = field.getAnnotation(XAutowired.class);
                String beanName = xAutowired.value();
                if ("".equals(beanName)){
                    beanName = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    /**
     * 获取对应的处理器
     * @param request
     * @param response
     */
    private void doDispatcher(HttpServletRequest request,HttpServletResponse response) throws IOException {
        System.out.println("------->进行匹配对于方法");
        if (handlerMapping.isEmpty())return;
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replace(contextPath,"").replaceAll("/+","/");
        if(!handlerMapping.containsKey(url)){
            response.getWriter().write("404!!!");
        }
        //获取请求的参数
        Map<String,String[]> parameterMap = request.getParameterMap();
        //获取该请求对应的方法
        Method method = handlerMapping.get(url);
        System.out.println("------->对应方法："+method.getName());
        //获取方法的参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        //保存参数值
        Object[] params = new Object[parameterTypes.length];
        for (int i=0;i<parameterTypes.length;i++){
            if (parameterTypes[i]==HttpServletRequest.class){
                params[i] = request;
                continue;
            }
            if(parameterTypes[i]==HttpServletResponse.class){
                params[i] = response;
                continue;
            }
            if(parameterTypes[i]==String.class){
                for (Map.Entry<String,String[]> entry : parameterMap.entrySet()){
                    String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                    params[i]= value;
                }
            }
        }
        System.out.println("------------>参数："+ Arrays.toString(params));
        //利用反射调用
        String beanName = lowerFirstCase(method.getDeclaringClass().getName());
        System.out.println("------------->匹配对于类："+beanName);
        try {
            method.invoke(ioc.get(beanName),params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    /**
     * 处理业务
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doDispatcher(req,resp);
    }
}
