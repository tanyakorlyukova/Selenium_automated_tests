import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import static java.util.concurrent.TimeUnit.SECONDS;

public class SeleniumTestClass {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver",
                "drivers\\chromedriver.exe");
        //запуск и настройка окна браузера
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        //добавление ожиданий
        driver.manage().timeouts().implicitlyWait(5, SECONDS);
        //открытие сайта Selenium.dev
        driver.get("https://selenium.dev/");

        //ввод и поиск "selenium documentation"
        By searchInput = By.id("gsc-i-id1");
        driver.findElement(searchInput).sendKeys("selenium documentation" + Keys.ENTER);
        System.out.println("Page title is: " + driver.getTitle());

        //получение ссылок поиска и переход по первой
        By pageTitles = By.cssSelector("a[class='gs-title']");
        List<WebElement> titles = driver.findElements(pageTitles);
        titles.get(0).click();

        //переключаемся на второе окно
        String parentHandle = driver.getWindowHandle();
        for(String childHandle : driver.getWindowHandles()){
            if (!childHandle.equals(parentHandle)){
                driver.switchTo().window(childHandle);
            }
        }
        System.out.println("Page title is: " + driver.getTitle());

        //переходим по страницам, используя исонку Next
        By nextIcon = By.cssSelector("i[class='fa fa-chevron-right']");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        for(int i=0; i<5; i++) {
            driver.findElement(nextIcon).click();
            System.out.println("Opened page is: " + openedPage(driver));

            //убеждаемся, что галочка на последней загруженой странице отобразилась
            By lastVisibleIcon = By.cssSelector("li.dd-item.active.visited > a > i");
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(lastVisibleIcon)));
        }

        //считаем, сколько страниц было посещено, в методе countOfVisitedPage() ниже
        int visitedPages = countOfVisitedPages(driver);
        System.out.println("Count of visited pages is: " + visitedPages);

        //очищаем истоию
        By clearHistoryButton = By.cssSelector("a > i[class='fas fa-history fa-fw']");
        driver.findElement(clearHistoryButton).click();

        //проверяем ещё раз, сколько страниц было посещено
        int visitedPages2 = countOfVisitedPages(driver);
        System.out.println("Count of visited pages after history was cleared is: " + visitedPages);

        //закрытие браузера
        driver.quit();
    }

    public static int countOfVisitedPages(WebDriver driver) {
        By icons = By.xpath("//section[@id='prefooter']//li[2]");
        List<WebElement> iconsVisited = driver.findElements(icons);
        int visitedPagesCount = 0;
        for(int i = 0; i < iconsVisited.size(); i++) {
            if(iconsVisited.get(i).isDisplayed())
                visitedPagesCount++;
        }
        return visitedPagesCount;
    }

    public static String openedPage(WebDriver driver) {
        By heading = By.tagName("h1");
        return driver.findElement(heading).getText();
    }
}
