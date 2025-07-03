package Glue;

import Agent.Agent;
import Glue.context.ScenarioContext;
import Glue.context.WebAgentManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

public class Hook {

    private final ScenarioContext scenarioContext;
    public Hook(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Before
    public void beforeScenario(Scenario scenario) throws MalformedURLException {
        WebDriver webDriver = WebAgentManager.getOrCreateDriver();
        Agent agent = new Agent(webDriver);
        scenarioContext.setContext(ScenarioContext.ContextKey.Agent,agent);
        scenario.log("Add Driver to Context");
        scenario.log("Start： "+scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        //截图
        WebAgentManager.getAndAttachSreenshot(scenario);
        //清除driver
        WebAgentManager.quitDriver();
        //清除上下文
        scenarioContext.clear();

    }
}
