package com.etouch.taf.util;

import java.util.HashMap;

import org.openqa.selenium.Keys;

public class KeysMap {

		static HashMap<String, CharSequence> keyMap=new HashMap<String,CharSequence>();
		
		public static  CharSequence get(String key){
			
			if(keyMap.size()<1){
				initialize();
			}
			
			CharSequence chars=keyMap.get(key);
			return chars;
			
		}
		
		private static void initialize(){
			
			for(Keys key:Keys.values()){
				keyMap.put(key.name(), key);
				
			}
			
		}
		
		
		
	
}
