package com.etouch.taf.core.resources;

import com.etouch.taf.tools.rally.RallyConstantsForScripting;

/**
 * The Class DefectParameters.
 */
public class DefectParameters {


	/**
	 * The Interface IDefect.
	 */
	public interface IDefect {

		/**
		 * Gets the param type.
		 * 
		 * @return the param type
		 */
		public String getParamType();
	}

	/**
	 * The Enum RallyParams.
	 */
	public enum RallyParams implements IDefect {

		/** The Object id. */
		OBJECTID(RallyConstantsForScripting.OBJECTID),

		/** The Name. */
		NAME(RallyConstantsForScripting.NAME),

		/** The State. */
		STATE(RallyConstantsForScripting.STATE),

		/** The Priority. */
		PRIORITY(RallyConstantsForScripting.PRIORITY),

		/** The Severity. */
		SEVERITY(RallyConstantsForScripting.SEVERITY),

		/** The Owner. */
		OWNER(RallyConstantsForScripting.OWNER),

		/** The Description. */
		DESCRIPTION(RallyConstantsForScripting.DESCRIPTION),

		/** The State_ open. */
		STATE_OPEN(RallyConstantsForScripting.STATUS_OPEN);

		/** The rparam. */
		private String rparam;

		/**
		 * Instantiates a new rally params.
		 * 
		 * @param param
		 *            the param
		 */
		RallyParams(String param) {
			this.rparam = param;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.etouch.taf.core.resources.DefectParameters.IDefect#getParamType()
		 */
		public String getParamType() {
			return rparam;
		}
	}
}
