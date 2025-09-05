package Agent;

import Glue.context.WebAgentManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static Agent.MyFunctions.getXpath;
import static Glue.BaseSteps.currentPage;

public class Agent {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(Agent.class);
    public Agent(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void open(String webPage) {
        driver.get(MyFunctions.getURL(webPage));
    }


    public WebElement getElement(String elementName) {
       String xpath = getXpath(currentPage,elementName);

       return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

    }

    public void checkElementExists(String elementName) {
        String xpath = getXpath(currentPage,elementName);
        WebElement element = null;
        try{
             element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        } catch (Exception e) {
            Assert.assertNotNull("Cannot find element "+xpath,element);

        }

    }

    public void clickElement(String elementName) {
        getElement(elementName).click();
    }



    public void typeDataIntoField(String text, String field) {
        WebElement element = getElement(field);
        element.sendKeys(text);
    }
}
