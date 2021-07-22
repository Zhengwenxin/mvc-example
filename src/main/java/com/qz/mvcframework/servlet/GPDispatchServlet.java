package com.qz.mvcframework.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GPDispatchServlet extends HttpServlet {

    //IOC容器
    private Map<String, Object> ioc = new HashMap<String, Object>();
    private Properties contextConfig = new Properties();
    private List<String, String> classNames;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    /**
     * 初始化，加载配置文件
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        doLoadConfig();
        doScanner();
        doInstance();
        doAutowired();
        doInitHandlerMapping();

    }

    private void doInitHandlerMapping() {

    }

    private void doAutowired() {

    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        for (String className : classNames) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class)) {
                    Object instance = clazz.newInstance();
                    String beanName = clazz.getSimpleName();
                    ioc.put(beanName, instance);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());

        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
            }

            String className = (scanPackage + "." + file.getName().replace(".class", ""));
            classNames.add(className);
        }
    }

    private void doLoadConfig() {

    }
}
