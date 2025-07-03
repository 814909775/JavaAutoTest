package Glue;

import Agent.Agent;
import io.cucumber.java.en.When;

import Glue.context.ScenarioContext;
public class BaseSteps {
    private ScenarioContext context;
    private Agent agent;
    public BaseSteps(ScenarioContext scenarioContext) {
        this.context = scenarioContext;
        agent=context.getContext(ScenarioContext.ContextKey.Agent);
    }

    @When("Agent opens {string} page")
    public void agentOpenCBusOLUrl(String url) {
        agent.open(url);
    }
}
