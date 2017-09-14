package com.etouch.taf.kd.validator;

public class UnwantedCharacterValidation {
	
	public static boolean isAsciiPrintable(String str){
		  if (str == null) {
	          return false;
	      }
	      for (int i = 0; i < str.length(); i++) {
	          if (isAsciiPrintable(str.charAt(i)) == false) {
	              return false;
	          }
	      }
	      return true;
	}
	 public static boolean isAsciiPrintable(char ch) {
       return ch >= 32 && ch < 127;
	  }

	public static long getNumberofNonWhiteSpace(String data) {
		 
		long numberOfWhiteSpace=0;
		 for(int i=0;i<data.length();i++){
			char ch=data.charAt(i);
			if(Character.getName(ch).equals("NO-BREAK SPACE"))
				numberOfWhiteSpace++;
			
		 }
		return numberOfWhiteSpace;

	}
	
	

}
