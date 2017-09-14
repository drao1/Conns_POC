/**
 * 
 */
package com.etouch.taf.tools.rally;

/**
 * @author etouch
 *
 */
public enum RallyConstants {

	DEFECT("defect"),
	USER("User"),
	ATTACHMENT("Attachment"),
	ARTIFACT("Artifact"),
	
	REFERENCE("_ref"),
	ID("FormattedID"),
	NAME("Name"),
	DESCRIPTION("Description"),
	STATE("State"),
	SCHEDULE_STATE("ScheduleState"),
	PRIORITY("Priority"),
	NOTES("Notes"),
	USER_NAME("UserName"),
	SUBSCRIPTION("Subscription"),
	USER_DISPLAY_NAME("DisplayName"),
	
	CONTENT("Content"),
	CONTENT_TYPE("ContentType"),
	ATTACHMENT_CONTENT("AttachmentContent"),
	ATTACHMENT_SIZE("Size"),
	
	IMAGE_CONTENT_TYPE("image/png"),
	
	EQUALS("="),
	
	MEDIUM_PRIORITY("Medium"),
	NORMAL_PRIORITY("Normal"),
	LOW_PRIORITY("Low"),
	
	OPEN_STATE("Open"),
	CLOSED_STATE("Closed"),
	
	DEFINED_SCHEDULE_STATE("Defined"),
	ACCEPTED_SCHEDULE_STATE("Accepted"),
	IN_PROGRESS_SCHEDULE_STATE("In-Progress"),
	
	DEFAULT_PAGE_SIZE("25"),
	MAX_FILE_SIZE("5000000"),

	TAB("\t"),
	NEW_LINE("\n"),
	HTML_BREAK("<BR>");
	
	private String value ;
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}


	/**
	 * @param name
	 * @param ordinal
	 */
	RallyConstants(String value) {
		
		this.value = value;
	}

}
