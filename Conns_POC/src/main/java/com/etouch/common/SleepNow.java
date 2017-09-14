package com.etouch.common;

import java.util.Map;

import com.etouch.taf.webui.selenium.WebPage;

public class SleepNow {
	
	private WebPage webPage;
	
	public SleepNow(WebPage webPage) {
		this.webPage = webPage;
	}
	
	public void iamSleeping(){
		
		
		try {
				Thread.sleep(5000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		
		
	}
		
	

}
