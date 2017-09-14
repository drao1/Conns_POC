package com.etouch.taf.tools.defect;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.exception.DefectException;
import com.etouch.taf.core.resources.DefectToolType;
import com.etouch.taf.tools.jira.JiraConstantsForScripting;
import com.etouch.taf.tools.jira.JiraForScripting;
import com.etouch.taf.tools.jira.JiraInfo;
import com.etouch.taf.tools.rally.RallyConstantsForScripting;
import com.etouch.taf.tools.rally.RallyForScripting;
import com.etouch.taf.tools.rally.RallyInfo;
import com.etouch.taf.util.LogUtil;

/**
 * A factory for creating DefectManager objects.
 */
public class DefectManagerFactory {

	/** The log. */
	static Log log = LogUtil.getLog(DefectManagerFactory.class);

	/**
	 * Manage defect.
	 * 
	 * @param defect
	 *            the defect
	 * @return the i defect manager
	 * @throws DefectException
	 *             the defect exception
	 */
	public static IDefectManager manageDefect(String defect) throws DefectException {

		if (defect.equalsIgnoreCase(DefectToolType.RALLY.getToolName())) {
			RallyInfo rInfo = new RallyInfo(RallyConstantsForScripting.RALLYURL, RallyConstantsForScripting.RALLYUSERNAME, RallyConstantsForScripting.RALLYPASSWD,
					RallyConstantsForScripting.RALLYAPP);
			try {
				return new RallyForScripting(rInfo);
			} catch (DefectException e) {
				log.error(e.getMessage());
				throw new DefectException(e.getMessage());
			}
		} else if (defect.equals(DefectToolType.JIRA.getToolName())) {
			JiraInfo jInfo = new JiraInfo(JiraConstantsForScripting.CREATE_ISSUE_URL, JiraConstantsForScripting.USERNAME, JiraConstantsForScripting.PASSWORD);
			try {
				return new JiraForScripting(jInfo);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			return null;
		}
		return null;
	}

}
