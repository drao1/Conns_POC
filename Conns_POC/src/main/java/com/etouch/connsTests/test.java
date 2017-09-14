package com.etouch.connsTests;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class test {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
/*String[][] test= ExcelUtil.readExcelData("C:\\dummy\\Conns_POC\\src\\test\\resources\\testdata\\excel\\conns_automation_data.xls", "CreditApp", "verifyFieldValidation");
		
		for(int r=0; r<test.length; r++) {
		       for(int c=0; c<test[0].length; c++)
		           System.out.print(test[r][c] + " ");
		       System.out.println();
		    }
		
		
	*/
		//@Test
		System.setProperty("webdriver.chrome.driver", "D:\\SeleniumDrivers\\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://connsecommdev-1365538477.us-east-1.elb.amazonaws.com/conns_rwd/");
	//		driver.findElement(By.xpath(".//*[@id='slide-nav']/div/div[1]/div/div[1]/div[1]/ul/li/a")).click();
			Thread.sleep(5000);
//			driver.findElement(By.xpath(".//*[@id='es']")).click();
			//Set<String> handles=driver.getWindowHandles();
		/*	List<WebElement> element = driver.findElements(By.tagName("iframe"));
		System.out.println(element.size());
		//driver.switchTo().frame(2);
		driver.switchTo().frame(driver.findElement(By.xpath(".//iframe[@id='lightbox_pop']")));
		driver.findElement(By.xpath(".//*[@id='es']")).click();
		Thread.sleep(3000);
		driver.quit();*/
			
			Actions builder = new Actions(driver);
			builder.moveToElement(driver.findElement(By.linkText("Financing & Promotions"))).perform();
		//	builder.moveToElement(driver.findElement(By.xpath("//*[@id='a-primary-computers---accessories']"))).perform();
			
			Thread.sleep(2000);
			driver.findElement(By.linkText("New Home Deals")).click();
			//builder.moveToElement(clickElement).click().perform();
		/*	driver.findElement(By.linkText("Living Room Furniture")).click();
			driver.navigate().back();
			builder.moveToElement(driver.findElement(By.linkText("Furniture & Mattresses"))).perform();
			Thread.sleep(2000);
			driver.findElement(By.linkText("Sofas & Loveseats")).click();*/
	}
}
