package com.etouch.taf.util;


import org.apache.commons.logging.Log;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


/**
 * The Class TestGroup.
 */
public class TestGroup {


	/** The log. */
	Log log = LogUtil.getLog(TestGroup.class);

	/**
	 * Valid authentication test.
	 */
	@Test (groups =  "Sniff")
    public void validAuthenticationTest(){
        log.debug("1 Sniff");
    }

    /**
     * Failed authentication test.
     */
    @Test (groups =  "Regression" )
    public void failedAuthenticationTest(){
        log.debug("2 Regression");
    }

    /**
     * New user authentication test.
     *
     * @param environment the environment
     */
    @Parameters("environment")
    @Test (groups = { "Sniff"})
    public void newUserAuthenticationTest(String environment){
        log.debug("1 test");
    }
    
    
    

}

