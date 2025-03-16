import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Set;

public class AmazonPriceTracker {
    public static void main(String[] args) {
        // Set up WebDriver
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            driver.get("https://www.amazon.in");

            // Search for a product
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.sendKeys("Redmi");
            WebElement searchButton = driver.findElement(By.id("nav-search-submit-button"));
            searchButton.click();

            // Click on the first product result
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[contains(@class, 'a-size-medium')])[1]")));
            firstProduct.click();

            // Handle tab switching
            String originalTab = driver.getWindowHandle();
            Set<String> allTabs = driver.getWindowHandles();
            for (String tab : allTabs) {
                if (!tab.equals(originalTab)) {
                    driver.switchTo().window(tab);
                    break;
                }
            }

            // Wait for the price element to be visible
            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//span[@class='a-price-whole'])[1]")));

            String priceText = priceElement.getText().replace(",", "").trim(); // Remove commas
            double currentPrice = Double.parseDouble(priceText);

            System.out.println("Current Price: Rs " + currentPrice);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
