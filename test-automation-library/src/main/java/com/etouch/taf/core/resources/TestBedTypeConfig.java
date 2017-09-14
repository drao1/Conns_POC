package com.etouch.taf.core.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

@Configurable
public class TestBedTypeConfig {
	
	@Autowired
	@Qualifier("testBedType")
	private TestBedType testBedType;
	

}
