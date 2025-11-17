package com.own.Agent;

import com.own.Glue.context.GlobalContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.own.Agent.MyFunctions.getUserInfo;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiClient {
    private final RequestSpecification requestSpec;
    private final Map<String, String> headers;
    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    public ApiClient() {
        this.headers = new HashMap<>();
        this.requestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .build();
    }

    public void setHeader(String name, String value) {


        this.headers.put(name, value);
    }



    public Response sendGetRequest(String baseUrl, String endpoint, String queryParams) {
        RequestSpecification request = given().spec(requestSpec);

        // 直接添加查询字符串
        if (queryParams != null && !queryParams.isEmpty()) {
            request.params(parseQueryString(queryParams));
        }
        // 添加请求头
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.header(header.getKey(), header.getValue());
        }
        Response response = request.given().baseUri(baseUrl).log().headers().when().get(endpoint);
        response.prettyPrint();
        logger.info("Response of {} is {}",endpoint, response.asString());
        GlobalContext.getInstance().set("lastResponse",response);
        return response;
    }
    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString != null && !queryString.isEmpty()) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                } else if (keyValue.length == 1) {
                    params.put(keyValue[0], "");
                }
            }
        }
        return params;
    }

    /**
     * 发送GET请求（无参数版本）
     *
     * @param baseUrl  基础URL
     * @param endpoint 端点路径
     */
    public void sendGetRequest(String baseUrl, String endpoint) {
        sendGetRequest(baseUrl, endpoint, null);
    }

    public void sendPostRequest(String apiName, String baseUrl, String path, String body) throws Exception {
        RequestSpecification request = given().spec(requestSpec);
        // 添加请求头
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.header(header.getKey(), header.getValue());
        }

        // 处理不同类型的body
       String bodyTemplate= processBody(body);
        Map<String, Object> parameters = new HashMap<>();
        String finalBody ="";
        Response lastResponse = (Response)GlobalContext.getInstance().get("lastResponse");
        if(lastResponse!=null){
            logger.info("Last Response: is {} - {}", apiName,lastResponse.asString());
        }

        switch (apiName){
            case "用户登录API":
                //获取配置的用户
                String userName = getUserInfo().get("UserName");
                logger.info("Username is {}", userName);
                String password = getUserInfo().get("Password");
                logger.info("Password is {}", password);
                //加密密码
                String encryptedPassword = Encrypt.encryptWithPublicKey(password);
                logger.info("Encrypted Password is {}", encryptedPassword);
                parameters.put("account",userName);
                parameters.put("password",encryptedPassword);
                finalBody=processRequestBody(bodyTemplate, parameters);
                break;

            case "选择企业API":
                parameters.put("enterpriseId",lastResponse.jsonPath().getString("data.enterpriseInfos[0].enterpriseId"));
                parameters.put("userId",lastResponse.jsonPath().getString("data.userId"));
                parameters.put("secret",lastResponse.jsonPath().getString("data.secret"));
                finalBody=processRequestBody(bodyTemplate, parameters);
                break;

            case "新建订舱":
                parameters.put("delegationNumber",lastResponse.jsonPath().getString("data"));
                parameters.put("delegationNumber2",lastResponse.jsonPath().getString("data"));
                finalBody=processRequestBody(bodyTemplate, parameters);
                break;
        }
        Response response = request
                .baseUri(baseUrl)
                .basePath(path)
                .body(finalBody)
                .when()
                .post();

        GlobalContext.getInstance().set("lastResponse",response);
        logger.info("Response: {}", response.asString());
        System.out.println(response.asString());
        if(apiName.equals("选择企业API")){
            System.out.println("Token is "+response.jsonPath().get("data.accessToken").toString());
            GlobalContext.getInstance().set("Access-Token",response.jsonPath().get("data.accessToken").toString());
        }

    }
    private String processBody(String body) {

            if (body.endsWith(".json")) {
                return readJsonFromFile(body);
        }else{
                // 其他类型直接返回
                return body;
            }

    }

    private String readJsonFromFile(String fileName) {
        // 实现读取JSON文件的逻辑
        // 例如从resources目录下读取
        try {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/features/API/Request Body/" + fileName)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + fileName, e);
        }
    }

    public String processRequestBody(String bodyTemplate, Map<String, Object> parameters) {
        String result = bodyTemplate;

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String placeholder = "%{" + entry.getKey() + "}";
            Object value = entry.getValue();

            if (value == null) {
                result = result.replace("\"" + placeholder + "\"", "null");
                result = result.replace(placeholder, "null");
            } else {
                // 根据值的类型进行不同的处理
                if (value instanceof String) {
                    result = result.replace("\"" + placeholder + "\"", "\"" + value + "\"");
                } else if (value instanceof Boolean || value instanceof Number) {
                    result = result.replace(placeholder, value.toString());
                } else {
                    // 其他复杂类型转换为JSON字符串
                    result = result.replace("\"" + placeholder + "\"", "\"" + toJsonString(value) + "\"");
                }
            }
        }
        return result;
    }

    private String toJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }

    public void validateResponse(List<Map<String, String>> validationRules, Response lastResponse){
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





    // 其他HTTP方法 (GET, PUT, DELETE等) 可以类似实现
}

