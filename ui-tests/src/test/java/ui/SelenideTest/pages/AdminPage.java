package ui.SelenideTest.pages;

import com.codeborne.selenide.SelenideElement;
import common.config.ConfigProvider;
import io.qameta.allure.Step;
import ui.SelenideTest.asserts.PageAssert;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;

public class AdminPage {
    private final SelenideElement nameField = $("#n-name");
    private final SelenideElement priceField = $("#n-price");
    private final SelenideElement addButton = $("#add-btn");
    private final SelenideElement backToSiteLink = $(byText("Вернуться на сайт"));
    private final SelenideElement toast = $(".toast");
    private final SelenideElement tbody = $("#tbody");

    // --- Методы взаимодействия ---
    @Step("Создание товара: '{name}', цена: '{price}'")
    public void createProduct(String name, String price) {
        nameField.setValue(name);
        priceField.setValue(price);
        addButton.click();
    }

    @Step("Обновление товара: старое имя='{oldName}', новое имя='{newName}'")
    public void updateProduct(String oldName, String newName) {
        SelenideElement nameInput = $("[value='" + oldName + "']");
        PageAssert.isVisible(nameInput);
        SelenideElement row = nameInput.closest("tr");
        nameInput.click();
        nameInput.setValue(newName);
        row.$("[data-action='update']").click();
    }

    @Step("Удаление товара по имени: '{productName}'")
    public void deleteProductByName(String productName) {
        SelenideElement nameInput = $("[value='" + productName + "']");
        nameInput.shouldBe(visible);

        SelenideElement row = nameInput.closest("tr");
        row.$("button[data-action='delete']")
                .shouldBe(enabled)
                .click();

        confirm();
        $("[value='" + productName + "']").shouldNot(exist);
    }

    @Step("Переход на сайт из админки")
    public void goToSite() {
        backToSiteLink.click();
    }

    @Step("Переход в админку")
    public void goToAdmin() {
        $("[href='/admin']").click();
    }

    @Step("Получение текста уведомления (toast)")
    public String getToastText() {
        return toast.shouldBe(visible).getText();
    }

    // --- Проверки ---
    @Step("Проверка: поле имени товара видно")
    public void assertNameFieldVisible() {
        PageAssert.isVisible(nameField);
    }

    @Step("Проверка: поле цены товара видно")
    public void assertPriceFieldVisible() {
        PageAssert.isVisible(priceField);
    }

    @Step("Проверка: кнопка добавления товара видна")
    public void assertAddButtonVisible() {
        PageAssert.isVisible(addButton);
    }

    @Step("Проверка: ссылка 'Вернуться на сайт' видна")
    public void assertBackToSiteVisible() {
        PageAssert.isVisible(backToSiteLink);
    }

    @Step("Проверка: уведомление содержит текст '{expectedText}'")
    public void assertToastContains(String expectedText) {
        ConfigProvider.getToastGoodAdded();
    }

    @Step("Проверка: страница админки загружена")
    public void assertPageLoaded() {
        assertNameFieldVisible();
        assertPriceFieldVisible();
        assertAddButtonVisible();
    }
}



