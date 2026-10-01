package ui.SelenideTest.asserts;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;

public class PageAssert {

    @Step("Проверка: элемент видим на странице")
    public static void isVisible(SelenideElement element) {
        element.shouldBe(visible);
    }

    @Step("Проверка: текст элемента точно равен '{expectedText}'")
    public static void hasText(SelenideElement element, String expectedText) {
        element.shouldHave(exactText(expectedText));
    }

    @Step("Проверка: элемент содержит текст '{expectedSubstring}'")
    public static void containsText(SelenideElement element, String expectedSubstring) {
        element.shouldHave(text(expectedSubstring));
    }

    @Step("Проверка: элемент не существует на странице")
    public static void notExists(SelenideElement element) {
        element.shouldNot(exist);
    }
}