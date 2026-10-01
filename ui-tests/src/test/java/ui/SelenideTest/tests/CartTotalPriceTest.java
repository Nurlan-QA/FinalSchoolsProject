package ui.SelenideTest.tests;

import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import common.config.ConfigProvider;
import ui.SelenideTest.BaseTestSelenide;
import ui.SelenideTest.pages.*;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartTotalPriceTest extends BaseTestSelenide {

    private final GoodsPage goodsPage = new GoodsPage();
    private final CartPage cartPage = new CartPage();
    private final LoginPage loginPage = new LoginPage();
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
        int sum = 0;

        // Вход в админку через LoginPage
        open("/admin");
        loginPage.assertPageLoaded();
        loginPage.login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());

        // Проверяем, что админка загрузилась
        adminPage.assertPageLoaded();

        // Создаём товары через AdminPage
        for (int i = 1; i <= COUNT; i++) {
            String name = baseName + "_" + i;
            int price = basePriceFromConfig + i;
            sum += price;
            adminPage.createProduct(name, String.valueOf(price));
            System.out.println("Добавлен товар: " + name + ", цена: " + price);
        }

        System.out.println("Ожидаемая сумма: " + sum);

        // Возврат на витрину
        adminPage.goToSite();
        goodsPage.assertPageLoaded();

        // Добавляем в корзину через GoodsPage
        for (int i = 1; i <= COUNT; i++) {
            goodsPage.addProductToCart(baseName + "_" + i);
        }

        // Открываем корзину и проверяем
        goodsPage.openCart();
        cartPage.assertItemsCount(COUNT);

        int totalPrice = cartPage.getTotalPrice();
        checkTotalPrice(totalPrice, sum);



        cartPage.closeCartModal();
    }

    @Step("Проверка: сумма в корзине ({actual}) равна расчётной ({expected})")
    private void checkTotalPrice(int actual, int expected) {
        org.junit.jupiter.api.Assertions.assertEquals(
                expected, actual,
                "Сумма в корзине не совпадает с расчётной"
        );
    }
}
