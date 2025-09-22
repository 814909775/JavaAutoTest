package Glue;

import Agent.Agent;
import Glue.context.GlobalContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import Glue.context.ScenarioContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class BaseSteps {
    private ScenarioContext context;
    private final Agent agent;
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
        if(element.startsWith("@")){
            String text = GlobalContext.getInstance().get(element.replace("@","")).toString();
            agent.checkElementExists("//div[text()=' {element} ']".replace("{element}",text));
        }else{
            agent.checkElementExists(element);
        }

    }

    @When("Agent selects {string} in {string} input")
    public void agentSelectsInInput(String item, String input)  {
        agent.selectValue(item, input);


    }

    @When("Agent type {string} then select in {string} input")
    public void agentTypeThenSelectInInput(String item, String input) {
        agent.inputThenSelectValue(item, input);

    }


    @When("Agent type data into related fields")
    public void agentTypeDataIntoRelatedFields(DataTable dataTable) {
        List<Map<String, String>> allRows = dataTable.asMaps();
        for (int i = 0; i < allRows.size(); i++) {
            Map<String, String> row = allRows.get(i);
            String content = row.get("Content");
            String fieldName = row.get("fieldname");
            agent.typeAccordingToFieldName(content, fieldName);
        }

    }


    @Then("Agent sees {string} in  {string}")
    public void agentSeesIn(String text, String element) {
      agent.checkValueOfElement(text,element);
    }

    @Then("Agent get value from {string} and save to {string}")
    public void agentGetValueFrom(String field,String key) {
        String delegationNumber = agent.checkValueExist(field);
        if(key.startsWith("@")){

            GlobalContext.getInstance().set(key.replace("@",""),delegationNumber);
        }

        System.out.println("first save "+key+delegationNumber);
    }


}
