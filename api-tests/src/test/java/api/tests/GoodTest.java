package api.tests;

import api.asserts.ApiAssert;
import api.basic.GoodsApi;
import api.config.ApiConfig;
import api.config.ReqSpec;
import api.models.Good;
import common.config.ConfigProvider;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class GoodTest {

    private final GoodsApi goodsApi = new GoodsApi();
    private final List<Long> createdGoodIds = new ArrayList<>();

    private static final Set<Long> PROTECTED_IDS = ConfigProvider.getProtectedIds();

    @Step("Очистка базы перед тестом (кроме защищённых ID)")
    @BeforeEach
    void clearDatabaseBeforeEachTest() {
        Response response = given()
                .spec(ReqSpec.requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        List<Good> allGoods = response.jsonPath().getList("goods", Good.class);

        for (Good good : allGoods) {
            if (PROTECTED_IDS.contains(good.getId())) {
                continue;
            }
            GoodsApi.deleteRaw(good.getId());
        }
    }

    @Step("Очистка товаров, созданных в тесте")
    @AfterEach
    void clearCreatedGoodsAfterEachTest() {
        for (Long id : createdGoodIds) {
            Response resp = given()
                    .spec(ReqSpec.requestSpec)
                    .auth()
                    .basic(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword())
                    .when()
                    .delete("/goods/" + id);

            int status = resp.getStatusCode();
            if (status != 200 && status != 404) {
                System.err.println("Неожиданный статус " + status + " при удалении ID " + id);
            }
        }
        createdGoodIds.clear();
    }

    // ==========================================
    // ТЕСТЫ
    // ==========================================

    @Test
    void testGetListWithGivenWhenThen() {
        Response response = given()
                .baseUri(ApiConfig.BASE_URL)
                .queryParam("page", ConfigProvider.getApiListPage())
                .queryParam("size", ConfigProvider.getApiListSize())
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        ApiAssert.assertGoodsListHasOnlyProtected(response, 4);
    }

    @Tag("smoke")
    @Test
    void testGetListWithRequestSpec() {
        Response response = goodsApi.getList();
        ApiAssert.assertGoodsListHasOnlyProtected(response, 4);
    }

    @Tag("smoke")
    @Test
    void testAddGoodsRestAssured() {
        long now = System.currentTimeMillis();
        String uniqueName = ConfigProvider.getProductName() + "_RA_" + (now % 1000000);
        double price = Double.parseDouble(ConfigProvider.getProductPrice());

        Good newGood = new Good(uniqueName, price);

        Response createResponse = goodsApi.addGoods(newGood);
        ApiAssert.assertStatusCode(createResponse, 200);

        Long createdId = createResponse.jsonPath().getLong("data.id");
        createdGoodIds.add(createdId);

        Response listResponse = goodsApi.getList();
        ApiAssert.assertGoodInListByName(listResponse, uniqueName);
    }

    @Test
    void testAddGoodsWithAssertJ() {
        long now = System.currentTimeMillis();
        String uniqueName = "Good_AJ" + (now % 1000000);
        double price = Double.parseDouble(ConfigProvider.getProductAssertjPrice());
        Good newGood = new Good(uniqueName, price);

        Response createResp = goodsApi.addGoods(newGood);
        ApiAssert.assertStatusCode(createResp, 200);

        Long createdId = createResp.jsonPath().getLong("data.id");
        createdGoodIds.add(createdId);

        Response response = goodsApi.getList();
        List<Good> goodsList = response.jsonPath().getList("goods", Good.class);

        ApiAssert.assertGoodsListNotEmpty(goodsList);
        ApiAssert.assertGoodNameInList(goodsList, uniqueName);
    }

    @Test
    void testDeleteGoods() {
        long now = System.currentTimeMillis();
        String uniqueName = ConfigProvider.getProductName() + "_delete_" + (now % 1000000);
        double price = Double.parseDouble(ConfigProvider.getProductPrice());
        Good newGood = new Good(uniqueName, price);

        Response createResp = goodsApi.addGoods(newGood);
        ApiAssert.assertStatusCode(createResp, 200);

        Long createdId = createResp.jsonPath().getLong("data.id");
        createdGoodIds.add(createdId);

        Response deleteResp = goodsApi.deleteGoods(createdId);
        ApiAssert.assertStatusCode(deleteResp, 200);

        Response getByIdResp = goodsApi.getGoodById(createdId);
        ApiAssert.assertStatusCodeIn(getByIdResp, 404, 500);
    }

    @Test
    void testUpdateGoods() {
        long now = System.currentTimeMillis();
        String uniqueName = ConfigProvider.getProductName() + "_update_" + (now % 1000000);
        double price = Double.parseDouble(ConfigProvider.getProductPrice());
        Good newGood = new Good(uniqueName, price);

        Response createResp = goodsApi.addGoods(newGood);
        ApiAssert.assertStatusCode(createResp, 200);

        Long createdId = createResp.jsonPath().getLong("data.id");
        createdGoodIds.add(createdId);

        double newPrice = Double.parseDouble(ConfigProvider.getProductBigPrice());
        Good updatedGood = new Good(uniqueName, newPrice);

        Response updateResp = goodsApi.updateGoods(createdId, updatedGood);
        ApiAssert.assertStatusCode(updateResp, 200);

        Good patchedGood = updateResp.jsonPath().getObject("$", Good.class);
        ApiAssert.assertGoodNotNull(patchedGood);
        ApiAssert.assertGoodPrice(patchedGood, newPrice);

        Response listResp = goodsApi.getList();
        List<Good> goodsList = listResp.jsonPath().getList("goods", Good.class);
        ApiAssert.assertGoodInListById(goodsList, createdId, newPrice);
    }
}