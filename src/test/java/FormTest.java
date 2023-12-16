import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;


public class FormTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void changedDate() {

        DataGenerator.UserData user = DataGenerator.Registration.generateUser("ru");

        String planningDate = DataGenerator.generateDate(3);
        String newPlaningDate = DataGenerator.generateDate(4);

        $$(".input__control").first().setValue(user.getCity());
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button.button").first().click();
        $(byText("Успешно!"))
                .shouldBe(visible);
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + planningDate))
                .shouldBe(visible);
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input")
                .setValue(newPlaningDate);
        $$("button.button").first().click();
        $("[data-test-id=replan-notification] .notification__title")
                .shouldHave(Condition.text("Необходимо подтверждение"))
                .shouldBe(visible);
        $("[data-test-id=replan-notification] .notification__content")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[data-test-id=replan-notification]").click();
        $$("button.button").last().click();
        $("[data-test-id=success-notification] .notification__title")
                .shouldHave(Condition.text("Успешно!"))
                .shouldBe(visible);
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + newPlaningDate))
                .shouldBe(visible);
    }
}