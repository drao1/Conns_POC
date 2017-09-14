package com.etouch.taf.webui;

import java.util.HashMap;
import java.util.Map;


/**
 * The Class TafElementFactoryManager.
 */
public class TafElementFactoryManager {
	
	/** The factories. */
	private static Map<String, ITafElementFactory> factories =
		      new HashMap<String, ITafElementFactory>();

		  /**
  		 * Sets the factory.
  		 *
  		 * @param factoryId the factory id
  		 * @param factory the factory
  		 */
  		public static void setFactory(
		       String factoryId, ITafElementFactory factory){
			  factories.put(factoryId, factory);
		  }

		  /**
  		 * Gets the factory.
  		 *
  		 * @param factoryId the factory id
  		 * @return the factory
  		 */
  		public static ITafElementFactory getFactory(String factoryId){
		    return factories.get(factoryId);
		  }


}
