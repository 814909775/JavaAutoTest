package Glue.Steps;

import Agent.ApiClient;
import Agent.UIClient;
import Glue.context.GlobalContext;
import Glue.context.ScenarioContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiSteps {
    private ScenarioContext context;
    private ApiClient apiClient;
    private Response response;
    private static final Logger logger = LoggerFactory.getLogger(ApiSteps.class);
    public ApiSteps(ScenarioContext scenarioContext) {
        this.context = scenarioContext;
        this.apiClient = new ApiClient();
    }


    @Given("APIClient设置请求头")
    public void apiclientSetHeader(DataTable headersTable) {
        // 将DataTable转换为键值对映射
        List<Map<String, String>> headers = headersTable.asMaps(String.class, String.class);

        // 遍历所有请求头并设置到APIClient中
        for (Map<String, String> header : headers) {
            String headerName = header.get("header-name");
            String headerValue = header.get("header-value");
            // 调用APIClient的方法设置请求头
            apiClient.setHeader(headerName, headerValue);

        }
    }


    @And("^APIClient发起(.+)POST请求$")
    public void apiclient发起用户登录api的post请求(String apiName,DataTable dataTable ) throws Exception {
        //logger.info("APIClient发起{}API的POST请求",apiName);
        List<Map<String, String>> param = dataTable.asMaps(String.class, String.class);

        // 添加检查确保至少有一个map
        if (param.isEmpty()) {
            throw new IllegalArgumentException("DataTable should contain at least one row");
        }
        // 获取第一个map
        Map<String, String> header = param.get(0);
        String baseUri = header.get("BaseUri");
        String path = header.get("Path");
        String body = header.get("Body");
        apiClient.post(apiName,baseUri,path,body);

    }

    @Then("^APIClient校验(.+)响应结果$")
    public void apiclient校验用户登录api响应结果(String apiName,DataTable expectations) {
        List<Map<String, String>> validationRules = expectations.asMaps(String.class, String.class);

        Response lastResponse = (Response) GlobalContext.getInstance().get("lastResponse");

        for (Map<String, String> rule : validationRules) {
            String validationType = rule.get("验证项");
            String expectedValue = rule.get("期望值");

            switch (validationType) {
                case "状态码":
                    int expectedStatusCode = Integer.parseInt(expectedValue);
                    assertEquals(expectedStatusCode, lastResponse.getStatusCode());
                    break;
                case "返回字段含":
                    // 验证响应中包含指定字段
                    assertTrue("实际"+lastResponse.getBody().asString(), lastResponse.getBody().asString().contains(expectedValue));
                    break;
                case "字段值等于":
                    // 直接从响应中提取字段值进行比较
                    String actualValue = lastResponse.jsonPath().getString(expectedValue.split("=")[0]);
                    String expectedValueStr = expectedValue.split("=")[1];
                    assertEquals("期望是 "+expectedValueStr+"实际是 "+actualValue,expectedValueStr, actualValue);
                    break;
            }
        }

    }
}
