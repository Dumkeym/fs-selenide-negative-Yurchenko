package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.Attachments;
import java.io.IOException;
import java.nio.file.*;

public abstract class BaseTest {
    protected WebDriver driver;

    @Parameters({"browser", "headless"})
    @BeforeMethod(description = "Инициализация драйвера")
    public void setUp(@Optional("chrome") String browser, @Optional("false") String headless) {
        boolean isHeadless = Boolean.parseBoolean(headless);

        switch (browser.toLowerCase()) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (isHeadless) options.addArguments("-headless");
                driver = new FirefoxDriver(options);
            }
            case "edge" -> {
                System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "/msedgedriver.exe");
                EdgeOptions options = new EdgeOptions();
                if (isHeadless) options.addArguments("--headless=new");
                driver = new EdgeDriver(options);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-search-engine-choice-screen");
                if (isHeadless) options.addArguments("--headless=new");
                driver = new ChromeDriver(options);
            }
        }
        driver.manage().window().maximize();
    }

    @AfterMethod(alwaysRun = true, description = "Скриншот и закрытие браузера")
    public void tearDown(ITestResult result) {
        if (driver != null) {
            Attachments.takeScreenshot(driver, "Final state: " + result.getName());
            driver.quit();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void copyEnvFile() {
        try {
            Path source = Paths.get("src/test/resources/environment.properties");
            Path target = Paths.get("target/allure-results/environment.properties");
            if (Files.exists(source)) {
                Files.createDirectories(target.getParent());
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.err.println("Failed to copy environment.properties: " + e.getMessage());
        }
    }
}