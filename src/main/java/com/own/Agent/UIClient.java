package com.own.Agent;

import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static com.own.Agent.MyFunctions.*;
import static com.own.Glue.Steps.UiSteps.currentPage;
import static com.own.Glue.Steps.Hook.currentTime;


public class UIClient {
    private WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;
    private final JavascriptExecutor js;
    private static final Logger logger = LoggerFactory.getLogger(UIClient.class);
    private static final Pattern XPATH_PATTERN = Pattern.compile(
            "^(/|//)|(@|contains\\(|text\\()",
            Pattern.CASE_INSENSITIVE
    );
    public UIClient(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(4));
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

             /*   WebElement firstVisibleElement = wait.until(driver -> {
                    List<WebElement> elements = driver.findElements(By.xpath(xpath));

                    for (WebElement element : elements) {
                        if (element.isDisplayed()) {
                            return element;  // 返回第一个可见的元素
                        }
                    }
                    return null;  // 如果没有可见元素，返回null
                });

            return wait.until(ExpectedConditions.visibilityOf(firstVisibleElement));*/
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
        switch (field) {
            case "ETD":
                text= getETD();
                break;
            default:
                break;
        }
        WebElement element = getElement(field);
        element.clear();
        element.sendKeys(text);
    }
    public  String generateTimestamp() {
        // 定义时间格式：年份后两位(y)、月份(m)、日期(d)、小时(m)、分钟(s)、秒(s)
        // 注意：月份和分钟都是m，区分在于位置和数量(MM是月份，mm是分钟)
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd-mmss");

        // 可选：指定时区（如北京时间），避免服务器时区影响
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        // 格式化当前时间并返回
        return sdf.format(new Date());
    }

    public static String getETD() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 加2天
        LocalDate twoDaysLater = currentDate.plusDays(2);
        // 定义格式器（注意：MM表示月份，dd表示日期）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 格式化并返回
        return twoDaysLater.format(formatter);
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

//        String xpath = "//span[contains(text(),'{item}')]".replace("{item}", item);
        String xpath=getXpath(currentPage,"CommonSelect").replace("{item}", item);
        clickElement(input);
        try {
            clickElement(xpath);
        } catch (ElementNotInteractableException e) {
            logger.info("Element not interactable so click it by manual");
            selectFirstItemByKeyboard();
        }
    }
    public void inputThenSelectValue(String item, String input) {

        //String xpath="//span[contains(text(),'{item}')]".replace("{item}", item);
//        String xpath= "//div[not(contains(@style,'display: none'))]/div/div/div/ul/li/span[text()='{item}']".replace("{item}", item);
        getElement(input).sendKeys(item);
        String xpath=getXpath(currentPage,"CommonInputAndSelect").replace("{item}", item);

        try{
            System.out.println(xpath);
            clickElement(xpath);
        }catch(Exception e){
          //  selectFirstItemByKeyboard();
            throw e;
        }


    }

    public void selectFirstItemByKeyboard() {
        actions.keyDown(Keys.ARROW_DOWN).keyUp(Keys.ARROW_DOWN).keyDown(Keys.ENTER).keyUp(Keys.ENTER).perform();
    }

    public void scrollToElement(String elementName) {
        actions.moveToElement(getElement(elementName)).perform();
    }

    public void typeAccordingToFieldName(String text, String fieldName) {
//        String xpath="//*[(local-name()='input' or local-name()='textarea') and @fieldname ='{item}']".replace("{item}", fieldName);
        String xpath=getXpath(currentPage,"CommonInput").replace("{item}", fieldName);
        actions.moveToElement(getElement(xpath)).perform();
        if(text.contains("#{timestamp}")){
            typeDataIntoField( text.replace("#{timestamp}", currentTime) ,xpath);

        }else {
            typeDataIntoField(text,xpath);
        }
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

    public  Response getSecret() throws Exception {
        //获取配置的用户
        String userName = getUserInfo().get("UserName");
        logger.info("Username is "+userName);
        String password = getUserInfo().get("Password");
        logger.info("Password is "+password);
        //加密密码
        String encryptedPassword = Encrypt.encryptWithPublicKey(password);
        logger.info("Encrypted Password is "+encryptedPassword);
        Map<String,String> header = new HashMap<>();
        header.put("App-Name","whale_common_pc");
        header.put("App-Key","whale_common_pc");
        String requestBody = """
                {
                    "username": "",
                    "account": "%s",
                    "countryCode": "86",
                    "loginType": 0,
                    "registerType": 1,
                    "password":"%s",
                    "rememberMe": true,
                    "param": {
                        "openId": "",
                        "unionId": "",
                        "client": ""
                    },
                    "captchaData": {
                
                    }
                }
                """.formatted(userName,encryptedPassword);

        return postRequest("https://beta-apisix.hgj.com","/whale-user-center/pass/login/password-login",header,requestBody);

    }
    public  Response getToken(Response loginResponse) throws Exception {
           String id = getResponseField(loginResponse,"data.userId");
        System.out.println("id is "+id);
           String secret = getResponseField(loginResponse,"data.secret");
        System.out.println("secret is "+secret);
           String enterprise = getResponseField(loginResponse,"data.enterpriseInfos[0].enterpriseId");
        System.out.println("enterpriseId is "+enterprise);
            Map<String,String> header = new HashMap<>();
            header.put("App-Name","whale_common_pc");
            String requestBody = """
                {
                    "userId": "%s",
                    "enterpriseId": "%s",
                    "secret": "%s"
                }
                """.formatted(id,enterprise,secret);
        return postRequest("https://beta-apisix.hgj.com","/whale-user-center/pass/login/choose-enterprise",header,requestBody);
    }
    /**
     * 向浏览器注入Cookie的工具方法
     * <p>
     * 功能：从接口响应中提取指定值，构建为Cookie并注入浏览器，最终刷新页面使Cookie生效
     * </p>
     *
     * @param domain   Cookie所属的域名（例如："hgj.com"，注意：不要包含"https://"或"www"）
     * @param key      Cookie的键名（需与网站实际使用的Cookie键一致，例如："access_token"）
     * @param response RestAssured的响应对象（包含需要提取的Cookie值的接口响应）
     * @param value    从响应中提取Cookie值的JSON路径（例如："data.access_token"，对应响应体中该路径的值）
     */
    public void addIntoCookie(String domain,String key,Response response,String value){
        driver.get("https://"+domain);
        Cookie accessTokenCookie = new Cookie.Builder(key, response.jsonPath().getString(value))
                .domain(domain) // 核心：域名必须匹配
                .path("/")     // 核心：路径必须匹配
                .isSecure(true)         // 若网站用HTTPS，需设为true（从F12查看）
                // .expiry(new Date(System.currentTimeMillis() + 3600000)) // 可选：设置过期时间（与实际一致）
                .build();
        // 3. 注入Cookie到浏览器
        driver.manage().addCookie(accessTokenCookie);
        driver.get("https://beta-smartbooking.hgj.com/Dashboard/Workplace");
        driver.navigate().refresh();
    }

    public void apiLogin() throws Exception {
        Response response=getToken(getSecret());
        addIntoCookie("beta-smartbooking.hgj.com","hgj-beta-access-token",response,"data.accessToken");
    }



}
