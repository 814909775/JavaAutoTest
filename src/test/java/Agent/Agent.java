package Agent;

import Glue.context.WebAgentManager;
import org.junit.Assert;
import org.openqa.selenium.*;
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
    private Actions actions;
    private static final Logger logger = LoggerFactory.getLogger(Agent.class);
    private static final Pattern XPATH_PATTERN = Pattern.compile(
            "^(/|//)|(@|contains\\(|text\\()",
            Pattern.CASE_INSENSITIVE
    );
    public Agent(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
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
        if(isXPath(elementName)){
            return getElementByXpath(elementName);
        }else{
            String xpath = getXpath(currentPage,elementName);
            logger.info("Element Xpath or Css selector is "+xpath);
            return getElementByXpath(xpath);
        }

    }

    public WebElement getElementByXpath(String xpath){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        if(isXPath(xpath)){

//            return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
           return  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        }
        else {
//            return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(xpath)));
            return  wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(xpath)));
        }
    }



    public void checkElementExists(String elementName) {

        WebElement element = null;
        try{
             element = getElement(elementName);
        } catch (Exception e) {
            Assert.assertNotNull("Cannot find element "+elementName,element);
            logger.error("Cannot find element "+elementName);
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

        WebElement element = getElement(elementName);
        new Actions(driver).moveToElement(element).pause(Duration.ofSeconds(1)).perform();
    }

    public void clickByCSS(String elementName) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String xpath=getXpath(currentPage,elementName);
        logger.info("Element Xpath or Css selector is "+xpath);
        js.executeScript("document.querySelector(arguments[0]).click()", xpath);
    }

    public void clickViaJS(String elementName) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        if(isXPath(elementName)){
            WebElement element = driver.findElement(By.xpath(elementName));
            new Actions(driver).moveToElement(element).pause(Duration.ofSeconds(1)).perform();
            js.executeScript("arguments[0].click();", element);
        }else{
            js.executeScript("arguments[0].click();", driver.findElement(By.xpath(getXpath(currentPage,elementName))));
        }

    }

    // 判断是否为XPath表达式
    public boolean isXPath(String selector) {
        if (selector == null || selector.trim().isEmpty()) {
            return false;
        }
        return XPATH_PATTERN.matcher(selector).find();
    }

    // 判断是否为CSS选择器
    public boolean isCssSelector(String selector) {
        // 不是XPath则默认为CSS选择器（简化判断）
        return !isXPath(selector);
    }

    public void inputThenSelectValue(String item, String input) throws InterruptedException {
        getElement(input).sendKeys(item);
        String xpath="//span[contains(text(),'{item}')]".replace("{item}", item);

        try{
            clickElement(xpath);
        }catch(Exception e){
            selectFirstItemByKeyboard(item, input);
        }
/*   调试元素代码
    getElement(input).click();
        String xpath = "//span[contains(text(),'{item}')]/parent::li".replace("{item}", item);
        WebElement target = driver.findElement(By.xpath(xpath));
        ((JavascriptExecutor) driver).executeScript("""
    var el = arguments[0];
    console.log("=== 元素状态排查 ===");
    console.log("1. 元素是否存在：", el !== null);
    console.log("2. CSS可见性：", el.style.display, el.style.visibility, el.style.opacity);
    console.log("3. 计算后样式：", window.getComputedStyle(el).display, window.getComputedStyle(el).visibility);
    console.log("4. 尺寸/位置：", el.getBoundingClientRect()); // 宽高、坐标
    console.log("5. 交互权限：", el.disabled, el.style.pointerEvents); // 是否禁用、是否拦截点击
    console.log("6. 父元素状态：", el.parentElement.style.pointerEvents); // 父元素是否拦截点击
""", target);
        target.click();*/
    }

    public void selectFirstItemByKeyboard(String item, String input) throws InterruptedException {
        actions.keyDown(Keys.ARROW_DOWN).keyUp(Keys.ARROW_DOWN).keyDown(Keys.ENTER).keyUp(Keys.ENTER).perform();
    }


}
