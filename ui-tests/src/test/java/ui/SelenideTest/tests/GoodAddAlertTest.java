package ui.SelenideTest.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import common.config.ConfigProvider;
import ui.SelenideTest.BaseTestSelenide;
import ui.SelenideTest.pages.*;

// Войти в админку и добавить товар. Проверить уведомление после добавления товара.

public class GoodAddAlertTest extends BaseTestSelenide {

    private final LoginPage loginPage = new LoginPage();
    private final AdminPage adminPage = new AdminPage();
    private final GoodsPage goodsPage = new GoodsPage();
    private final ProductCleanup productCleanup = new ProductCleanup();
    private final CartPage cartPage = new CartPage();

    private static final long UNIQUE_SUFFIX = System.nanoTime() % 1_000_000;
    private final String productName = ConfigProvider.getProductName() + "_" + UNIQUE_SUFFIX;
    private final String productPrice = ConfigProvider.getProductPrice();

    @AfterEach
    void cleanUp() {
        productCleanup.removeProductByName(productName);
    }

    @Tag("smoke")
    @Test
    void goodsAdd() {

        // Вход в админку
        loginToAdmin();

        // Создание товара
        adminPage.assertPageLoaded();
        adminPage.createProduct(productName, productPrice);

        // Проверяем наличие тостера об успешном оформлении
        ConfigProvider.getToastGoodAdded();
    }
}