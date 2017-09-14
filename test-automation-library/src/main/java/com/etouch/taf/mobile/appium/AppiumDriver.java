package com.etouch.taf.mobile.appium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.logging.Log;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.config.TestBedConfig;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.resources.DeviceType;
import com.etouch.taf.core.resources.PlatformType;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.LogUtil;

/**
 * The Class AppiumDriver.
 */
public class AppiumDriver {

	/** The log. */
	private static Log log = LogUtil.getLog(AppiumDriver.class);

	/**
	 * Builds the driver.
	 * 
	 * @param testBed
	 *            the test bed
	 * @return the remote web driver
	 * @throws DriverException
	 *             the driver exception
	 */

	// for IOS driver
	public static IOSDriver buildIOSDriver(TestBed testBed) throws DriverException {
		IOSDriver driver = null;
		TestBedManagerConfiguration tbMgrConfig = TestBedManagerConfiguration.getInstance();

		try {
			// Added below code for setting desired capabilities for Remote iOS
			// Driver
			String currentPort = testBed.getPort();
			String Env = tbMgrConfig.getMobileConfig().getTestEnv();
			if (Env.equalsIgnoreCase("remote")) {
				System.out.println("Creating Remote Driver");

				DesiredCapabilities cap = new DesiredCapabilities();

				CommonUtil.sop(" TestBed NNNAAAAAMMMMMEEEE : " + testBed.getTestBedName());

				cap.setCapability("browserName", testBed.getBrowser().getName());
				cap.setCapability("platform", testBed.getPlatform().getName());
				cap.setCapability("device", testBed.getDevice().getName());
				cap.setCapability("version", testBed.getPlatform().getVersion());
				CommonUtil.sop("Setting Capabilities to IOSDriver: " + cap.toString());
				driver = new IOSDriver(new URL(tbMgrConfig.getMobileConfig().getHub()), cap);

			}
			// Code Updation Completed

			else {

				DesiredCapabilities cap = createCapabilities(testBed);

				CommonUtil.sop(" TestBed NNNAAAAAMMMMMEEEE : " + testBed.getTestBedName());

				/**
				 * If the testBed doesn't have a individual port, then get the
				 * common port from mobileConfig
				 */
				if (currentPort == null || currentPort.length() == 0) {
					currentPort = tbMgrConfig.getMobileConfig().getPort();
				}

				log.debug(" Current Port is ---------------- " + currentPort);

				if (testBed.getApp().getBundleId() != null || testBed.getApp().getAppPath() != null
						|| testBed.getBrowser().getName() != null) {

					if (ConfigUtil.isRemoteEnv(testBed.getTestBedName()))
						driver = new IOSDriver(new URL("http://" + tbMgrConfig.getMobileConfig().getHub() + ":"
								+ tbMgrConfig.getMobileConfig().getPort() + "/wd/hub"), cap);
					else
						driver = new IOSDriver(new URL(
								"http://" + tbMgrConfig.getMobileConfig().getHub() + ":" + currentPort + "/wd/hub"),
								cap);
				} else if (testBed.getApp().getBundleId() == null && testBed.getApp().getAppPath() == null
						&& testBed.getBrowser().getName() == null) {
					throw new DriverException(
							"Please provide application path (or) bundle Id  (or) broswer details in mobile testbed configuration: ");
				}
			}
		} catch (Exception e) {
			log.error("failed to create iOS driver : " + e.getMessage());
			if (testBed.getApp().getAppPath() == null && testBed.getApp().getBundleId() != null) {
				throw new DriverException(
						"Could not create iOS driver :: Please make sure that the application is installed in mobile device or provide the application path in mobile testbed configuration:: "
								+ e.getMessage());
			} else {
				throw new DriverException("Could not create ios driver :: " + e.getMessage());
			}
		}
		return driver;
	}

	private static boolean startServer(TestBed testBed) {
		try {
			Runtime.getRuntime().exec("/bin/bash export ANDROID_HOME=$HOME/Downloads/adt-bundle-mac-x86_64/sdk/");

		} catch (IOException e) {

			log.debug("IOException", e);
		}
		CommandLine command = new CommandLine("/");
		command.addArgument("/Users/eTouch/.nvm/versions/v0.12.2/bin");
		command.addArgument("/Users/eTouch/node_modules/appium/bin/appium.js", false);
		command.addArgument("--address", false);
		command.addArgument("127.0.0.1");
		command.addArgument("--port", false);
		command.addArgument(testBed.getPort());
		command.addArgument("-bp", false);
		int bpPort = Integer.parseInt(testBed.getPort());
		bpPort = bpPort + 2;
		command.addArgument(String.valueOf(bpPort));
		command.addArgument("-U", false);
		command.addArgument(testBed.getDevice().getUdid());
		command.addArgument("--no-reset");

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		try {
			executor.execute(command, resultHandler);
			CommonUtil.sop(resultHandler.getException().toString());
		} catch (ExecuteException e) {

			log.debug("ExecuteException", e);
		} catch (IOException e) {

			log.debug("IOException", e);
		}

		return true;

	}

	// /for android driver
	public static AndroidDriver buildAndroidDriver(TestBed testBed) throws DriverException {
		AndroidDriver driver = null;
		TestBedManagerConfiguration tbMgrConfig = TestBedManagerConfiguration.getInstance();

		try {

			// Added below code for setting desired capabilities for Remote
			// Android Driver
			String Env = tbMgrConfig.getMobileConfig().getTestEnv();
			if (Env.equalsIgnoreCase("remote")) {
				System.out.println("Creating Remote Driver");

				DesiredCapabilities cap = new DesiredCapabilities();

				CommonUtil.sop(" TestBed NNNAAAAAMMMMMEEEE : " + testBed.getTestBedName());

				cap.setCapability("browserName", testBed.getBrowser().getName());
				cap.setCapability("platform", testBed.getPlatform().getName());
				cap.setCapability("device", testBed.getDevice().getName());
				cap.setCapability("version", testBed.getPlatform().getVersion());
				CommonUtil.sop("Setting Capabilities to Android Driver :" + cap.toString());
				driver = new AndroidDriver(new URL(tbMgrConfig.getMobileConfig().getHub()), cap);

			}
			// Code Updation Completed
			else {
				CommonUtil.sop("Creating Local Android Driver");
				DesiredCapabilities cap = createCapabilities(testBed);
				CommonUtil.sop(" TestBed NNNAAAAAMMMMMEEEE : " + testBed.getTestBedName());
				String currentPort = testBed.getPort();

				if (testBed.getApp().getAppPackage() != null || testBed.getApp().getAppPath() != null
						|| testBed.getBrowser().getName() != null) {

					/**
					 * If the testBed doesn't have a individual port, then get
					 * the common port from mobileConfig
					 */
					if (currentPort == null || currentPort.length() == 0) {
						currentPort = tbMgrConfig.getMobileConfig().getPort();
					}

					if (ConfigUtil.isRemoteEnv(testBed.getTestBedName()))
						driver = new AndroidDriver(new URL("http://" + tbMgrConfig.getMobileConfig().getHub() + ":"
								+ tbMgrConfig.getMobileConfig().getPort() + "/wd/hub"), cap);
					else
						driver = new AndroidDriver(new URL(
								"http://" + tbMgrConfig.getMobileConfig().getHub() + ":" + currentPort + "/wd/hub"),
								cap);
				} else if ((testBed.getApp().getAppPackage() == null || testBed.getApp().getAppActivity() == null)
						&& testBed.getApp().getAppPath() == null && testBed.getBrowser().getName() == null) {
					throw new DriverException(
							"Please provide application path (or) package and activity details  (or) broswer details in mobile testbed configuration: ");
				}
			}
		} catch (Exception e) {
			log.error("failed to create android driver : " + e.getMessage());
			if (testBed.getApp().getAppPath() == null && testBed.getApp().getAppPackage() != null)
				throw new DriverException(
						"Could not create android driver :: Please make sure that the application is installed in mobile device or provide the application path in mobile testbed configuration:: "
								+ e.getMessage());
			else
				throw new DriverException("Could not create android driver :: " + e.getMessage());
		}
		return driver;
	}

	/**
	 * Creates the capabilities.
	 * 
	 * @param testBed
	 *            the test bed
	 * @return the desired capabilities
	 */
	private static DesiredCapabilities createCapabilities(TestBed testBed) {
		DesiredCapabilities capabilities = null;
		if (testBed.getPlatform().getName().equalsIgnoreCase(PlatformType.ANDROID.getName())) {
			capabilities = createAndroidDriver(testBed);

		} else if (testBed.getPlatform().getName().equalsIgnoreCase(PlatformType.IOS.getName())) {
			capabilities = createiOSDriver(testBed);
		}
		return capabilities;

	}

	/**
	 * Createi os driver.
	 * 
	 * @param testBed
	 *            the test bed
	 * @return the desired capabilities
	 */
	private static DesiredCapabilities createiOSDriver(TestBed testBed) {

		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability(TafCapabilityType.BUNDLE_ID, testBed.getApp().getBundleId());
		capabilities.setCapability(TafCapabilityType.BROWSER_NAME, testBed.getBrowser().getName());
		capabilities.setCapability(TafCapabilityType.VERSION, testBed.getBrowser().getVersion());
		capabilities.setCapability(TafCapabilityType.PLATFORM_NAME, testBed.getPlatform().getName());
		capabilities.setCapability("U", "auto");
		if (testBed.getApp().getAppPath() != null) {
			capabilities.setCapability(TafCapabilityType.APP, testBed.getApp().getAppPath());
		}

		if ((testBed.getDevice().getType() != null)
				&& (testBed.getDevice().getType().equalsIgnoreCase(DeviceType.SIMULATOR.toString()))) {
			updateiOSSimulator(testBed, capabilities);
		} else {
			updateiOSDevice(testBed, capabilities);

		}
		return capabilities;

	}

	/**
	 * Updatei os device.
	 * 
	 * @param testBed
	 *            the test bed
	 * @param capabilities
	 *            the capabilities
	 */
	private static void updateiOSDevice(TestBed testBed, DesiredCapabilities capabilities) {
		capabilities.setCapability(TafCapabilityType.UDID, testBed.getDevice().getUdid());
		capabilities.setCapability(TafCapabilityType.DEVICE_NAME, testBed.getDevice().getName());
	}

	/**
	 * Updatei os simulator.
	 * 
	 * @param testBed
	 *            the test bed
	 * @param capabilities
	 *            the capabilities
	 */
	private static void updateiOSSimulator(TestBed testBed, DesiredCapabilities capabilities) {
		capabilities.setCapability(TafCapabilityType.DEVICE_NAME, testBed.getDevice().getName());

	}

	/**
	 * Creates the android driver.
	 * 
	 * @param testBed
	 *            the test bed
	 * @return the desired capabilities
	 */
	private static DesiredCapabilities createAndroidDriver(TestBed testBed) {

		DesiredCapabilities capabilities = new DesiredCapabilities();

		// Added for Android web testing
		capabilities.setCapability(TafCapabilityType.BROWSER_NAME, testBed.getBrowser().getName());

		// We have to set the Android or IOS version for platform version.
		// Browser version doesn't matter.
		// capabilities.setCapability(TafCapabilityType.VERSION,
		capabilities.setCapability(TafCapabilityType.VERSION, testBed.getPlatform().getVersion());
		capabilities.setCapability(TafCapabilityType.ACCEPT_SSL_CERTS, true);

		capabilities.setCapability(TafCapabilityType.PLATFORM_NAME, testBed.getPlatform().getName());
		capabilities.setCapability(TafCapabilityType.APP, testBed.getApp().getAppPath());
		capabilities.setCapability(TafCapabilityType.APP_PACKAGE, testBed.getApp().getAppPackage());
		capabilities.setCapability(TafCapabilityType.APP_ACTIVITY, testBed.getApp().getAppActivity());
		if (testBed.getDevice().getType().equalsIgnoreCase(DeviceType.EMULATOR.toString())) {
			updateAndroidEmulator(testBed, capabilities);
		} else {
			updateAndroidDevice(testBed, capabilities);
		}

		return capabilities;

	}

	/**
	 * Update android device.
	 * 
	 * @param testBed
	 *            the test bed
	 * @param capabilities
	 *            the capabilities
	 */
	private static void updateAndroidDevice(TestBed testBed, DesiredCapabilities capabilities) {
		capabilities.setCapability(TafCapabilityType.DEVICE_NAME, testBed.getDevice().getName());
		capabilities.setCapability(TafCapabilityType.UDID, testBed.getDevice().getUdid());

	}

	/**
	 * Update android emulator.
	 * 
	 * @param testBed
	 *            the test bed
	 * @param capabilities
	 *            the capabilities
	 */
	private static void updateAndroidEmulator(TestBed testBed, DesiredCapabilities capabilities) {
		capabilities.setCapability(TafCapabilityType.DEVICE_NAME, testBed.getDevice().getName());
		// have to find out a way how to prioritize the given device
		capabilities.setCapability(TafCapabilityType.DEVICE_ID, testBed.getDevice().getId());

	}

	private static TestBed loadTestBedDetails(String testBedName) {

		TestBed currentTestBed = null;
		TestBedManagerConfiguration testBedMgrConfig = TestBedManagerConfiguration.getInstance();
		if (ConfigUtil.isWebTestTypeEnabled()) {
			for (TestBedConfig tbConfig : testBedMgrConfig.getWebConfig().getTestBeds()) {

				CommonUtil.sop(
						" Current TestBedName " + testBedName + "tbConfig TestBedName " + tbConfig.getTestBedName());
				if (tbConfig.getTestBedName().equalsIgnoreCase(testBedName)) {
					currentTestBed = copyTestBedDetails(tbConfig);
					break;
				}
			}
		}
		if (ConfigUtil.isMobileTestTypeEnabled()) {
			if (currentTestBed == null) {
				for (TestBedConfig tbConfig : testBedMgrConfig.getMobileConfig().getTestBeds()) {
					if (tbConfig.getTestBedName().equalsIgnoreCase(testBedName)) {
						currentTestBed = copyTestBedDetails(tbConfig);
						break;
					}
				}
			}
		}
		return currentTestBed;
	}

	/**
	 * Copy test bed details.
	 * 
	 * @param testBedConfig
	 *            the test bed config
	 * @return the test bed
	 */
	private static TestBed copyTestBedDetails(TestBedConfig testBedConfig) {
		TestBed currentTestBed = new TestBed();
		if (testBedConfig != null) {

			currentTestBed.setTestBedName(testBedConfig.getTestBedName());

			currentTestBed.setPlatform(testBedConfig.getPlatform());
			currentTestBed.setBrowser(testBedConfig.getBrowser());
			currentTestBed.setApp(testBedConfig.getApp());
			currentTestBed.setDevice(testBedConfig.getDevice());
			currentTestBed.setTestBedName(testBedConfig.getTestBedName());
			currentTestBed.setTestbedClassName(testBedConfig.getTestbedClassName());
			currentTestBed.setPort(testBedConfig.getPort());

		}

		return currentTestBed;
	}
}
