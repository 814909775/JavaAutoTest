package Agent;

import Glue.context.WebAgentManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.regex.Pattern;

import static Agent.MyFunctions.getXpath;
import static Glue.BaseSteps.currentPage;

public class Agent {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(Agent.class);
    private static final Pattern XPATH_PATTERN = Pattern.compile(
            "^(/|//)|(@|contains\\(|text\\()",
            Pattern.CASE_INSENSITIVE
    );
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
       logger.info("Element Xpath or Css selector is "+xpath);
        if(isXPath(xpath)){
            return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        }
        else {
            return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(xpath)));
        }

    }

    public void checkElementExists(String elementName) {
        String xpath = getXpath(currentPage,elementName);
        WebElement element = null;
        try{
             element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        } catch (Exception e) {
            Assert.assertNotNull("Cannot find element "+xpath,element);
            logger.error("Cannot find element "+xpath);
        }

    }

    public void clickElement(String elementName) {
        getElement(elementName).click();
    }



    public void typeDataIntoField(String text, String field) {
        WebElement element = getElement(field);
        element.sendKeys(text);
    }

    public void moveToElement(String elementName) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement element = getElement(elementName);
        new Actions(driver).moveToElement(element).pause(Duration.ofSeconds(1)).perform();
    }

    public void clickByCSS(String elementName) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String xpath=getXpath(currentPage,elementName);
        logger.info("Element Xpath or Css selector is "+xpath);
        js.executeScript("document.querySelector(arguments[0]).click()", xpath);
    }

    // 判断是否为XPath表达式
    public static boolean isXPath(String selector) {
        if (selector == null || selector.trim().isEmpty()) {
            return false;
        }
        return XPATH_PATTERN.matcher(selector).find();
    }

    // 判断是否为CSS选择器
    public static boolean isCssSelector(String selector) {
        // 不是XPath则默认为CSS选择器（简化判断）
        return !isXPath(selector);
    }
}
