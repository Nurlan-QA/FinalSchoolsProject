package ui.SelenideTest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import common.config.ConfigProvider;
import common.config.ConfigPrinter;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ui.SelenideTest.pages.AdminPage;
import ui.SelenideTest.pages.GoodsPage;
import ui.SelenideTest.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public abstract class BaseTestSelenide {

    protected final LoginPage loginPage = new LoginPage();
    protected final GoodsPage goodsPage = new GoodsPage();
    protected final AdminPage adminPage = new AdminPage();

    @BeforeEach
    void setup() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
        );

        ConfigPrinter.printConfig();

        Configuration.browser = ConfigProvider.getBrowser();
        Configuration.browserSize = ConfigProvider.getBrowserSize();
        Configuration.timeout = ConfigProvider.getTimeout();
        Configuration.baseUrl = ConfigProvider.getBaseUrl();

        open("/");
    }

    @AfterEach
    void quitTests() {
        Selenide.closeWebDriver();
    }

    protected void loginToAdmin() {
        goodsPage.goToAdmin();
        loginPage.assertPageLoaded();
        loginPage.login(
                ConfigProvider.getAdminLogin(),
                ConfigProvider.getAdminPassword()
        );
    }

    protected void loginToAdminInvalid(String username, String password) {
        goodsPage.goToAdmin();
        loginPage.login(username, password);
    }

    protected void deleteProduct(String productName) {
        open("/admin");

        if (loginPage.loginField.isDisplayed()) {
            loginPage.login(
                    ConfigProvider.getAdminLogin(),
                    ConfigProvider.getAdminPassword()
            );
        }

        adminPage.deleteProductByName(productName);
        System.out.println("Тестовые данные успешно удалены!");
    }
}