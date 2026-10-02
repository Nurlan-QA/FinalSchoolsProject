package ui.SelenideTest.tests;

import common.config.ConfigProvider;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.BaseTestSelenide;
import ui.SelenideTest.pages.AdminPage;
import ui.SelenideTest.pages.CartPage;
import ui.SelenideTest.pages.GoodsPage;
import ui.SelenideTest.pages.ProductCleanup;

@Feature("Корзина")
@Story("Оформление заказа")
@Severity(SeverityLevel.BLOCKER)

public class CartTotalPriceTest extends BaseTestSelenide {

    private final GoodsPage goodsPage = new GoodsPage();
    private final CartPage cartPage = new CartPage();
    private final AdminPage adminPage = new AdminPage();
    private final ProductCleanup productCleanup = new ProductCleanup();

    private static final int COUNT = 3;
    private final int basePriceFromConfig = Integer.parseInt(ConfigProvider.getProductPrice());

    @AfterEach
    void cleanUp() {
        productCleanup.removeTestProducts();
    }

    @Test
    void cartTotalPrice() {
        String baseName = ConfigProvider.getProductName();
        int expectedSum = 0;

        // Вход в админку через базовый метод
        loginToAdmin();
        adminPage.assertPageLoaded();

        // Создаём товары через AdminPage
        for (int i = 1; i <= COUNT; i++) {
            String name = baseName + "_" + i;
            int price = basePriceFromConfig + i;
            expectedSum += price;
            adminPage.createProduct(name, String.valueOf(price));
            System.out.println("Добавлен товар: " + name + ", цена: " + price);
        }
        System.out.println("Ожидаемая сумма: " + expectedSum);

        // Возврат на витрину
        adminPage.goToSite();
        goodsPage.assertPageLoaded();

        // Добавляем в корзину
        for (int i = 1; i <= COUNT; i++) {
            goodsPage.addProductToCart(baseName + "_" + i);
        }

        // Открываем корзину и проверяем сумму
        goodsPage.openCart();
        cartPage.assertItemsCount(COUNT);
        int totalPrice = cartPage.getTotalPrice();
        checkTotalPrice(totalPrice, expectedSum);

        cartPage.closeCartModal();
    }

    private void checkTotalPrice(int actual, int expected) {
        org.junit.jupiter.api.Assertions.assertEquals(
                expected, actual,
                "Сумма в корзине не совпадает с расчётной"
        );
    }
}