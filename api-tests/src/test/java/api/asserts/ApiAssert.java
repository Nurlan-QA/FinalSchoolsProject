package api.asserts;

import api.models.Good;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

/**
 * Единая точка проверок для API-тестов.
 * Заменяет локальные checkXxx-методы, которые были внутри GoodTest.
 */
public class ApiAssert {

    @Step("Проверка: список товаров содержит только защищённые элементы ({expectedSize} шт.)")
    public static void assertGoodsListHasOnlyProtected(Response response, int expectedSize) {
        response.then()
                .statusCode(200)
                .body("goods", hasSize(expectedSize))
                .body("goods.id", containsInAnyOrder(50, 51, 52, 53));
    }

    @Step("Проверка: товар с именем '{name}' присутствует в списке")
    public static void assertGoodInListByName(Response response, String name) {
        response.then()
                .statusCode(200)
                .body("goods", hasItem(hasEntry("name", name)));
    }

    @Step("Проверка: список товаров не пуст")
    public static void assertGoodsListNotEmpty(List<Good> goodsList) {
        Assertions.assertThat(goodsList).isNotEmpty();
    }

    @Step("Проверка: товар с именем '{name}' есть в списке")
    public static void assertGoodNameInList(List<Good> goodsList, String name) {
        Assertions.assertThat(goodsList)
                .extracting(Good::getName)
                .contains(name);
    }

    @Step("Проверка: статус-код ответа равен {expected}")
    public static void assertStatusCode(Response response, int expected) {
        Assertions.assertThat(response.getStatusCode())
                .as("Ожидаем статус-код " + expected)
                .isEqualTo(expected);
    }

    @Step("Проверка: статус-код ответа входит в список допустимых")
    public static void assertStatusCodeIn(Response response, int... expectedCodes) {
        List<Integer> codes = new ArrayList<>();
        for (int c : expectedCodes) {
            codes.add(c);
        }
        Assertions.assertThat(response.getStatusCode())
                .as("Ожидаем один из статусов: " + codes)
                .isIn(codes.toArray());
    }

    @Step("Проверка: цена товара равна {expectedPrice}")
    public static void assertGoodPrice(Good good, double expectedPrice) {
        Assertions.assertThat(good.getPrice())
                .as("Цена должна быть " + expectedPrice)
                .isEqualTo(expectedPrice);
    }

    @Step("Проверка: товар не null")
    public static void assertGoodNotNull(Good good) {
        Assertions.assertThat(good).isNotNull();
    }

    @Step("Проверка: товар с ID {createdId} найден в списке с ценой {newPrice}")
    public static void assertGoodInListById(List<Good> goodsList, Long createdId, double newPrice) {
        Good foundInList = goodsList.stream()
                .filter(g -> g.getId().equals(createdId))
                .findFirst()
                .orElse(null);

        Assertions.assertThat(foundInList).isNotNull();
        Assertions.assertThat(foundInList.getPrice()).isEqualTo(newPrice);
    }
}