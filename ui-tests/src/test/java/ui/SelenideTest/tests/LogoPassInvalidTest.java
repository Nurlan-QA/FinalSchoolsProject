package ui.SelenideTest.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ui.SelenideTest.BaseTestSelenide;
import ui.SelenideTest.pages.LoginPage;
import common.config.ConfigProvider;

// Попытаться войти в админку с неверным логином и паролем.

@Feature("Авторизация")
@Story("Негативные сценарии")
@Severity(SeverityLevel.BLOCKER)

public class LogoPassInvalidTest extends BaseTestSelenide {
    private final LoginPage loginPage = new LoginPage();

    @Description("Авторизация с некорректными логиним и паролем")
    @Tag("smoke")
    @Test
    void invalidLoginTest() {

        // Вход в админку с неверными данными
        loginToAdminInvalid(ConfigProvider.getInvalidLogin(), ConfigProvider.getInvalidPassword());

        // Проверка результата: должно появиться сообщение об ошибке
        loginPage.assertLoginErrorVisible();
    }
}