package com.bm.tests.web;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;


public class TelusTV {

	static WebDriverWait wait;
	static WebDriver driver;
	static String flag;
	public static void main(String args[]) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "\\src\\main\\resources\\drivers\\chromedriver.exe");

		// Instantiate a ChromeDriver
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 10);

		// Maximize the browser
		driver.manage().window().maximize();

		// Launch the Telus tv URL
		driver.get("https://telustvplus.com/#/");
		
		// Implemented pageload timeout
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		//wait till cross icon clickable
		TelusTV.explictWaitTillClickable("//img[@src='/images/cross-icon.svg']");

		// Getting the X element of pop up and click on it
		WebElement crossIcon = driver.findElement(By.xpath("//img[@src='/images/cross-icon.svg']"));
		crossIcon.click();
		
		// Getting the onDemand element and click on it
		WebElement onDemand = driver.findElement(By.id("2"));
		onDemand.click();
		
		// Scroll to Movies and click on it
		WebElement movieTitle = driver.findElement(By.xpath("//span[text()='Movies']"));		
		((JavascriptExecutor) driver).executeScript("arguments[0]. scrollIntoView(true);", movieTitle);
		movieTitle.click();
		
		// Getting the filter element and click on it
		TelusTV.explictWaitTillClickable("//div[text()='Filter']");
		WebElement filter = driver.findElement(By.xpath("//div[text()='Filter']"));
		filter.click();
		
		// Getting the animated element and click on it
		TelusTV.explictWaitTillClickable("//span[text()='Animated']");
		WebElement animated = driver.findElement(By.xpath("//span[text()='Animated']"));
		animated.click();
		
		// Getting the apply element and click on it
		TelusTV.explictWaitTillClickable("//div[text()='Apply']");
		WebElement apply = driver.findElement(By.xpath("//div[text()='Apply']"));
		Thread.sleep(3000);
		apply.click();
		
		//wait till cross icon clickable
		TelusTV.explictWaitTillClickable("//div[@class='row']//div[@class='column'][1]");
		
		// Getting the list of movies		
		List<WebElement> movieList = driver.findElements(By.xpath("//div[@class='row']//div[@class='column']"));
		
		//Looping through the list and find an asset with rating 18+
		for(int movieNumber = 1; movieNumber<=movieList.size(); movieNumber++) {
			
			// Getting the subtitle
			String updatedLocator = "//div[@class='row']//div[@class='column']["+movieNumber+"]//p[@class='subtitle']";
			TelusTV.explictWaitTillPresence(updatedLocator);
			String subtitle = driver.findElement(By.xpath(updatedLocator)).getText();
			System.out.println("check the subtitle of "+movieNumber+" movie");
			if(subtitle.equalsIgnoreCase("18+")) {
				
				// Getting the movie with 18+ subtitle and click on it 
				WebElement movie18 = driver.findElement(By.xpath("//div[@class='row']//div[@class='column']["+movieNumber+"]"));
				movie18.click();
				
				//wait till metadata presence
				TelusTV.explictWaitTillClickable("//span[@class='no-scores-line-metadata']");		
				((JavascriptExecutor) driver).executeScript("window.scrollBy(0,250)","");
				flag = "true";
				
				// Getting the value of metedata element
				String metadata = driver.findElement(By.xpath("//span[@class='no-scores-line-metadata']")).getText();
				
				//Validating Expected Results
				Assert.assertEquals(metadata.contains("18+"), true,"metadata do not contain 18+");
				break;
			}
		}
		if(flag!="true") {
			System.out.println("No movie available for 18+)");
		}
	}

	public static void explictWaitTillPresence(String locator) {

		// wait condition till element located
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
	}
	public static void explictWaitTillClickable(String locator) {	

		// wait condition till element Clickable
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
	}
}
