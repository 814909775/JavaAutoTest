package Agent;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {
    private RequestSpecification requestSpec;
    private Map<String, String> headers;

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

    public Response post(String baseUrl, String path, Object body) {
        RequestSpecification request = given().spec(requestSpec);

        // 添加请求头
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.header(header.getKey(), header.getValue());
        }

        return request
            .baseUri(baseUrl)
            .basePath(path)
            .body(body)
            .when()
            .post();
    }

    // 其他HTTP方法 (GET, PUT, DELETE等) 可以类似实现
}

