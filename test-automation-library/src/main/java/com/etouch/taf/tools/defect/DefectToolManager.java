package com.etouch.taf.tools.defect;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.etouch.taf.core.TestActionExecutor;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.resources.DefectToolType;
import com.etouch.taf.core.resources.KDConstants;
import com.etouch.taf.tools.rally.VideoRecorder;
import com.etouch.taf.util.LogUtil;

/**
 * @author etouch
 * 
 */
public class DefectToolManager {

	private static Log log = LogUtil.getLog(DefectToolManager.class);

	private DefectTool defectTool;

	private DefectBuilder defectBuilder;

	private VideoRecorder videoRecorder;

	private WebDriver webDriver;

	private File screenshot = null;

	private Throwable exception;

	public DefectToolManager(TestActionExecutor testActionExecutor) throws Exception {

		this.defectTool = getDefectTool();
		this.videoRecorder = new VideoRecorder(getScreenRecordingFileName(testActionExecutor), TestBedManagerConfiguration.INSTANCE.getVideoConfig().getVideoPath());
		this.defectBuilder = new DefectBuilder(testActionExecutor);
		this.webDriver = testActionExecutor.getWebPage().getDriver();

	}

	/**
	 * This getter for defectTool.
	 * 
	 * @return
	 * @throws Exception
	 */
	public DefectTool getDefectTool() throws Exception {

		String defectToolName = TestBedManagerConfiguration.INSTANCE.getDefectManagementTool();

		DefectTool defectToolValue = null;

		if (DefectToolType.isSupported(defectToolName)) {
			defectToolValue = DefectToolType.getDefectTool(defectToolName);
		} else {
			throw new Exception("Defect Tool not supported");
		}

		return defectToolValue;
	}

	/**
	 * This method is used to get the Screen Recording Filename.
	 * 
	 * @param testActionExecutor
	 * @return
	 */
	private String getScreenRecordingFileName(TestActionExecutor testActionExecutor) {
		String screenRecorderFileName = testActionExecutor.getTestBedName() + "-" + testActionExecutor.getTestSuiteName() + "-"
				+ testActionExecutor.getTestAction().getActionName() + "_ScreenRecording";
		return screenRecorderFileName;
	}

	public void startRecording() {
		log.debug("Starting video recording...");
		videoRecorder.startRecording();
	}

	public void stopRecording() {
		log.debug("Stopping video recording!");
		videoRecorder.stopRecording();
	}

	public void manageDefect(String methodExecutionStatus) throws Exception {

		// Opening connection
		defectTool.openConnection();

		boolean isDefectLogged = defectTool.isDefectLogged(defectBuilder.getDefectTitle());

		if (methodExecutionStatus.equalsIgnoreCase(KDConstants.EXECUTION_FAILED.getValue())) {
			manageFailedDefects(isDefectLogged);
		} else if (methodExecutionStatus.equalsIgnoreCase(KDConstants.EXECUTION_PASSED.getValue())) {
			managePassedDefects(isDefectLogged);
		}

		// Closing connection
		defectTool.closeConnection();

	}

	/**
	 * @param isDefectLogged
	 * @throws Exception
	 */
	private void managePassedDefects(boolean isDefectLogged) throws Exception {

		// Verify if the Defect is already logged
		if (isDefectLogged) {
			closeResolvedDefect();
		}
	}

	/**
	 * @throws Exception
	 */
	private void closeResolvedDefect() throws Exception {

		String defectStatus = defectTool.getDefectStatus();

		if (!defectStatus.equalsIgnoreCase(KDConstants.CLOSE_DEFECT.getValue())) {
			// Close the defect
			defectBuilder.buildDefectNotes(KDConstants.CLOSE_DEFECT.getValue(), null);
			defectTool.closeDefect(defectBuilder.getDefectNotes());
		}
	}

	/**
	 * @param isDefectLogged
	 * @throws Exception
	 */
	private void manageFailedDefects(boolean isDefectLogged) throws Exception {

		// Verify if the Defect is already logged
		if (isDefectLogged) {
			updateExistingDefect();
		} else {
			createNewDefect();
		}
	}

	/**
	 * @throws Exception
	 * 
	 */
	private void createNewDefect() throws Exception {
		// Log a new defect
		defectBuilder.buildDefect(getException());
		defectTool.logDefect(defectBuilder.getDefectTitle(), defectBuilder.getDefectDescription(), defectBuilder.getDefectPriority(), getScreenshot());
	}

	/**
	 * @throws Exception
	 */
	private void updateExistingDefect() throws Exception {

		String defectStatus = defectTool.getDefectStatus();

		if (defectStatus.equalsIgnoreCase(KDConstants.CLOSE_DEFECT.getValue())) {
			// reOpen the defect
			defectBuilder.buildDefectNotes(KDConstants.REOPEN_DEFECT.getValue(), getException());
			defectTool.reOpenDefect(defectBuilder.getDefectNotes(), getScreenshot());
		} else {
			// just update the defect with comments
			defectBuilder.buildDefectNotes(KDConstants.UPDATE_DEFECT.getValue(), getException());
			defectTool.updateDefect(defectBuilder.getDefectNotes(), getScreenshot());
		}
	}

	/**
	 * @return the exception
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * @param exception
	 *            the exception to set
	 */
	public void setException(Throwable exception) {
		this.exception = exception;
	}

	private boolean deleteRecording() {

		File videoFileCreated = videoRecorder.getCreatedVideoFile();

		boolean isFileDeleted = false;

		if (videoFileCreated != null && videoFileCreated.exists()) {
			log.debug("Deleting video file ==>" + videoFileCreated.getName());
			isFileDeleted = videoRecorder.deleteRecording(videoFileCreated);
		} else {
			isFileDeleted = true;
		}

		return isFileDeleted;

	}

	private File getScreenshot() throws WebDriverException, IOException {

		String screenshotPath = TestBedManagerConfiguration.INSTANCE.getVideoConfig().getBaseScreenshotPath();

		DateFormat dataFormat = new SimpleDateFormat("EEE_MMM_dd_HH-mm-ss_z");

		return videoRecorder.takeScreenshot(screenshotPath, "Error_" + dataFormat.format(new Date()), webDriver);
	}

	public boolean cleanTempFiles() {

		boolean isFileDeleted = false;

		if (deleteScreenshot() && deleteRecording()) {
			isFileDeleted = true;
		} else {
			isFileDeleted = false;
		}

		return isFileDeleted;

	}

	private boolean deleteScreenshot() {

		boolean isFileDeleted = false;

		if (screenshot != null && screenshot.exists()) {
			isFileDeleted = videoRecorder.deleteScreenshot(screenshot);
		} else {
			isFileDeleted = true;
		}

		return isFileDeleted;
	}
}