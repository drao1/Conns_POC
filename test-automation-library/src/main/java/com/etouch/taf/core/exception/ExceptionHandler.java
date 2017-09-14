package com.etouch.taf.core.exception;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


public class ExceptionHandler {
	
	public void handleit(ExceptionListener instance, Exception e) {
		Method m = null;
		try {
			m = instance.getClass().getMethod("divide");
		} catch (NoSuchMethodException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    Annotation as[] = m.getAnnotations();
	    for(Annotation a:as){
	    	if(a.annotationType().equals(HandleException.class)){
	    		HandleException h=(HandleException)a;
	    		
	    		if(h.exceptedExceptions().equals(e.getClass())){
	    			System.out.println("I can handle this " + h.exceptedExceptions());
	    		}else{
	    			System.out.println("I can't handle this " + h.exceptedExceptions());
	    		}
	    		
	    		System.out.println("Method Name " +m.getName() );
	    		System.out.println(" Class name" + instance.toString());
	    		
	    		
	    	}
	    }
		
	}
	
}
