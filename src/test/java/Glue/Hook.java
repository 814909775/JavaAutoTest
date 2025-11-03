package Glue;

import Agent.EmailUtils;
import Agent.UIClient;
import Glue.context.GlobalContext;
import Glue.context.ScenarioContext;
import Glue.context.WebAgentManager;
import io.cucumber.java.*;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Agent.EmailUtils.getEmailInfo;

public class Hook {

    private final ScenarioContext scenarioContext;
    public static String currentTime;
    private static final Logger logger = LoggerFactory.getLogger(Hook.class);
    public Hook(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }
    public static final List<Scenario> scenarioResults = new ArrayList<>();
    @Before
    public void beforeScenario(Scenario scenario) throws MalformedURLException {
        WebDriver webDriver = WebAgentManager.getOrCreateDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        UIClient UIClient = new UIClient(webDriver);
        scenarioContext.setContext(ScenarioContext.ContextKey.Agent, UIClient);
        scenario.log("Add Driver to Context");
        scenario.log("Start： "+scenario.getName());
        currentTime= UIClient.generateTimestamp();
    }

    @AfterStep
    public void afterStep(Scenario scenario) throws IOException {

    }
    @After
    public void afterScenario(Scenario scenario) throws IOException {
        //截图
        WebAgentManager.getAndAttachSreenshot(scenario);
        WebAgentManager.getOrCreateDriver().manage().deleteAllCookies();
       //清除driver
        WebAgentManager.quitDriver();
        //清除上下文
        scenarioContext.clear();
        //汇总scenario结果
        scenarioResults.add(scenario);
    }

    @AfterAll
    public static void afterAll() {
        GlobalContext.getInstance().clear();
    }
}
