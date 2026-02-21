package id.ac.ui.cs.advprog.eshop.functional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CreateProductFunctionalTest {
    /**
     * The port number assigned to the running application during test execution.
     * Set automatically during each test run by Spring Framework's test context.
     */
    @LocalServerPort
    private int serverPort;

    /**
     * The base URL for testing. Default to {@code http://localhost}.
     */
    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void testCreateProduct(ChromeDriver driver) {
        driver.get(baseUrl + "/product/create");

        driver.findElement(By.id("name")).sendKeys("ABC");
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String currentUrl = driver.getCurrentUrl();
        assertEquals(baseUrl + "/product/list", currentUrl);
        WebElement product = driver.findElement(By.xpath("//td[text()='ABC']"));
        assertEquals("ABC", product.getText());
        assertEquals("1", driver.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText());
    }



    @Test
    void testCreateProductMultiple(ChromeDriver driver) {
        driver.get(baseUrl + "/product/create");

        driver.findElement(By.id("name")).sendKeys("ABC");
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.get(baseUrl + "/product/create");

        driver.findElement(By.id("name")).sendKeys("EFG");
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("2");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String currentUrl = driver.getCurrentUrl();
        assertEquals(baseUrl + "/product/list", currentUrl);
        assertEquals("ABC", driver.findElement(By.xpath("//tbody/tr[1]/td[1]")).getText());
        assertEquals("1", driver.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText());
        assertEquals("EFG", driver.findElement(By.xpath("//tbody/tr[2]/td[1]")).getText());
        assertEquals("2", driver.findElement(By.xpath("//tbody/tr[2]/td[2]")).getText());
    }
}
