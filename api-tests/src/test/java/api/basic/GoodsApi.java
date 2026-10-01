package api.basic;

import api.builders.RestApiBuilder;
import api.config.ReqSpec;
import common.config.ConfigProvider;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GoodsApi {

    private static final String ADMIN_LOGIN = ConfigProvider.getAdminLogin();
    private static final String ADMIN_PASSWORD = ConfigProvider.getAdminPassword();

    @Step("Получить список всех товаров (GET /goods/list)")
    public Response getList() {
        return RestApiBuilder.create()
                .method(RestApiBuilder.Method.GET)
                .path("/goods/list")
                .execute();
    }

    @Step("Создать товар (POST /goods/add): name={good.name}, price={good.price}")
    public Response addGoods(api.models.Good good) {
        return RestApiBuilder.create()
                .method(RestApiBuilder.Method.POST)
                .path("/goods/add")
                .withAuth(ADMIN_LOGIN, ADMIN_PASSWORD)
                .body(good)
                .execute();
    }

    @Step("Удалить товар по ID (DELETE /goods/{id})")
    public Response deleteGoods(Long id) {
        return RestApiBuilder.create()
                .method(RestApiBuilder.Method.DELETE)
                .path("/goods/" + id)
                .withAuth(ADMIN_LOGIN, ADMIN_PASSWORD)
                .execute();
    }

    @Step("Обновить товар по ID (PATCH /goods/{id})")
    public Response updateGoods(Long id, api.models.Good updatedGood) {
        return RestApiBuilder.create()
                .method(RestApiBuilder.Method.PATCH)
                .path("/goods/" + id)
                .withAuth(ADMIN_LOGIN, ADMIN_PASSWORD)
                .body(updatedGood)
                .execute();
    }

    @Step("Получить товар по ID (GET /goods/{id})")
    public Response getGoodById(Long id) {
        return RestApiBuilder.create()
                .method(RestApiBuilder.Method.GET)
                .path("/goods/" + id)
                .withAuth(ADMIN_LOGIN, ADMIN_PASSWORD)
                .execute();
    }

    // --- «Сырой» доступ для служебных нужд теста (например, @BeforeEach-очистка) ---

    @Step("Удалить товар по ID без обёртки (для cleanup)")
    public static void deleteRaw(Long id) {
        given()
                .spec(ReqSpec.requestSpec)
                .auth()
                .basic(ADMIN_LOGIN, ADMIN_PASSWORD)
                .when()
                .delete("/goods/" + id)
                .then()
                .log().ifValidationFails()
                .statusCode(200);
    }
}