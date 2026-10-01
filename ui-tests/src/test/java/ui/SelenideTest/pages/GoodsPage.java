package ui.SelenideTest.pages;

import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ui.SelenideTest.asserts.PageAssert;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;

public class GoodsPage {

    private final SelenideElement logoSmartShop = $("#main-title");
    private final SelenideElement adminLink = $("[href='/admin']");
    private final SelenideElement openCartBtn = $("#open-cart-btn");
    private final ElementsCollection goodsList = $$(".product-card");
    private final ElementsCollection addToCartButtons = $$("button[data-action='add-to-cart']");
    private final SelenideElement successNotification = $(".notification-success");

    // --- Методы взаимодействия ---

    @Step("Открыть корзину")
    public void openCart() {
        openCartBtn.click();
    }

    @Step("Перейти в админку")
    public void goToAdmin() {
        adminLink.click();
    }

    @Step("Добавить товар '{productName}' в корзину")
    public void addProductToCart(String productName) {
        $(String.format("button[data-name='%s']", productName)).click();
    }

    @Step("Получить текст уведомления об успехе")
    public String getNotificationText() {
        return successNotification.shouldBe(com.codeborne.selenide.Condition.visible).getText();
    }

    @Step("Перетащить товар '{productName}' в корзину (drag-and-drop)")
    public void dragProductToCart(String productName) {
        SelenideElement product = $(byAttribute("data-name", productName));
        SelenideElement cartBtn = $("#open-cart-btn");
        product.dragAndDrop(DragAndDropOptions.to(cartBtn));
    }


    // --- Проверки ---

    @Step("Проверка: логотип магазина виден")
    public void assertLogoVisible() {
        PageAssert.isVisible(logoSmartShop);
    }

    @Step("Проверка: ссылка на админку видна")
    public void assertAdminLinkVisible() {
        PageAssert.isVisible(adminLink);
    }

    @Step("Проверка: кнопка корзины видна")
    public void assertCartButtonVisible() {
        PageAssert.isVisible(openCartBtn);
    }

    @Step("Проверка: товар '{productName}' виден на витрине")
    public void assertProductVisible(String productName) {
        String selector = String.format("[data-name='%s']", productName);
        PageAssert.isVisible($(selector));
    }

    @Step("Проверка: товар '{productName}' содержит текст на витрине")
    public void assertProductHasText(String productName) {
        String selector = String.format("[data-name='%s']", productName);
        PageAssert.containsText($(selector), productName);
    }

    @Step("Проверка: счётчик корзины показывает {expected}")
    public void assertCartCount(int expected) {
        $("#cart-count").shouldHave(text(String.valueOf(expected)));
    }

    @Step("Проверка: страница витрины загружена")
    public void assertPageLoaded() {
        assertLogoVisible();
        assertAdminLinkVisible();
        assertCartButtonVisible();
    }
}
