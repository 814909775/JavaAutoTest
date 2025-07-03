package Agent;

import org.openqa.selenium.WebDriver;

public class Agent {
    private WebDriver driver;
    public Agent(WebDriver driver) {
        this.driver = driver;
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
}
