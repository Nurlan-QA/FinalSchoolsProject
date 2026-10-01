package ui.SelenideTest.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import common.config.ConfigProvider;
import io.qameta.allure.Step;
import ui.SelenideTest.asserts.PageAssert;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;

public class CartPage {

    private final SelenideElement totalPriceElement = $("#total-price");
    private final SelenideElement cartCount = $("#cart-count");
    private final SelenideElement makeOrderBtn = $("#makeOrder");
    private final SelenideElement closeModalBtn = $("#close-modal");
    private final ElementsCollection cartItems = $$(".cart-item");

    // --- Методы взаимодействия ---
    @Step("Нажать кнопку 'Оформить заказ'")
    public void makeOrder() {
        makeOrderBtn.click();
    }

    @Step("Закрыть модальное окно корзины")
    public void closeCartModal() {
        closeModalBtn.click();
    }

    @Step("Получить количество товаров в корзине")
    public int getCartCount() {
        return Integer.parseInt(cartCount.getText());
    }

    @Step("Получить итоговую сумму корзины")
    public int getTotalPrice() {
        String raw = totalPriceElement.getText();
        String digitsOnly = raw.replaceAll("[^\\d]", "");
        return digitsOnly.isEmpty() ? 0 : Integer.parseInt(digitsOnly);
    }

    @Step("Удалить товар '{productName}' из корзины")
    public void removeProductFromCart(String productName) {
        SelenideElement cartItemRow = $$(".cart-item")
                .filterBy(text(productName))
                .shouldHave(size(1))
                .get(0);
        cartItemRow.$(byAttribute("data-action", "remove"))
                .shouldBe(visible)
                .click();
        cartItemRow.shouldNotBe(exist);
    }

    // --- Проверки ---

    @Step("Проверка: товар '{productName}' есть в корзине")
    public void shouldBeGoodInCart(String productName) {
        $(".cart-item").shouldHave(text(productName));
    }

    @Step("Проверка: товар '{productName}' отсутствует в корзине")
    public void goodNotInCart(String productName) {
        $(".cart-item").shouldNot(exist);
    }

    @Step("Проверка: итоговая сумма видна")
    public void assertTotalPriceVisible() {
        PageAssert.isVisible(totalPriceElement);
    }

    @Step("Проверка: кнопка 'Оформить заказ' видна")
    public void assertMakeOrderButtonVisible() {
        PageAssert.isVisible(makeOrderBtn);
    }

    @Step("Проверка: количество товаров в корзине равно {expectedCount}")
    public void assertItemsCount(int expectedCount) {
        cartItems.shouldHave(size(expectedCount));
    }

    @Step("Проверка: товары в корзине содержат текст '{expectedText}'")
    public void assertItemsContainText(String expectedText) {
        cartItems.filterBy(text(expectedText)).shouldHave(sizeGreaterThan(0));
    }

    @Step("Проверка: заказ принят")
    public void assertOrderAccepted() {
        $$(".toast")
                .findBy(text(ConfigProvider.getToastOrderAccepted()))
                .shouldBe(visible);
    }
}
