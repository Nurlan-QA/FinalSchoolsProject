package ui.SelenideTest.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import common.config.ConfigProvider;
import ui.SelenideTest.BaseTestSelenide;
import ui.SelenideTest.pages.*;

@Feature("Корзина")
@Story("Оформление заказа")
@Severity(SeverityLevel.CRITICAL)

public class CartRemovalTest extends BaseTestSelenide {

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

    @Description("Добавление товара в корзину методом Drug&Drop и проверка счетчика корзины")
    @Test
    void cartRemoval() {
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

        // Drag-and-drop в корзину
        goodsPage.dragProductToCart(productName);
        goodsPage.assertCartCount(1);

        // Открыть корзину
        goodsPage.openCart();

        // Удалить товар из корзины
        cartPage.removeProductFromCart(productName);

        // Закрыть корзину и проверить счётчик
        cartPage.closeCartModal();
        goodsPage.assertCartCount(0);

        // Очистка: удалить товар из админки
        goodsPage.goToAdmin();
        deleteProduct(productName);
    }
}
