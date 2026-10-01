package api.builders;

import api.config.ReqSpec;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Строитель REST-запросов. Скрывает детали given()/when()/then().
 * Используется в basicApi-классах (GoodsApi).
 */
public class RestApiBuilder {

    public enum Method { GET, POST, PATCH, DELETE, PUT }

    private Method method;
    private String path;
    private Object body;
    private boolean withAuth = false;
    private String login;
    private String password;

    public static RestApiBuilder create() {
        return new RestApiBuilder();
    }

    public RestApiBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public RestApiBuilder path(String path) {
        this.path = path;
        return this;
    }

    public RestApiBuilder body(Object body) {
        this.body = body;
        return this;
    }

    public RestApiBuilder withAuth(String login, String password) {
        this.withAuth = true;
        this.login = login;
        this.password = password;
        return this;
    }

    @Step("Выполнить {method} {path}")
    public Response execute() {
        var request = given().spec(ReqSpec.requestSpec);

        if (withAuth) {
            request = request.auth().basic(login, password);
        }
        if (body != null) {
            request = request.body(body);
        }

        Response response = switch (method) {
            case GET    -> request.when().get(path);
            case POST   -> request.when().post(path);
            case PATCH  -> request.when().patch(path);
            case PUT    -> request.when().put(path);
            case DELETE -> request.when().delete(path);
        };

        return response.then().log().all().extract().response();
    }
}