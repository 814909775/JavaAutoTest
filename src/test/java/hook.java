import io.cucumber.java.Before;

import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

public class hook {

    private final ScenarioContext scenarioContext;
    public hook(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Before
    public void beforeScenario(Scenario scenario) throws MalformedURLException {
        WebDriver webDriver = WebAgentManager.getOrCreateDriver();
        scenarioContext.setContext(ScenarioContext.ContextKey.WEB_DRIVER,webDriver);
        scenario.log("Add Driver to Context");
        scenario.log("Start： "+scenario.getName());
    }
}
