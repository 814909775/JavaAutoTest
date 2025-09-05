package Agent;

import Glue.context.WebAgentManager;
import org.apache.commons.beanutils.PropertyUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MyFunctions {
    public MyFunctions() {}


    public static String getURL(String webSiteName){

        Yaml yaml = new Yaml();
        HashMap<String, Object> map = new HashMap<>();
        String url = "";
        try {
            FileInputStream fis = new FileInputStream(System.getProperty ("user.dir")+"/config/config.yml");
            map = yaml.load(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            url = PropertyUtils.getProperty(map,"URL."+webSiteName).toString();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return url;
    }

    public static String getXpath(String pageName ,String element){

        Yaml yaml = new Yaml();
        HashMap<String, Object> map = new HashMap<>();
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
            throw new RuntimeException(e);
        }

        return xpath;

    }


}
