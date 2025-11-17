package com.own.Agent;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.beanutils.PropertyUtils;


import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class MyFunctions {
    public MyFunctions() {}



    public static  HashMap<String, Object> loadConfig() {
        Yaml yaml = new Yaml();
        HashMap<String, Object> map;
        try {
            FileInputStream fis = new FileInputStream(System.getProperty ("user.dir")+"/config/config.yml");
            map = yaml.load(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return map;

    }
    public static Map<String,String> getUserInfo(){

        HashMap<String, Object> map = loadConfig();
        // 修改 getUserInfo 方法以增强类型安全性
        @SuppressWarnings("unchecked")
        Map<String, String> userInfoMap = (Map<String, String>) map.get("UserInfo");
        if (userInfoMap == null) {
            throw new RuntimeException("YAML中未找到UserInfo节点");
        }
        return userInfoMap;

    }
    public static String getURL(String webSiteName){

        HashMap<String, Object> map = loadConfig();
        String url;

        try {
            String environmentName = System.getProperty ("env");
            if (environmentName == null) {
                environmentName = "Beta";
            }
            url = PropertyUtils.getProperty(map,"URL."+environmentName+"."+webSiteName).toString();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return url;
    }

    public static String getXpath(String pageName ,String element){

        Yaml yaml = new Yaml();
        HashMap<String, Object> map;
        StringBuilder xpathDic = new StringBuilder();
        xpathDic.append(pageName);
        xpathDic.append(".");
        xpathDic.append(element);
        String xpath;
        try {
            FileInputStream fis = new FileInputStream(System.getProperty ("user.dir")+"/repository/repository.yml");
            map = yaml.load(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            xpath = PropertyUtils.getProperty(map,xpathDic.toString()).toString();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("读取repository中的xpath失败"+ "key: "+xpathDic,e);
        }

        return xpath;

    }

    public static Response postRequest(String uri,String path, Map<String,String> header,String body){
        RestAssured.baseURI = uri;

        return RestAssured.given()
                .headers(header)
                .contentType("application/json")
                .body(body)
                .when()
                .post(path)
                .then()
                .statusCode(200) // 确保登录成功
                .extract()
                .response();

    }

    public static String getResponseField(Response response,String fieldName){
       return response.jsonPath().getString(fieldName);
    }


}
