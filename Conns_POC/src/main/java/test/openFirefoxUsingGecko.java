package test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;

public class openFirefoxUsingGecko {

 public static void main(String[] args) throws InterruptedException {
  
 //@Test
// public static void main1() throws InterruptedException {
  // TODO Auto-generated method stub
   System.setProperty("webdriver.gecko.driver","D:\\SeleniumDrivers\\geckodriver.exe");
  // System.setProperty("webdriver.chrome.driver","D:\\SeleniumDrivers\\chromedriver.exe");
  // System.setProperty("webdriver.ie.driver","D:\\SeleniumDrivers\\IEDriverServer.exe");
   // if above property is not working or not opening the application in browser then try below property

        //System.setProperty("webdriver.firefox.marionette","G:\\Selenium\\Firefox driver\\geckodriver.exe");

      WebDriver driver = new FirefoxDriver();
    //   WebDriver driver = new InternetExplorerDriver();
       
       driver.get("http://newtours.demoaut.com/mercurywelcome.php");
       Thread.sleep(3000);
       driver.findElement(By.linkText("REGISTER")).click();
       Thread.sleep(3000);
       driver.findElement(By.name("email")).sendKeys("123456");
       driver.findElement(By.name("password")).sendKeys("123456");
       driver.findElement(By.name("confirmPassword")).sendKeys("123456");
       driver.findElement(By.name("register")).click();
       Thread.sleep(3000);
       driver.findElement(By.linkText("Flights")).click();
       Thread.sleep(3000);
       Select li = new Select(driver.findElement(By.name("fromPort")));
       li.selectByValue("Frankfurt");
       System.out.println("Application title is ============="+driver.getTitle());
       Thread.sleep(30000);
       driver.quit();
 }

}