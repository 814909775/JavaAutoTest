package Agent;

import Glue.Steps.UiSteps;
import Glue.context.GlobalContext;
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
import java.util.Map;

import static Agent.MyFunctions.getUserInfo;
import static io.restassured.RestAssured.given;

public class ApiClient {
    private RequestSpecification requestSpec;
    private Map<String, String> headers;
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

    public void setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);


    }

    public Response post(String apiName,String baseUrl, String path, String body) throws Exception {
        RequestSpecification request = given().spec(requestSpec);
        // 添加请求头
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.header(header.getKey(), header.getValue());
        }

        // 处理不同类型的body
       String bodyTemplate= processBody(body);
        Map<String, Object> parameters = new HashMap<>();
        String finalBody ="";
        switch (apiName){
            case "用户登录API":
                //获取配置的用户
                String userName = getUserInfo().get("UserName");
                logger.info("Username is "+userName);
                String password = getUserInfo().get("Password");
                logger.info("Password is "+password);
                //加密密码
                String encryptedPassword = Encrypt.encryptWithPublicKey(password);
                logger.info("Encrypted Password is "+encryptedPassword);
                parameters.put("account",userName);
                parameters.put("password",encryptedPassword);
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
        return response;

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

    // 其他HTTP方法 (GET, PUT, DELETE等) 可以类似实现
}

