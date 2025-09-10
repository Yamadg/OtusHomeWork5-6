package factory;

import enums.DriverType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public class WebDriverFactory {

    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);

    public static WebDriver create(String webDriverName, AbstractDriverOptions options) {
        logger.info("Попытка создать WebDriver для: {}", webDriverName);
        DriverType driverType = DriverType.fromString(webDriverName);
        WebDriver driver;

        switch (driverType) {
            case CHROME:
                ChromeOptions chromeOptions = (options instanceof ChromeOptions) ? (ChromeOptions) options : new ChromeOptions();
                driver = new ChromeDriver(chromeOptions);
                break;
            case FIREFOX:
                FirefoxOptions firefoxOptions = (options instanceof FirefoxOptions) ? (FirefoxOptions) options : new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
                break;
            default:
                logger.error("Неподдерживаемый тип WebDriver: {}", webDriverName);
                throw new IllegalArgumentException("Неподдерживаемый WebDriver: " + webDriverName);
        }

        logger.info("{} WebDriver успешно создан.", driverType.name());
        return driver;
    }


    public static WebDriver create(String webDriverName) {
        return create(webDriverName, null);
    }
}
