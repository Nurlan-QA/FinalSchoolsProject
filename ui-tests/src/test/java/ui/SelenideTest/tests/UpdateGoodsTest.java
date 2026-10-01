package ui.SelenideTest.tests;

import common.config.ConfigProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.BaseTestSelenide;
import ui.SelenideTest.pages.AdminPage;
import ui.SelenideTest.pages.GoodsPage;
import ui.SelenideTest.pages.LoginPage;
import ui.SelenideTest.pages.ProductCleanup;

import static com.codeborne.selenide.Selenide.open;

public class UpdateGoodsTest extends BaseTestSelenide {

    private final LoginPage loginPage = new LoginPage();
    private final AdminPage adminPage = new AdminPage();
    private final GoodsPage goodsPage = new GoodsPage();
    private final ProductCleanup productCleanup = new ProductCleanup();

    private final long uniqueSuffix = System.nanoTime() % 1_000_000;
    private final String originalProductName = ConfigProvider.getProductName() + "_" + uniqueSuffix;
    private final String updatedProductName = originalProductName + "_updated";

    @AfterEach
    void cleanUp() {
        productCleanup.removeProductByName(updatedProductName);
    }

    @Test
    void goodsUpdate() {
        // Вход в админку через LoginPage
        open("/admin");
        loginPage.assertPageLoaded();
        loginPage.login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());

        adminPage.assertPageLoaded();

        // Создаём товар
        adminPage.createProduct(originalProductName, "100");
        adminPage.assertToastContains("Товар успешно добавлен");

        // Идём на витрину и проверяем
        adminPage.goToSite();
        goodsPage.assertPageLoaded();
        goodsPage.assertProductVisible(originalProductName);
        goodsPage.assertProductHasText(originalProductName);

        // Возвращаемся в админку для редактирования
        goodsPage.goToAdmin();
        adminPage.assertPageLoaded();

        // Редактируем товар через AdminPage
        adminPage.updateProduct(originalProductName, updatedProductName);

        // Идём на витрину и проверяем изменения
        adminPage.goToSite();
        goodsPage.assertPageLoaded();
        goodsPage.assertProductVisible(updatedProductName);
        goodsPage.assertProductHasText(updatedProductName);
    }
}