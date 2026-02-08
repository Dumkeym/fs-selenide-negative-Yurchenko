package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationPage extends BasePage {

    private final By signupLink = By.cssSelector(".signup a");
    private final By firstNameInput = By.id("create_first");
    private final By lastNameInput = By.id("create_last");
    private final By emailInput = By.id("create_email");
    private final By passwordInput = By.id("password_meter");
    private final By confirmPasswordInput = By.id("create_passwordmatch");
    private final By registerButton = By.cssSelector(".btn-beoro-1");

    // Локатор для JQuery ошибок и для серверного алерта
    private final By anyError = By.cssSelector("label.error, .alert-error");

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть форму регистрации")
    public RegistrationPage openForm() {
        driver.get("https://log.finalsurge.com/");
        click(signupLink);
        return this;
    }

    @Step("Заполнить форму")
    public RegistrationPage fillForm(String fn, String ln, String em, String pw, String cpw) {
        if (fn != null) type(firstNameInput, fn);
        if (ln != null) type(lastNameInput, ln);
        if (em != null) type(emailInput, em);
        if (pw != null) type(passwordInput, pw);
        if (cpw != null) type(confirmPasswordInput, cpw);
        return this;
    }

    @Step("Нажать 'Create New Account'")
    public RegistrationPage submit() {
        click(registerButton);
        return this;
    }

    @Step("Получить тексты всех видимых ошибок")
    public String getAllErrorsText() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(anyError));
        } catch (Exception e) {
            return "";
        }

        List<WebElement> errors = driver.findElements(anyError);
        return errors.stream()
                .map(WebElement::getText)
                .collect(Collectors.joining(" | "));
    }
}