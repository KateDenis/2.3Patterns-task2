package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

class AuthTest {
    //
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        $("[name=login]").setValue(registeredUser.getLogin());
        $("[name=password]").setValue(registeredUser.getPassword());
        $("[type=button]").click();
        $("h2").shouldBe(Condition.visible)
                .shouldHave(text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        DataGenerator.RegistrationDto user = getUser("active");
        $("[name=login]").setValue(user.getLogin());
        $("[name=password]").setValue(user.getPassword());
        $("[type=button]").click();
        $("[class=notification__content]").shouldBe(Condition.visible, Duration.ofMillis(14000))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("blocked");
        $("[name=login]").setValue(registeredUser.getLogin());
        $("[name=password]").setValue(registeredUser.getPassword());
        $("[type=button]").click();
        $("[class=notification__content]").shouldBe(Condition.visible, Duration.ofMillis(14000))
                .shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        $("[name=login]").setValue(registeredUser.getLogin());
        $("[name=password]").setValue(getRandomPassword());
        $("[type=button]").click();
        $("[class=notification__content]").shouldBe(Condition.visible, Duration.ofMillis(14000))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        $("[name=login]").setValue(getRandomLogin());
        $("[name=password]").setValue(registeredUser.getPassword());
        $("[type=button]").click();
        $("[class=notification__content]").shouldBe(Condition.visible, Duration.ofMillis(14000))
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}
