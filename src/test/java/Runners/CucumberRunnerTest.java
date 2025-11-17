package Runners;

import com.own.Agent.EmailUtils;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static com.own.Agent.EmailUtils.getEmailInfo;

@RunWith(Cucumber.class)
@CucumberOptions(
        features ="src/test/resources/features",
        glue = "com/own/Glue",
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber.json"
                }
        )
public class CucumberRunnerTest {
    private static final Logger logger = LoggerFactory.getLogger(CucumberRunnerTest.class);
    @AfterClass
    public static void afterAll(){
        Map<String,String> emailConfig = getEmailInfo();
        Object value = emailConfig.get("enableEmail");
        if(value.equals(Boolean.TRUE)){
            try {
                logger.info("开始发送邮件");
                EmailUtils.sendEmail(emailConfig);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
