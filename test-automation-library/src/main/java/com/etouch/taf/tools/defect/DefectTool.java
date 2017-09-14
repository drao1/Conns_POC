package com.etouch.taf.tools.defect;

import java.io.File;

/**
 * @author etouch
 *
 */
public abstract class DefectTool {

	public abstract void openConnection() throws Exception;

	public abstract void closeConnection() throws Exception;
	
	public abstract String getDefectStatus();
	
	public abstract boolean isDefectLogged(String defectTitle) throws Exception;
	
	public abstract void logDefect(String defectTitle, StringBuilder defectDescription, String defectPriority, File fileAttachment) throws Exception;

	public abstract void closeDefect(StringBuilder comments) throws Exception;

	public abstract void reOpenDefect(StringBuilder comments, File fileAttachment) throws Exception;

	public abstract void updateDefect(StringBuilder comments, File fileAttachment) throws Exception;

}
