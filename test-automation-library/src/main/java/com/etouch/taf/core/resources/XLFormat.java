/**
 * 
 */
package com.etouch.taf.core.resources;

/**
 * @author etouch
 *
 */
public enum XLFormat {

	// Column identifications
	TS_START_COLUMN(0),
	TS_INDEX_COLUMN(1),
	TS_PRE_ACTION_COLUMN(2),
	TS_ELEMENT_NAME_COLUMN(3),
	TS_ELEMENT_ID_KEY_COLUMN(4),
	TS_ELEMENT_ID_VALUE_COLUMN(5),
	TS_ACTION_COLUMN(6),
	TS_TEST_DATA_COLUMN(7),
	TS_POST_ACTION_COLUMN(8),
	TS_TEST_GROUP_COLUMN(9),
	TS_TEST_RUN(10),
	TS_END_COLUMN(11);
	
	/**
	 * @param index
	 */
	private XLFormat(int index) {
		this.index = index;
	}

	int index;

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
}
