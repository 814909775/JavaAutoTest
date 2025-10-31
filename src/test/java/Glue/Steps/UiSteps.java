package Glue.Steps;

import Agent.UIClient;
import Glue.context.GlobalContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import Glue.context.ScenarioContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class UiSteps {
    private ScenarioContext context;
    private final UIClient UIClient;
    public static String currentPage;
/*    public static String currentTime;*/
    private static final Logger logger = LoggerFactory.getLogger(UiSteps.class);
    public UiSteps(ScenarioContext scenarioContext) {
        this.context = scenarioContext;
        UIClient =context.getContext(ScenarioContext.ContextKey.Agent);
    }

    @When("UIClient opens {string}")
    public void agentOpenUrl(String url) {
        UIClient.open(url);
/*        currentTime=agent.generateTimestamp();*/

    }


    @Given("UIClient type {string} into {string}")
    public void agentTypeInto(String text, String field) {
        UIClient.typeDataIntoField(text,field);

    }

    @Then("UIClient is on {string} page" )
    public void agentIsOn(String pageName) {
        currentPage=pageName;
        UIClient.checkElementExists("trait");


    }

    @And("UIClient clicks {string}")
    public void agentClicks(String element) {
        UIClient.clickElement(element);
    }

    @When("UIClient moves to {string}")
    public void agentMovesTo(String element) throws InterruptedException {
        UIClient.moveToElement(element);

    }

    @And("UIClient clicks {string} via CSS")
    public void agentClicksViaJS(String element) {
      UIClient.clickByCSS(element);
    }

    @Then("UIClient sees {string}")
    public void agentSees(String element) {
        if(element.startsWith("@")){
            String text = GlobalContext.getInstance().get(element.replace("@","")).toString();
            UIClient.checkElementExists("//div[text()=' {element} ']".replace("{element}",text));
        }else{
            UIClient.checkElementExists(element);
        }

    }

    @When("UIClient selects {string} in {string} input")
    public void agentSelectsInInput(String item, String input)  {
        UIClient.selectValue(item, input);


    }

    @When("UIClient type {string} then select in {string} input")
    public void agentTypeThenSelectInInput(String item, String input) {
        UIClient.inputThenSelectValue(item, input);

    }


    @When("UIClient type data into related fields")
    public void agentTypeDataIntoRelatedFields(DataTable dataTable) {
        List<Map<String, String>> allRows = dataTable.asMaps();
        for (int i = 0; i < allRows.size(); i++) {
            Map<String, String> row = allRows.get(i);
            String content = row.get("Content");
            String fieldName = row.get("fieldname");
            UIClient.typeAccordingToFieldName(content, fieldName);
        }

    }


    @Then("UIClient sees {string} in  {string}")
    public void agentSeesIn(String text, String element) {
      UIClient.checkValueOfElement(text,element);
    }

    @Then("UIClient get value from {string} and save to {string}")
    public void agentGetValueFrom(String field,String key) {
        String delegationNumber = UIClient.checkValueExist(field);
        if(key.startsWith("@")){

            GlobalContext.getInstance().set(key.replace("@",""),delegationNumber);
        }

        System.out.println("first save "+key+delegationNumber);
    }


    @Given("UIClient login HGJBooking via API")
    public void agentLoginViaAPI() {
        try {
            UIClient.apiLogin();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
