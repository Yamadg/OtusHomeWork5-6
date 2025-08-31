package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FormPage {
    private static final Logger logger = LogManager.getLogger(FormPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы для элементов формы
    private final By nameField = By.id("username");
    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By confirmPasswordField = By.id("confirm_password");
    private final By dateField = By.id("birthdate");
    private final By languageSelect = By.id("language_level");
    private final By submitButton = By.xpath("//input[@type='submit']");

    private final By submittedDataPreTag = By.id("output");

    public FormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }


    public void open(String url) {
        logger.info("Открытие URL формы: {}", url);
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));
        logger.debug("Страница формы успешно загружена.");
    }


    public void enterName(String name) {
        logger.debug("Ввод имени: {}", name);
        driver.findElement(nameField).sendKeys(name);
    }


    public void enterEmail(String email) {
        logger.debug("Ввод Email: {}", email);
        driver.findElement(emailField).sendKeys(email);
    }


    public void enterPassword(String password) {
        logger.debug("Ввод пароля.");
        driver.findElement(passwordField).sendKeys(password);
    }


    public void enterConfirmPassword(String confirmPassword) {
        logger.debug("Ввод подтверждения пароля.");
        driver.findElement(confirmPasswordField).sendKeys(confirmPassword);
    }


    public void enterDateOfBirth(String date) {
        logger.debug("Ввод даты рождения: {}", date);
        driver.findElement(dateField).sendKeys(date);
    }


    public void selectLanguageLevel(String level) {
        logger.debug("Выбор уровня языка: {}", level);
        WebElement languageDropdown = driver.findElement(languageSelect);
        Select select = new Select(languageDropdown);
        select.selectByVisibleText(level);
        logger.info("Уровень языка '{}' успешно выбран.", level);
    }


    public void clickSubmit() {
        logger.info("Нажатие кнопки 'Отправить'.");
        driver.findElement(submitButton).click();
    }


    public Map<String, String> getSubmittedData() {
        logger.info("Попытка получить отправленные данные со страницы.");
        WebElement preElement = wait.until(ExpectedConditions.visibilityOfElementLocated(submittedDataPreTag));
        String preText = preElement.getText();
        logger.debug("Необработанные отправленные данные из <pre> тега:\n{}", preText);

        Map<String, String> submittedData = new HashMap<>();
        String[] lines = preText.split("\n");
        for (String line : lines) {
            if (line.contains(":")) {
                int colonIndex = line.indexOf(":");
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                submittedData.put(key, value);
            }
        }
        logger.info("Отправленные данные успешно получены: {}", submittedData);
        return submittedData;
    }
}