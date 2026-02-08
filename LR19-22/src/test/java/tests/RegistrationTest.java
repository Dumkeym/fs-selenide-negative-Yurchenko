package tests;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.RegistrationPage;

@Epic("Registration")
@Feature("Negative Scenarios")
public class RegistrationTest extends BaseTest {

    @Test(description = "1. Пустая форма: проверка обязательности")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmptyForm() {
        RegistrationPage page = new RegistrationPage(driver);
        String errors = page.openForm().submit().getAllErrorsText().toLowerCase();

        Assert.assertTrue(errors.contains("required") || errors.contains("please enter"),
                "Текст ошибок при пустой форме: " + errors);
    }

    @Test(description = "2. Невалидный e-mail")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidEmail() {
        RegistrationPage page = new RegistrationPage(driver);
        String errors = page.openForm()
                .fillForm("Ivan", "Petrov", "test.com", "Pass123!", "Pass123!")
                .submit()
                .getAllErrorsText().toLowerCase();

        Assert.assertTrue(errors.contains("email"), "Ожидалась ошибка формата email. Найдено: " + errors);
    }

    @Test(description = "3. Короткий пароль")
    @Severity(SeverityLevel.NORMAL)
    public void testShortPassword() {
        RegistrationPage page = new RegistrationPage(driver);
        String errors = page.openForm()
                .fillForm("Ivan", "Petrov", "test@test.com", "123", "123")
                .submit()
                .getAllErrorsText().toLowerCase();

        Assert.assertTrue(errors.contains("password") || errors.contains("characters"),
                "Нет ошибки на длину пароля. Ошибки: " + errors);
    }

    @Test(description = "4. Несовпадение паролей")
    @Severity(SeverityLevel.NORMAL)
    public void testPasswordMismatch() {
        RegistrationPage page = new RegistrationPage(driver);
        String errors = page.openForm()
                .fillForm("Ivan", "Petrov", "test@test.com", "Pass123!", "Diff123!")
                .submit()
                .getAllErrorsText().toLowerCase();

        Assert.assertTrue(errors.contains("match") || errors.contains("same"),
                "Нет ошибки несовпадения паролей. Ошибки: " + errors);
    }

    @Test(description = "5. Отсутствие имени")
    @Severity(SeverityLevel.NORMAL)
    public void testMissingFirstName() {
        RegistrationPage page = new RegistrationPage(driver);
        String errors = page.openForm()
                .fillForm("", "Petrov", "test@test.com", "Pass123!", "Pass123!")
                .submit()
                .getAllErrorsText().toLowerCase();

        Assert.assertTrue(errors.contains("required"), "Нет ошибки обязательного поля Имя");
    }

    @Test(description = "6. Отсутствие фамилии")
    @Severity(SeverityLevel.NORMAL)
    public void testMissingLastName() {
        RegistrationPage page = new RegistrationPage(driver);
        String errors = page.openForm()
                .fillForm("Ivan", "", "test@test.com", "Pass123!", "Pass123!")
                .submit()
                .getAllErrorsText().toLowerCase();

        Assert.assertTrue(errors.contains("required"), "Нет ошибки обязательного поля Фамилия");
    }
}