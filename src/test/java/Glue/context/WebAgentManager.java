package Glue.context;

import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class WebAgentManager {
    private static final Logger logger = LoggerFactory.getLogger(WebAgentManager.class);
    //read the config
    private static final ThreadLocal<WebDriver> drivers = new ThreadLocal<>();
    private static String env;


    private static Map<Object, Object> getConfig() {
        String configPath = System.getProperty("user.dir") + "/config/config.yml";

        logger.info("Reading config file: {}", configPath);

        try (FileInputStream fis = new FileInputStream(configPath)) {
            Yaml yaml = new Yaml();
            return yaml.load(fis);
        } catch (IOException e) {
            logger.error("Failed to read config file: {}", configPath);
            throw new RuntimeException(e);
        }
    }

    public static WebDriver getOrCreateDriver() throws MalformedURLException {
        Map<Object, Object> configMap = getConfig();
        Object agentObj = configMap.get("Agent");
        Map<Object, Object> agentConfig = new HashMap<>();
        WebDriver webDriver = null;
        if (agentObj instanceof Map<?, ?> ) {
            Map<?, ?> rawMap = (Map<?, ?>) agentObj;
            agentConfig.putAll(rawMap);

        } else {
            // 处理Agent不是Map的情况
            System.err.println("Agent配置不是一个Map类型");
        }


        String browser = (String) agentConfig.get(ConfigKey.Browser.toString());
        Boolean webDriverManager = (Boolean) agentConfig.get(ConfigKey.WebDriverManager.toString());
        Boolean remote = (Boolean) agentConfig.get(ConfigKey.Remote.toString());
        String remoteUrl = (String) agentConfig.get(ConfigKey.RemoteUrl.toString());
        String driverpath = (String) agentConfig.get(ConfigKey.Path.toString());
        Boolean headless =(Boolean) agentConfig.get(ConfigKey.Headless.toString());
        List<String> options = Arrays.asList("--disable-gpu","--disable-extensions","--disable-dev-shm-usage","--start-maximized");

        if(drivers.get()==null) {
            if (remote) {
            switch (browser) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    options.forEach(chromeOptions::addArguments);
                    if (headless){ chromeOptions.addArguments("--headless");}
                    webDriver = new RemoteWebDriver(new URL(remoteUrl),chromeOptions);
                    break;
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    options.forEach(edgeOptions::addArguments);
                    if (headless){ edgeOptions.addArguments("--headless");}
                    webDriver = new RemoteWebDriver(new URL(remoteUrl),edgeOptions);
                    break;

            }

            } else if (webDriverManager) {
            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver()
                            .cachePath("remote-drivers")
                            /*.driverVersion("114.0.5735.90") 指定chrome driver版本*/
                            .setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    options.forEach(chromeOptions::addArguments);
                    if (headless){ chromeOptions.addArguments("--headless");}
                    webDriver = new ChromeDriver(chromeOptions);
                    break;
                case "edge":
                    WebDriverManager.edgedriver()
                            .cachePath("remote-drivers")
                            /*.driverVersion("114.0.5735.90") 指定chrome driver版本*/
                            .setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    options.forEach(edgeOptions::addArguments);
                    if (headless){ edgeOptions.addArguments("--headless");}
                    webDriver = new EdgeDriver(edgeOptions);

            }

            } else{
            System.setProperty("webdriver.chrome.driver", driverpath);
            System.setProperty("webdriver.edge.driver", driverpath);
            switch (browser) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    options.forEach(chromeOptions::addArguments);
                    if (headless) {
                        chromeOptions.addArguments("--headless");
                    }
                    webDriver = new ChromeDriver(chromeOptions);
                    break;
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    options.forEach(edgeOptions::addArguments);
                    if (headless) {
                        edgeOptions.addArguments("--headless");
                    }
                    webDriver = new EdgeDriver(edgeOptions);
                    break;
            }
            }

            drivers.set(webDriver);
        }


        return drivers.get();
    }

    public static void quitDriver() {
        if (drivers.get() != null) {
            drivers.get().quit();
            drivers.remove();
        }

    }



    public static void getAndAttachSreenshot(Scenario scenario) {
        if (scenario.isFailed()&&drivers.get()!=null) {
            scenario.log("Scenario is Failed");
            byte[] screenshot = ((TakesScreenshot)drivers.get()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName()+UUID.randomUUID().toString());
        }
    }


    public enum ConfigKey {
        Browser,
        WebDriverManager,
        Remote,
        RemoteUrl,
        Path,
        Headless,
        Environment
    }
}
