package Agent;

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
    private final WebDriverWait wait;
    private final Actions actions;
    private final JavascriptExecutor js;
    private static final Logger logger = LoggerFactory.getLogger(Agent.class);
    private static final Pattern XPATH_PATTERN = Pattern.compile(
            "^(/|//)|(@|contains\\(|text\\()",
            Pattern.CASE_INSENSITIVE
    );
    public Agent(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        actions = new Actions(driver);
        js=(JavascriptExecutor)driver;
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
            String xpath = getXpath(currentPage,elementName);;
            return getElementByXpath(xpath);
        }

    }

    public WebElement getElementByXpath(String xpath){

        if(isXPath(xpath)){
         /*   WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            try{
                wait.until(ExpectedConditions.visibilityOf(element));
            }catch (Exception e){
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                        element
                );

            }*/
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

        }
        else {

            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(xpath)));
        }
    }



    public void checkElementExists(String elementName) {

            Assert.assertNotNull("Cannot find element "+elementName, getElement(elementName));


    }

    public void clickElement(String elementName) {
        scrollToElement(elementName);
        getElement(elementName).click();
    }



    public void typeDataIntoField(String text, String field) {
        WebElement element = getElement(field);
        element.clear();
        element.sendKeys(text);
    }

    public void moveToElement(String elementName)  {

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

    public void selectValue(String item, String input) {

        String xpath = "//span[contains(text(),'{item}')]".replace("{item}", item);
        clickElement(input);
        try {
            clickElement(xpath);
        } catch (ElementNotInteractableException e) {
            logger.info("Element not interactable so click it by manual");
            selectFirstItemByKeyboard();
        }
    }
    public void inputThenSelectValue(String item, String input) {
        getElement(input).sendKeys(item);
        String xpath="//span[contains(text(),'{item}')]".replace("{item}", item);
        try{
            clickElement(xpath);
        }catch(Exception e){
            selectFirstItemByKeyboard();
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

    public void selectFirstItemByKeyboard() {
        actions.keyDown(Keys.ARROW_DOWN).keyUp(Keys.ARROW_DOWN).keyDown(Keys.ENTER).keyUp(Keys.ENTER).perform();
    }

    public void scrollToElement(String elementName) {
        actions.moveToElement(getElement(elementName)).perform();
    }

    public void typeAccordingToFieldName(String text, String fieldName) {
        String xpath="//*[(local-name()='input' or local-name()='textarea') and @fieldname ='{item}']".replace("{item}", fieldName);
        actions.moveToElement(getElement(xpath)).perform();
        typeDataIntoField(text,xpath);
    }

    public void checkValueOfElement(String content, String fieldName) {
        String actual = checkValueExist(fieldName);
        Assert.assertEquals("Doesn't match expect is "+ content +" \n actual is "+actual,content,actual);
    }

    public String checkValueExist(String field) {
     //   System.out.println(getElement(field).getAttribute("fielddescription"));
        String  value = js.executeScript("return arguments[0].value;", getElement(field)).toString();
        Assert.assertNotNull("Get value fail",value);
        return value;
    }


}
