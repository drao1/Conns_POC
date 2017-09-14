package com.etouch.taf.kd.config;

import java.util.LinkedList;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;

public class CheckList {
	
	static Log log =LogUtil.getLog(CheckList.class);
	
	public static void main(String args[]){
		LinkedList<String> s= new LinkedList<String>();
		
		for(int i=0;i<10;i++){
			s.add(i+"");
		}
		
		
		for(String g:s){
			log.debug(g);
		}
		
	}

}
