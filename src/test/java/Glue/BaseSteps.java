package Glue;

import Agent.Agent;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import Glue.context.ScenarioContext;
public class BaseSteps {
    private ScenarioContext context;
    private Agent agent;
    public static String currentPage;
    public BaseSteps(ScenarioContext scenarioContext) {
        this.context = scenarioContext;
        agent=context.getContext(ScenarioContext.ContextKey.Agent);
    }

    @When("Agent opens {string}")
    public void agentOpenUrl(String url) {
        agent.open(url);
    }

    @Given("Agent login {string}")
    public void agentLogin(String url) {
        // Write code here that turns the phrase above into concrete actions
        agentOpenUrl(url);
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
}
