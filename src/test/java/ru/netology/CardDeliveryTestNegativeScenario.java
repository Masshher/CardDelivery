package ru.netology;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selectors.byText;

public class CardDeliveryTestNegativeScenario {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").sendKeys(Keys.CONTROL,"a" + Keys.DELETE);
    }

    @Test//Использование города не административного центра
    void shouldUseCityNotCivicCenter() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Канаш");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"city\"].input_invalid").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test//Использование города с ошибкой в названии
    void shouldUseCityNameWithError() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Масква");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"city\"].input_invalid").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test//Пустая строка город
    void shouldEmptyLineCity() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"city\"].input_invalid").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test//Планируемая дата ранее трёх дней с текущей даты.
    void shouldDateBeforeThreeDays() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(2));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"date\"]").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test//Пустая строка даты
    void shouldEmptyLineDate() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").setValue("");
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"date\"]").shouldHave(exactText("Неверно введена дата"));
    }

    @Test//Пустая строка Фамилия и имя
    void shouldEmptyLineName() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"name\"].input_invalid").shouldHave(exactText("Фамилия и имя Поле обязательно для заполнения"));
    }

    @Test// Имя введено на латинице
    void shouldNameInLatin() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Ivanov Ivan");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"name\"].input_invalid").shouldHave(exactText("Фамилия и имя Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test// В имени есть знак препинания
    void shouldPunctuationMarksInName() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван.");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"name\"].input_invalid").shouldHave(exactText("Фамилия и имя Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test // Строка телефон пустая
    void shouldEmptyLinePhone() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"phone\"].input_invalid").shouldHave(exactText("Мобильный телефон Поле обязательно для заполнения"));
    }

    @Test // Телефон без знака плюс
    void PhoneNoPlusSign() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"phone\"].input_invalid").shouldHave(exactText("Мобильный телефон Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test // Телефон состоит из 10 цифр
    void PhoneTenDigits() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+7999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"phone\"].input_invalid").shouldHave(exactText("Мобильный телефон Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test // Телефон состоит из 12 цифр
    void PhoneTwelveDigits() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+799999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"phone\"].input_invalid").shouldHave(exactText("Мобильный телефон Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test // знаке в строке телефон
    void PhoneWithSigns() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+7(999)999-99-99");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $("[data-test-id=\"phone\"].input_invalid").shouldHave(exactText("Мобильный телефон Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test // Не поставлен чек бокс
    void WithOutCheckbox() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $(".button__content").click();
        $("[data-test-id=\"agreement\"]").should(cssClass("input_invalid"));
    }
}
