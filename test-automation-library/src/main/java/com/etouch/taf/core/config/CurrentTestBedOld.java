package com.etouch.taf.core.config;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.driver.DriverBuilder;
import com.etouch.taf.core.driver.DriverManager;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.kd.validator.TestSuiteSheetValidator;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;

public  class CurrentTestBedOld extends Thread{
	static Log log = LogUtil.getLog(TestSuiteSheetValidator.class);
	
	
	TestBed testBed=null;
	
	
	public String getTestBedName(){
		return this.getTestBed().getTestBedName();
	}


	private TestBed getTestBed() {
		return this.testBed;
	}


	public void setTestBed(TestBed testBed) {
		this.testBed = testBed;
	}
	
	public TestBed getCurrentTestBed(){
		return getTestBed();
	}
	
	
	@Override
	public void run()
	   {
	      try
	      {	
	    	  	synchronized(this.testBed){
	    	  		CommonUtil.sop(" Current Test Bed Information now : "+ this.testBed.getTestBedName());
	    	  		DriverBuilder db=DriverManager.buildDriver(this.testBed.getTestBedName());
	    	  		Object driver=db.getDriver();
	    		  this.testBed.setDriver(driver);
	    		  
	    		  TestBedManager.INSTANCE.getCurrentTestBeds().put(this.testBed.getTestBedName(), this.testBed);
	    		  Thread.sleep(18000);	    	  	
	    	  	}	    	         
	        
	     }
	      catch(DriverException e)
		     {
		       log.error("Driver is not created",e);
		       
		     }
	     catch(InterruptedException e)
	     {
	    	 log.error("my thread interrupted",e);
	    	 
	        
	     }
	    
	   }


	
    
}
