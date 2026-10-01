package ui.SelenideTest.tests;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import common.config.ConfigProvider;
import ui.SelenideTest.BaseTestSelenide;
import ui.SelenideTest.pages.*;

// 2.5. Добавить в корзину товаров более чем на 300 рублей и нажать на кнопку «Оформить заказ».
// Проверить, что отображается JS Alert.

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Description;

@Feature("Корзина")
@Story("Оформление заказа")
@Severity(SeverityLevel.NORMAL)

public class CartCheckoutAlertTest extends BaseTestSelenide {

    private final LoginPage loginPage = new LoginPage();
    private final AdminPage adminPage = new AdminPage();
    private final GoodsPage goodsPage = new GoodsPage();
    private final ProductCleanup productCleanup = new ProductCleanup();
    private final CartPage cartPage = new CartPage();

    private static final long UNIQUE_SUFFIX = System.nanoTime() % 1_000_000;
    private final String productName = ConfigProvider.getProductName() + "_" + UNIQUE_SUFFIX;
    private final String productBigPrice = ConfigProvider.getProductBigPrice();

    @Description("Добавление товаров дороже 300 руб и проверка алерта")
    @AfterEach
    void cleanUp() {
        productCleanup.removeProductByName(productName);
    }

    @Test
    void checkoutWithExpensiveItem() {

        // ************* ВХОД В АДМИНКУ *************
        loginToAdmin();

        // ************* ДОБАВЛЕНИЕ ТОВАРА ДОРОЖЕ 300 РУБ В АДМИНКЕ *************
        adminPage.assertPageLoaded(); // Проверяем, что админка загрузилась
        adminPage.createProduct(productName, productBigPrice); // Создаём товар через AdminPage
        adminPage.assertToastContains(ConfigProvider.getToastGoodAdded());
        System.out.println("Уведомление: Товар успешно добавлен!");

        // ************* ВОЗВРАЩАЕМСЯ НА САЙТ И ДОБАВЛЯЕМ ТОВАР В КОРЗИНУ *************
        adminPage.goToSite();
        goodsPage.assertPageLoaded();

        // *********** ПРОВЕРЯЕМ НАЛИЧИЕ ТЕСТОВОГО ТОВАРА *************
        goodsPage.assertProductVisible(productName);
        goodsPage.assertProductHasText(productName);
        System.out.println("Созданный товар '" + productName + "' есть на витрине сайта!");

        // ************* ДОБАВЛЯЕМ ТОВАР В КОРЗИНУ *************
        goodsPage.addProductToCart(productName);

        // ************* ОТКРЫВАЕМ КОРЗИНУ И ПЫТАЕМСЯ ОФОРМИТЬ ТОВАР *************
        goodsPage.openCart();
        cartPage.makeOrder(); // Оформляем заказ

        var alert = Selenide.switchTo().alert();
        String alertText = alert.getText();
        System.out.println("Получен алерт: " + alertText);
        alert.accept();

        // *********** ЗАКРЫВАЕМ МОДАЛЬНОЕ ОКНО КОРЗИНЫ *************
        cartPage.closeCartModal();
    }
}
