package com.etouch.taf.kd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

@Configurable
public class KeywordLibraryConfig {
	
	@Autowired
	@Qualifier("keyword")
	private KeywordLibrary keyword;

}
