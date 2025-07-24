package tracker;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class RedbusPriceTrackerTest {
	
	@Test
	public void trackBusPrice()  throws Exception {
		System.out.println("===== Test started =====");
		ChromeOptions options = new ChromeOptions();
		//options.addArguments("--start-maximized");
		Path tempProfile = Files.createTempDirectory("chrome-profile");
		options.addArguments("--user-data-dir=" + tempProfile.toAbsolutePath().toString());

		if (System.getenv("CI") != null) {
		    options.addArguments("--headless=new");
		    options.addArguments("--disable-gpu");
		    options.addArguments("--window-size=1920,1080");
		    options.addArguments("--no-sandbox");
		    options.addArguments("--disable-dev-shm-usage");
		}
		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		String TrackerPrice = "";
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		

		driver.get(
				"https://www.redbus.in/bus-tickets/bangalore-to-hyderabad?fromCityName=Bangalore&fromCityId=122&srcCountry=IND&fromCityType=CITY&toCityName=Hyderabad&toCityId=124&destCountry=India&toCityType=CITY&onward=14-Aug-2025&doj=14-Aug-2025&ref=home");
		wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'busesFoundText')]")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-autoid='filters-desktop']")));

		WebElement filterContainer = driver.findElement(By.xpath("//div[@data-autoid='filters-desktop']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop = arguments[0].scrollTop + 200;",
				filterContainer);

		driver.findElement(By.xpath("//div[text()='Bus operator']")).click();
		WebElement busoperator = driver.findElement(By.xpath("//input[@placeholder='Search bus operator']"));
		if (busoperator.isEnabled()) {
			busoperator.sendKeys("Jabbar");
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Jabbar  Travels']")));

		driver.findElement(By.xpath("//div[@data-autoid=\"busOperator\"]//label[@for=\"checkbox\"]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'busTitleWrap_')]")));

		List<WebElement> busesList = driver.findElements(By.xpath("//div[contains(@class,'busTitleWrap_')]"));
		WebElement busOperatorName = busesList.get(0).findElement(By.xpath("//div[contains(@class,'travelsName___')]"));
		String text = busOperatorName.getText().toLowerCase();
		if (text.contains("jabbar")) {
			System.out.println("Bus Found");
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

		// Get current time
		String currentTime = LocalDateTime.now().format(formatter);

		// Send with your message
		TelegramNotifier.sendTextMessage("📅 Sending data for: " + currentTime);
		  for(int i=0;i<busesList.size();i++) { 
			  String busType=  busesList.get(i).findElement(By.xpath(".//p[contains(@class, 'busType__')]")).getText();
			  
		 if(busType.equalsIgnoreCase("Scania AC Multi Axle Sleeper (2+1)")) {
		  TrackerPrice= busesList.get(i).findElement(By.xpath(".//ancestor::li//p[@class='finalFare___a41cf4']")).getText();
		  
		  busesList.get(i).findElement(By.xpath(".//ancestor::li//button[contains(@class,'viewSeat')]")).click();
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-autoid='busDetailsContainer']")));
		  
          // Screenshot
          File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        
          File dest = new File("screenshots/redbus_price_result" + i + ".png");
          FileUtils.copyFile(src, dest);

          System.out.println("Screenshot saved to /screenshots");
          driver.findElement(By.xpath("//div[@class=\"header__ind-seat-styles-module-scss-4q84B\"]//button")).click();
        //div[@class="header__ind-seat-styles-module-scss-4q84B"]//button
		
          // Send message
          TelegramNotifier.sendTextMessage("Bus Price is :"+TrackerPrice);

          // Send screenshot
          TelegramNotifier.sendPhoto(dest);
          Thread.sleep(1000);

          System.out.println("Price and screenshot sent to Telegram!");

		  
		
		 }
		  
		  }
		  System.out.println("===== Test Ended =====");
		  TelegramNotifier.sendTextMessage("------------------------");

	}

}
