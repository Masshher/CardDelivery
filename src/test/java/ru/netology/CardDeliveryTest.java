package ru.netology;

import com.codeborne.selenide.Condition;
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

public class CardDeliveryTest {
    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").sendKeys(Keys.CONTROL,"a" + Keys.DELETE);
    }

    @Test//Успешное оформление доставки карты
    void shouldArrangeDeliveryOfCard() {

        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + generateDate(5)), Duration.ofSeconds(15)).shouldBe(visible);
    }

    @Test//Использование в названии города дефис
    void shouldUseCityNameWithHyphen() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Горно-Алтайск");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(5));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + generateDate(5)), Duration.ofSeconds(15)).shouldBe(visible);
    }

    @Test//Использование в фамилии дефис
    void shouldUseSurnameWithHyphen() {
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"date\"] [placeholder=\"Дата встречи\"]").val(generateDate(3));
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Римский-Корсаков Николай");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + generateDate(3)), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
