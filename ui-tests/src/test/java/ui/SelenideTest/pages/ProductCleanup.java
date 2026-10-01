package ui.SelenideTest.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import common.config.ConfigProvider;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProductCleanup {

    private final LoginPage loginPage = new LoginPage();
    private static final int COUNT = 3;

    // Бизнес-метод: удаляет сразу 3 тестовых товара
    @Step("Удаление всех тестовых товаров (Notebook_1, Notebook_2, Notebook_3)")
    public void removeTestProducts() {
        for (int i = 1; i <= COUNT; i++) {
            String productName = ConfigProvider.getProductName() + "_" + i;
            removeProductByName(productName);
        }
    }

    // Бизнес-метод: полный сценарий — открыть админку, залогиниться, удалить товар
    @Step("Удаление товара '{productName}' через админку")
    public void removeProductByName(String productName) {
        open("/admin");

        SelenideElement usernameField = $("#username");

        if (usernameField.isDisplayed()) {
            loginPage.enterLogin(ConfigProvider.getAdminLogin());
            loginPage.enterPassword(ConfigProvider.getAdminPassword());
            loginPage.clickLoginButton();

            $("#tbody").shouldBe(visible, Duration.ofSeconds(10));
        }

        $("#tbody").shouldBe(visible, Duration.ofSeconds(5));

        String selector = "input[value='" + productName + "']";

        SelenideElement inputField = $(selector);
        if (!inputField.exists()) {
            System.out.println("Товар '" + productName + "' не найден, пропускаем.");
            return;
        }

        SelenideElement row = inputField.closest("tr");

        row.$("button[data-action='delete']")
                .shouldBe(enabled)
                .click();

        confirm();

        $(selector).shouldNot(exist, Duration.ofSeconds(5));
        System.out.println("Товар '" + productName + "' успешно удалён!");
    }
}
