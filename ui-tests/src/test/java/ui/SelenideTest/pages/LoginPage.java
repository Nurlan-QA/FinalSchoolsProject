package ui.SelenideTest.pages;

import com.codeborne.selenide.SelenideElement;
import common.config.ConfigProvider;
import io.qameta.allure.Step;
import ui.SelenideTest.asserts.PageAssert;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public final SelenideElement loginField = $("#username");
    private final SelenideElement passwordField = $("#password");
    private final SelenideElement submitButton = $("button[type='submit']");

    // --- Методы взаимодействия ---

    @Step("Ввод логина: '{login}'")
    public void enterLogin(String login) {
        loginField.setValue(login);
    }

    @Step("Ввод пароля")
    public void enterPassword(String password) {
        passwordField.setValue(password);
    }

    @Step("Нажатие кнопки входа")
    public void clickLoginButton() {
        submitButton.click();
    }

    @Step("Авторизация в админку: логин = '{login}'")
    public void login(String login, String password) {
        enterLogin(login);
        enterPassword(password);
        clickLoginButton();
    }

    // --- Проверки ---

    @Step("Проверка: сообщение об ошибке авторизации видно")
    public void assertLoginErrorVisible() {
        SelenideElement errorMessage = $(byText(ConfigProvider.getErrorInvalidCredentials()));
        PageAssert.isVisible(errorMessage);
    }

    @Step("Проверка: поле логина видно")
    public void assertLoginFieldVisible() {
        PageAssert.isVisible(loginField);
    }

    @Step("Проверка: поле пароля видно")
    public void assertPasswordFieldVisible() {
        PageAssert.isVisible(passwordField);
    }

    @Step("Проверка: кнопка отправки формы видна")
    public void assertSubmitButtonVisible() {
        PageAssert.isVisible(submitButton);
    }

    @Step("Проверка: страница логина загружена")
    public void assertPageLoaded() {
        assertLoginFieldVisible();
        assertPasswordFieldVisible();
        assertSubmitButtonVisible();
    }
}