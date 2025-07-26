package tracker;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class RedbusPriceTrackerTest {
	
	@Test
	public void trackBusPrice()  throws Exception {
   		System.out.println("===== Test started =====");
   		ChromeOptions options = new ChromeOptions();

		  
		/*
		 * if (System.getenv("CI") != null) {
		 * 
		 * options.addArguments("--headless=chrome");
		 * options.addArguments("--disable-gpu"); options.addArguments("--no-sandbox");
		 * options.addArguments("--disable-dev-shm-usage");
		 * options.addArguments("--disable-blink-features=AutomationControlled");
		 * options.
		 * addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36"
		 * ); options.setExperimentalOption("excludeSwitches",
		 * Arrays.asList("enable-automation"));
		 * options.setExperimentalOption("useAutomationExtension", false);}
		 */
		  WebDriver driver = new ChromeDriver(options);
		  Actions actions = new Actions(driver);
		 
     
	  	driver.manage().window().maximize();
		String TrackerPrice = "";
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
 
    		//String link ="https://www.redbus.in/bus-tickets/bangalore-to-hyderabad?fromCityName=Bangalore&fromCityId=122&srcCountry=IND&fromCityType=CITY&toCityName=Hyderabad&toCityId=124&destCountry=India&toCityType=CITY&onward=14-Aug-2025&doj=14-Aug-2025&ref=home";

    		String link = "https://www.redbus.in/";

    		driver.get(link);
    		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-autoid='searchWidget']")));
    		// Click on the "From" field
    		WebElement fromDiv = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='srcDestWrapper___db6b0f' and .//div[text()='From']]")));
    		fromDiv.click();
    		
    		Thread.sleep(2000);
    		

    		// Type into the focused element (React traps focus to a hidden input field)
    		actions.sendKeys("Bangalore").perform();
    		Thread.sleep(2000);

    		// Wait for and click on the auto-suggestion (optional safety)
    		WebElement fromSuggestion = wait.until(ExpectedConditions.elementToBeClickable(
    		    By.xpath("//div[@aria-label=\"Search suggestions list\"]")
    		));
    		driver.findElement(By.xpath("(//div[@class=\"listHeader___40b031\"])[1]")).click();
    		// Click on the "ToDiv" field
    		WebElement toDiv = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='srcDestWrapper___db6b0f' and .//div[text()='To']]")));
    		toDiv.click();
    		
    		Thread.sleep(2000);
    		

    		// Type into the focused element (React traps focus to a hidden input field)
    		actions.sendKeys("Hyderabad").perform();
    		Thread.sleep(2000);

    		// Wait for and click on the auto-suggestion (optional safety)
    		WebElement toSuggestion = wait.until(ExpectedConditions.elementToBeClickable(
    		    By.xpath("//div[@aria-label=\"Search suggestions list\"]")
    		));
    		driver.findElement(By.xpath("(//div[@class=\"listHeader___40b031\"])[1]")).click();
    		
    	//click on calendar
    		
    		driver.findElement(By.xpath("//i[contains(@class,'icon-date_range')]")).click();
    		wait.until(
    				ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datePickerWrapper___9a8bba')]")));
    		
    		String Month =driver.findElement(By.xpath("//p[@class='monthYear___93a489']")).getText();
    		System.out.println(Month);
    		if(Month.toLowerCase().contains("july")) {
    			driver.findElement(By.xpath("//i[contains(@class,'right__')]")).click();
    			String newMonth =driver.findElement(By.xpath("//p[@class='monthYear___93a489']")).getText();
    			System.out.println(newMonth);
    		} 
    		
    		driver.findElement(By.xpath("//li//span[text()='14']")).click();
       		wait.until(
    				ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='14 Aug, 2025']")));
       		driver.findElement(By.xpath("//button[contains(@class,'searchButtonWrapper')]")).click();
       	
    		
    		
    		//div[@class="datePickerWrapper___9a8bba"]
    		File src1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    		FileUtils.copyFile(src1, new File("screenshots/debug_before_busesFoundText.png"));
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
    		  TrackerPrice= busesList.get(i).findElement(By.xpath(".//ancestor::li//p[contains(@class,'finalFare')]")).getText();
    		  
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
        } catch(Exception e) {
        	System.out.println(e);
        } finally {
        	driver.quit();
        }
		 

	}

}
