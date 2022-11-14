
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;


@Listeners(TestListeners.class)
public class InsiderWork {


    public static WebDriver driver;
    public static Properties prop;

    public static WebDriver InitializeDriver(WebDriver driver) throws IOException, InterruptedException {

        prop = new Properties();
        FileInputStream fis = new FileInputStream("C:\\Users\\SAMSUNG\\insiderPrjct\\src\\main\\resources\\data.properties");
        prop.load(fis);

        String browserName = prop.getProperty("browser");
        System.out.println(browserName);

        if (browserName.equalsIgnoreCase("chrome")) {
            System.setProperty("wedriver.chrome.driver", "C:\\Users\\SAMSUNG\\chromedriver.exe");
            driver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("firefox")) {
            System.setProperty("wedriver.gecko.driver", "C:\\Users\\SAMSUNG\\geckodriver.exe");
            driver = new FirefoxDriver();

        }
        return driver;
    }

    public static class UseInsider {


        public static void initialize() throws IOException, InterruptedException {
            driver = InitializeDriver(driver);
        }
        @Test
        public void TestListen() throws IOException, InterruptedException {

            initialize();
            driver.manage().window().maximize();
            driver.get("https://useinsider.com/");
            driver.findElement(By.cssSelector("a#wt-cli-accept-all-btn")).click();

            // 1- doğru safyada olduğumuzu tespit
            String actaulTitle = "#1 Leader in Individualized, Cross-Channel CX — Insider";
            String resultTitle = driver.getTitle();
            Assert.assertEquals(resultTitle, actaulTitle);
            System.out.println("We are at right page");

            // 2- More seç , Careers sayfasına git , Teams , Locations ve Life at insider bölümlerinin açıldıklarını işlevsel oldklarını kontrol et
            driver.findElement(By.xpath("//*[text()='More']")).click();
            driver.findElement(By.xpath("//h5[contains(text(),'Careers')]")).click();
            WebElement seeAllTeamsButton = driver.findElement(By.cssSelector("a[class='btn btn-outline-secondary rounded text-medium mt-5 mx-auto py-3 loadmore']"));
            if (seeAllTeamsButton.isEnabled()) {
                System.out.println(" Teams part is visible");
            } else {
                System.out.println(" Teams part is not visible");
            }

            WebElement locations = driver.findElement(By.cssSelector("div[class*='d-flex justify-content-between']"));
            if (locations.isEnabled()) {
                System.out.println(" Locations block is visible");
            } else {
                System.out.println("Locations part not visible");
            }

            WebElement lifeAtInsider = driver.findElement(By.xpath("//div[@class='swiper-wrapper'][contains(@style,'transition-duration')]"));
            if (lifeAtInsider.isEnabled()) {
                System.out.println(" Life block is visible");
            } else {
                System.out.println("Life block not visible");
            }

            // 3- a-click See All Teams
            driver.findElement(By.xpath("//a[text()='See all teams']")).click();
            // b-Select Quality Assurence
            Thread.sleep(3000);
            driver.findElement(By.xpath("//h3[text()='Quality Assurance']")).click();
            // c-click See All QA Jobs
            driver.findElement(By.cssSelector("a[class*='btn btn-outline-secondary rounded text']")).click();
            // d-filter jobs by location - istanbul
            driver.findElement(By.cssSelector("span#select2-filter-by-location-container")).click();
            Thread.sleep(3000);

            List<WebElement> options = driver.findElements(By.xpath("//ul[@class='select2-results__options']/li"));
            for (WebElement option : options) {

                if (option.getText().equalsIgnoreCase("Istanbul")) {
                    option.click();
                    break;
                }
            }

            // 4- check all positions include QA , Istanbul
            Thread.sleep(5000);
            List<WebElement>positions=driver.findElements(By.cssSelector("div[class='position-list-item-wrapper bg-light']"));
            for (WebElement position : positions) {
                Assert.assertTrue(position.getText().toLowerCase(Locale.ROOT).contains("Quality Assurance".toLowerCase(Locale.ROOT)));
                Assert.assertTrue(position.getText().toLowerCase(Locale.ROOT).contains("Istanbul".toLowerCase(Locale.ROOT)));
            }

            // 5- when click Apply Now redirects Lever Application Page check

            driver.findElement(By.xpath("//a[text()='Apply Now']")).click();
            Assert.assertEquals(driver.getTitle(), "Insider Open Positions | Insider");
            System.out.println(" we were directed to Lever Application form page");


        }
    }
}