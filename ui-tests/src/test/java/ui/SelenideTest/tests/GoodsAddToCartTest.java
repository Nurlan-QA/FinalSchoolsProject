package ui.SelenideTest.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import common.config.ConfigProvider;
import ui.SelenideTest.BaseTestSelenide;
import ui.SelenideTest.pages.*;

@Feature("Корзина")
@Story("Список товаров в корзине")
@Severity(SeverityLevel.CRITICAL)

public class GoodsAddToCartTest extends BaseTestSelenide {

    private final AdminPage adminPage = new AdminPage();
    private final GoodsPage goodsPage = new GoodsPage();
    private final ProductCleanup productCleanup = new ProductCleanup();
    private final CartPage cartPage = new CartPage();

    private static final long UNIQUE_SUFFIX = System.nanoTime() % 1_000_000;
    private final String productName = ConfigProvider.getProductName() + "_" + UNIQUE_SUFFIX;
    private final String productPrice = ConfigProvider.getProductPrice();

    @Description("Добавление товара в корзину и проверка, что он отображается в ней.")
    @AfterEach
    void cleanUp() {
        productCleanup.removeProductByName(productName);
    }

    @Test
    void goodsAddInCart() {

        // Вход в админку
        loginToAdmin();

        // Создание товара
        adminPage.assertPageLoaded();
        adminPage.createProduct(productName, productPrice);
        ConfigProvider.getToastGoodAdded();

        // Возврат на витрину
        adminPage.goToSite();
        goodsPage.assertPageLoaded();

        // Проверка наличия товара
        goodsPage.assertProductVisible(productName);
        goodsPage.assertProductHasText(productName);

        // ************* ДОБАВЛЯЕМ ТОВАР В КОРЗИНУ И ПРОВЕРЯЕМ *************
        goodsPage.addProductToCart(productName);

        // Открываем корзину
        goodsPage.openCart();

        // Проверяем наличие добавленного товара в корзине
        cartPage.shouldBeGoodInCart(productName);

        // Закрываем модальное окно корзины
        cartPage.closeCartModal();

    }
}