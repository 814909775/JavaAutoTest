package Glue.Steps;

import Agent.ApiClient;
import Agent.UIClient;
import Glue.context.ScenarioContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

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

    @And("APIClient发起如下请求")
    public void apiclient发起如下请求(DataTable headersTable) {
    }
}
