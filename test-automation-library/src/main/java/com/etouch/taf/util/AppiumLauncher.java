package com.etouch.taf.util;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.logging.Log;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.config.TestBedManagerConfiguration;

public class AppiumLauncher {

	private static String OS = System.getProperty("os.name").toLowerCase();
	
	private static Log log = LogUtil.getLog(AppiumLauncher.class);
	static String AppiumnodePath,AppiumJsPath;
	
	
	public static void main(String args[]) {
		log.debug(getBootstrapPort("4723"));
		
	}

	public static boolean launchAppiumSession(TestBed testBed) {
		
		try {
			CommandLine command = null;

			if (isWindows()) {
				
				log.debug("In Appium Command - checking command***********" +testBed);
				command = commandForWindows(testBed);
				
				log.debug("In Appium Command - checking command***********" +command);
			} else if (isMac() || isUnix() || isSolaris()) {
				command = commandForMac(testBed);
			}

			if (command != null) {
				executeCommand(command);
				log.debug("In Appium Command - checking command***********" +command);
			}

		} catch (Exception ex) {
			log.debug(ex);
		}

		return true;
	}

	public static boolean closeAppiumSession(TestBed testBed) {
		CommonUtil.sop("Inside close Appium Session");
		String portNumber = testBed.getPort();

		try {
			File batchFile_windows = new File("C://awsslave//workspace//Mobile_POC//test-automation-library//resources");

			if (isWindows()) {
				executeCommand("cmd /c " + batchFile_windows.getAbsolutePath() + "\\" + "killport_windows.bat" + " " + portNumber);
			
			} else if (isMac() || isUnix() || isSolaris()) {
				executeCommand("sh " + "..//test-automation-library//resources//killport_mac" + " " + portNumber);
			}

		} catch (Exception ex) {
			log.debug(ex);
		}

		return true;
	}

	private static void executeCommand(String command) {
		String s = null;

		try {
			Process p = Runtime.getRuntime().exec(command);
			log.debug("Inside executeCommand****************"+command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			log.debug("Inside executeCommand  Input****************"+stdInput);
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			log.debug("Inside executeCommand error****************"+stdError);
			// read the output from the command
			log.debug("Standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				log.debug(s);
			}

			// read any errors from the attempted command
			log.debug("Standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				log.debug(s);
			}
		} catch (IOException e) {
			log.debug("exception happened: ");
			log.debug("IOException", e);
		}
	}

	private static void executeCommand(CommandLine command) {
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		try {

			executor.execute(command, resultHandler);
			Thread.sleep(2000);

		} catch (InterruptedException e) {

			log.debug("InterruptedException", e);

		} catch (ExecuteException e) {

			log.debug("ExecuteException", e);

		} catch (IOException e) {

			log.debug("IOException", e);
		}

	}

	private static void testAndroidDriver() throws MalformedURLException, InterruptedException {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("browserName", "Chrome");
		capabilities.setCapability("version", "43.0.2357.93");

		capabilities.setCapability("appium-version", "1.4.1");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("deviceName", "HT45FWM07753");

		AppiumDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4733/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		Thread.sleep(3000);

		driver.get("http://www.amajon.com");

	}

	private static void testIOSDriver() throws MalformedURLException, InterruptedException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("appium-version", "1.3.4");
		capabilities.setCapability("platformName", "iOS");
		capabilities.setCapability("platformVersion", "7.0.4");

		capabilities.setCapability("deviceName", "iPad");
		capabilities.setCapability("bundleId", "com.example.apple-samplecode.Recipes");

		AppiumDriver driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		Thread.sleep(3000);
	}
	
	
	private static CommandLine commandForWindows(TestBed testBed) {
		
	
		CommandLine command = new CommandLine("cmd");
		  command.addArgument("/c");
		  command.addArgument(configurePathWithQuotes(TestBedManagerConfiguration.INSTANCE.getMobileConfig().getAppiumConfig().getNodePath())+
				  " "+configurePathWithQuotes(TestBedManagerConfiguration.INSTANCE.getMobileConfig().getAppiumConfig().getAppiumJSPath()),false);
		  command.addArgument("--address", false);
		  command.addArgument("127.0.0.1");
		  command.addArgument("--port", false);
		  command.addArgument(testBed.getPort());
		  command.addArgument("-bp", false);
		  command.addArgument(getBootstrapPort(testBed.getPort()));
		  command.addArgument("-U", false);
		  command.addArgument(testBed.getDevice().getUdid());
		  command.addArgument("--no-reset", false);
		  return command;
	}
	
	
	
	/**
	 * @author Sarvesh
	 * @param Path
	 * @return true
	 * Finds blank spaces between file name in file path and appends double quotes to the start and end of the file name 
	 */
	private static String configurePathWithQuotes(String Path)
	{
		int count=0;
		String RequiredPath="";
		int index = Path.indexOf("/");
		while (index >= 0) {
		    index = Path.indexOf("/", index + 1);
		    count++;
		}
		
		String[] DirectoryNames=new String[count];
		DirectoryNames=Path.split("/");
		
		for(int i=0;i<=count;i++)
		{
			
			if(DirectoryNames[i].contains(" "))
			{
				DirectoryNames[i]="\""+DirectoryNames[i]+"\"";
			}
			if(i>0)
			{			
				DirectoryNames[i]="/"+DirectoryNames[i];
			}
			
		}
		for(int i=0;i<=count;i++)
		{
			
			RequiredPath=RequiredPath+DirectoryNames[i];
			
		}
		return RequiredPath;
	}

	private static String killWindowsCommand(String portNumber) {

		String windowsKillCommand = "cmd /c echo off & FOR /F \"usebackq tokens=5\" %a in (`netstat -nao ^| findstr /R /C:\"" + portNumber
				+ "\"`) do (FOR /F \"usebackq\" %b in (`TASKLIST /FI \"PID eq %a\" ^| findstr /I node.exe`) do taskkill /F /PID %a)";
		log.debug("Kill command ******" +windowsKillCommand);
		return windowsKillCommand;
	}

	private static String killMacCommand(String portNumber) {

		String macKillCommand = "sh" + " " + "..//test-automation-library//resources//killport_mac" + " " + portNumber;
		return macKillCommand;
	}

	private static CommandLine commandForMac(TestBed testBed) {

		CommandLine command = new CommandLine(TestBedManagerConfiguration.INSTANCE.getMobileConfig().getAppiumConfig().getMacNodePath());
		command.addArgument(TestBedManagerConfiguration.INSTANCE.getMobileConfig().getAppiumConfig().getMacAppiumJSPath());

		command.addArgument("--address", false);
		command.addArgument("127.0.0.1");
		command.addArgument("--port", false);
		command.addArgument(testBed.getPort());
		command.addArgument("-bp", false);
		command.addArgument(getBootstrapPort(testBed.getPort()));
		command.addArgument("-U", false);
		command.addArgument(testBed.getDevice().getUdid());
		command.addArgument("--no-reset", false);

		return command;
	}

	private static String getBootstrapPort(String port) {

		Integer bpPort = Integer.valueOf(port) + 2;
		return bpPort.toString();

	}

	public static boolean isWindows() {

		return OS.indexOf("win") >= 0;

	}

	public static boolean isMac() {

		return OS.indexOf("mac") >= 0;

	}

	public static boolean isUnix() {

		return OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0;

	}

	public static boolean isSolaris() {

		return OS.indexOf("sunos") >= 0;

	}

}
