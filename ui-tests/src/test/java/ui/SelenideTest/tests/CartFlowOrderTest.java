package ui.SelenideTest.tests;

import common.config.ConfigProvider;
import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.BaseTestSelenide;
import ui.SelenideTest.pages.*;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("Корзина")
@Story("Оформление заказа")
@Severity(SeverityLevel.CRITICAL)

public class CartFlowOrderTest extends BaseTestSelenide {

    private final GoodsPage goodsPage = new GoodsPage();
    private final CartPage cartPage = new CartPage();
    private final LoginPage loginPage = new LoginPage();
    private final AdminPage adminPage = new AdminPage();
    private final ProductCleanup productCleanup = new ProductCleanup();

    private static final int COUNT = 3;

    @Description("Проверка, что сумма трех товаров не превышает 300 руб")
    @AfterEach
    void cleanUpProducts() {
        productCleanup.removeTestProducts();
    }

    @Test
    void addThreeGoodsThroughUi() {
        String baseName = ConfigProvider.getProductName();
        int basePrice = Integer.parseInt(ConfigProvider.getProductPrice());

        // 1. Вход в админку
        open("/admin");
        loginPage.assertPageLoaded();
        loginPage.login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
        adminPage.assertPageLoaded();

        // 2. Создаём 3 товара через админку
        List<Integer> prices = new ArrayList<>();
        for (int i = 1; i <= COUNT; i++) {
            String name = baseName + "_" + i;
            int price = basePrice + i;
            prices.add(price);
            adminPage.createProduct(name, String.valueOf(price));
            System.out.println("Создан тестовый товар: " + name + " (цена " + price + ")");
        }

        // 3. Возвращаемся на витрину
        adminPage.goToSite();
        goodsPage.assertPageLoaded();

        // 4. Добавляем 3 товара в корзину
        for (int i = 1; i <= COUNT; i++) {
            goodsPage.addProductToCart(baseName + "_" + i);
        }

        // 5. Открываем корзину и проверяем
        goodsPage.openCart();
        cartPage.assertItemsCount(COUNT);
        cartPage.assertItemsContainText(baseName + "_");

        int totalPrice = cartPage.getTotalPrice();
        System.out.println("Сумма в корзине: " + totalPrice);
        checkTotalPrice(totalPrice, ConfigProvider.getCartMaxSmallOrder());

        // 6. Оформляем заказ
        cartPage.makeOrder();
        cartPage.assertOrderAccepted();
        System.out.println("Уведомление: " + ConfigProvider.getToastOrderAccepted());
    }

    @Step("Проверка: сумма корзины {totalPrice} не превышает {maxPrice}")
    private void checkTotalPrice(int totalPrice, int maxPrice) {
        assertThat(totalPrice).as("Сумма в корзине не должна превышать " + maxPrice)
                .isLessThanOrEqualTo(maxPrice);
    }
}