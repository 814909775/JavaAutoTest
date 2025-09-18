package Glue;

import Agent.Agent;
import Glue.context.WebAgentManager;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import Glue.context.ScenarioContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseSteps {
    private ScenarioContext context;
    private Agent agent;
    public static String currentPage;
    private static final Logger logger = LoggerFactory.getLogger(BaseSteps.class);
    public BaseSteps(ScenarioContext scenarioContext) {
        this.context = scenarioContext;
        agent=context.getContext(ScenarioContext.ContextKey.Agent);
    }

    @When("Agent opens {string}")
    public void agentOpenUrl(String url) {
        agent.open(url);

    }


    @Given("Agent type {string} into {string}")
    public void agentTypeInto(String text, String field) {
        agent.typeDataIntoField(text,field);

    }

    @Then("Agent is on {string} page" )
    public void agentIsOn(String pageName) {
        currentPage=pageName;
        agent.checkElementExists("trait");

    }

    @And("Agent clicks {string}")
    public void agentClicks(String element) {
        agent.clickElement(element);
    }

    @When("Agent moves to {string}")
    public void agentMovesTo(String element) throws InterruptedException {
        agent.moveToElement(element);

    }

    @And("Agent clicks {string} via CSS")
    public void agentClicksViaJS(String element) {
      agent.clickByCSS(element);
    }

    @Then("Agent sees {string}")
    public void agentSees(String element) {
        agent.checkElementExists(element);
    }

    @When("Agent selects {string} in {string} input")
    public void agentSelectsInInput(String item, String input) throws InterruptedException {
        agent.inputThenSelectValue(item, input);
        Thread.sleep(5000);

    }


}
