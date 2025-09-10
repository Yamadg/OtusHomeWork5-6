package tests;

import factory.WebDriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import pages.FormPage;

import java.time.Duration;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FormAutomationTest {

    private static final Logger logger = LogManager.getLogger(FormAutomationTest.class);
    private WebDriver driver;
    private FormPage formPage;

    // Значения по умолчанию, если параметры не переданы через командную строку
    private static final String DEFAULT_BROWSER = "chrome";
    private static final String DEFAULT_USER_NAME = "Тестовый Пользователь1";
    private static final String DEFAULT_USER_EMAIL = "user@example.com";
    private static final String DEFAULT_USER_PASSWORD = "StrongPassword123!";
    private static final String DEFAULT_DOB = "01011990";
    private static final String DEFAULT_LANGUAGE_LEVEL = "Продвинутый";

    private String browserName;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String dob;
    private String languageLevel;

    private static final String FORM_URL = "https://otus.home.kartushin.su/form.html";

    @BeforeAll
    void setup() {
        browserName = System.getProperty("browser", DEFAULT_BROWSER);
        userName = System.getProperty("userName", DEFAULT_USER_NAME);
        userEmail = System.getProperty("userEmail", DEFAULT_USER_EMAIL);
        userPassword = System.getProperty("userPassword", DEFAULT_USER_PASSWORD);
        dob = DEFAULT_DOB;
        languageLevel = DEFAULT_LANGUAGE_LEVEL;

        logger.info("Инициализация WebDriver для браузера: {}", browserName);
        logger.info("Используемые данные пользователя: Имя='{}', Email='{}'", userName, userEmail);

        try {
            driver = WebDriverFactory.create(browserName);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            formPage = new FormPage(driver);
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка инициализации WebDriver: {}", e.getMessage());
            throw new RuntimeException("Ошибка инициализации WebDriver: " + e.getMessage(), e);
        }
    }

    @Test
    @DisplayName("Проверка заполнения формы и корректности отправленных данных")
    void testFormSubmission() {
        logger.info("Начало теста заполнения и отправки формы.");
        formPage.open(FORM_URL);

        logger.debug("Заполнение полей формы...");
        formPage.enterName(userName);
        formPage.enterEmail(userEmail);
        formPage.enterPassword(userPassword);
        formPage.enterConfirmPassword(userPassword);
        formPage.enterDateOfBirth(dob);
        formPage.selectLanguageLevel(languageLevel);

        formPage.clickSubmit();
        logger.info("Форма отправлена. Получение отправленных данных.");

        Map<String, String> submittedData = formPage.getSubmittedData();

        // Используем методы страницы для проверок вместо ассертов в тесте
        formPage.verifyAllSubmittedData(submittedData, userName, userEmail, dob, languageLevel);

        logger.info("Тест заполнения формы успешно завершен, данные проверены.");
    }

    @AfterAll
    void teardown() {
        if (driver != null) {
            logger.info("Закрытие WebDriver.");
            driver.quit();
        }
    }
}