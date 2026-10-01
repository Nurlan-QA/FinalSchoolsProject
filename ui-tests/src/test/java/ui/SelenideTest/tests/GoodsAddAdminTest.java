package ui.SelenideTest.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import common.config.ConfigProvider;
import ui.SelenideTest.BaseTestSelenide;
import ui.SelenideTest.pages.*;

public class GoodsAddAdminTest extends BaseTestSelenide {

    private final AdminPage adminPage = new AdminPage();
    private final GoodsPage goodsPage = new GoodsPage();
    private final ProductCleanup productCleanup = new ProductCleanup();

    private static final long UNIQUE_SUFFIX = System.nanoTime() % 1_000_000;
    private final String productName = ConfigProvider.getProductName() + "_" + UNIQUE_SUFFIX;
    private final String productPrice = ConfigProvider.getProductPrice();

    @AfterEach
    void cleanUp() {
        productCleanup.removeProductByName(productName);
    }

    @Test
    void goodsAdd() {

        // Вход в админку
        loginToAdmin();

        // Проверяем, что админка загрузилась
        adminPage.assertPageLoaded();

        // Создаём товар через AdminPage
        adminPage.createProduct(productName, productPrice);

        // Проверяем уведомление
        ConfigProvider.getToastGoodAdded();

        // Переходим на витрину
        adminPage.goToSite();
        goodsPage.assertPageLoaded();

        // Проверяем наличие товара
        goodsPage.assertProductVisible(productName);
        goodsPage.assertProductHasText(productName);
    }
}
