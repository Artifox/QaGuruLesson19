package tests;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static helpers.CustomApiListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class DemoWebShopTest extends TestBase {


    @Test
    @DisplayName("Sign in user")
    void userRegistrationTest() {

        open("/Themes/DefaultClean/Content/images/logo.png");
        step("[API] Register a new user", () -> {
            given()
                    .filter(withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded")
                    .cookie("__RequestVerificationToken", "SX4rSDHiZ4Om1-IjCIX2lD-b7Eft9TwCn331Oz" +
                            "J0zWlB73f14yYeem65UYfr9fZdK3DzQFYDySHPkBe8tRf1_HKOkgXE6vxqRwJ9DTiJN5w1")
                    .formParam("__RequestVerificationToken", "FOc02jnOI29B-ADOzGcl-XmH" +
                            "pIsdxDZQi_lh3ahKzVv4c1AE9DM-NqzoOR2yvl4WsiAhDWl5zb4TXhXqkqpWmKdSZggBxeBsqxaZZqUJZOY1")
                    .formParam("Gender", "M")
                    .formParam("FirstName", testData.firstName)
                    .formParam("LastName", testData.lastName)
                    .formParam("Email", testData.email)
                    .formParam("Password", testData.passwordRandom)
                    .formParam("ConfirmPassword", testData.passwordRandom)
                    .formParam("register-button", "Register")
                    .log().all()
                    .when()
                    .post("/register")
                    .then()
                    .log().all()
                    .statusCode(302);
        });

        step("Open login form", () ->
                open("/login"));

        step("Authorization with created user", () -> {
            $("#Email").setValue(testData.email);
            $("#Password").setValue(testData.passwordRandom).pressEnter();
        });

        step("Verify that new user is authorized", () -> {
            $(".account").shouldHave(text(testData.email));
        });

        step("Log out", () -> {
            $(".ico-logout").click();

        });
    }


    @Test
    @DisplayName("Editing profile")
    void editingProfile() {

        step("[API] Authorization", () -> {
            testData.authCookieValue = given()
                    .filter(withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .formParam("Email", testData.email)
                    .formParam("Password", testData.passwordRandom)
                    .when()
                    .post("/login")
                    .then()
                    .statusCode(302)
                    .extract()
                    .cookie(testData.authCookieName);
        });

        step("Set cookie", () -> {
            open("");
            Cookie authCookie = new Cookie(testData.authCookieName, testData.authCookieValue);
            WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
        });

        step("Changing user data", () -> {
            open("/customer/info");
            $("[for=gender-female]").click();
            $("#FirstName").setValue("ChackNorris");
            $("[value=Save]").click();
        });

        step("Verify name and", () -> {
            $("[for=gender-female]").shouldHave(text("Female"));
            $("#FirstName").shouldHave((attribute("value", "ChackNorris")));
        });
    }
}