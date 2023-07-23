package ru.netology;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selectors.byText;

public class CardDeliveryTest {

    @Test
    void shouldArrangeDeliveryOfCard() {
        open("http://localhost:9999/");
        $("[data-test-id=\"city\"] [placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id=\"name\"] [name=\"name\"]").setValue("Иванов Иван");
        $("[data-test-id=\"phone\"] [name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button__content").click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));

    }
}
