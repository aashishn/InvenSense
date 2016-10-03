package com.invensense.util;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class PropFileUtil {

    
	private static Properties prop = null;
    
	private static final Logger log = Logger.getLogger(PropFileUtil.class);

    public static void load() throws Exception {
    	if ( prop == null ) {
	        initialize();
    	}
    }

	private synchronized static void initialize() throws Exception {
		if ( prop != null ){
			return;
		}
		
		try {
		    prop = new Properties();

		    ClassLoader classLoader = PropFileUtil.class.getClassLoader();
		    InputStream inFile = classLoader.getResourceAsStream(Constants.SC_PROPERTY_FILE_NAME);
//		    InputStream inFile1 = classLoader.getResourceAsStream(Constants.ERROR_INTERPRETATION_PROP_FILE_NAME);
		    prop.load(inFile);
//		    prop.load(inFile1);
		} catch (Exception exp) {
		    throw new Exception("Error while loading NHP Property file", exp);
		}
	}

    /**
     * Returns a property value from the property File.
     * @param propertyKey the property key.
     * @return the property value.
     */
    public static String getValue(String propertyKey) {
        String propertyValue = null;

        if ((propertyKey == null) || (propertyKey.trim().length() <= 0)) {
            throw new IllegalArgumentException("Property Key is empty or null");
        }

        if ( prop == null ){
        	try {
				load ();
			} catch (Exception e) {
				log.error( "prop is null in PropFileUtil, and exception thrown while trying to init.", e );
			}
        }
        
        propertyValue = prop.getProperty(propertyKey);
       /* if(propertyValue!=null)
        {
        	if(propertyValue.indexOf(Constants.PROPERTY_ENCRYPTION_PREFIX)>=0)
        	{
        		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        		//log.debug("Encrypted:"+propertyValue);
        		encryptor.setPassword(Constants.PROPERTY_ENCRYPTION_PASSWORD);
        		propertyValue=propertyValue.substring(Constants.PROPERTY_ENCRYPTION_PREFIX.length()
        				, propertyValue.length()-1);
        		
        		//log.debug("propertyValue:"+propertyValue);
        		propertyValue=encryptor.decrypt(propertyValue);
        		//log.debug("Decrypted:"+propertyValue);
        
        		
        	}
        }*/

        return propertyValue.trim();
    }
    
    /**
     * Returns a property value list from the property File based on the separator used.
     * (eg. contactField=contactId, contactName)
     * 
     * @param propertyKey the property key.
     * @param separator the property separator.
     * @return the property value.
     */
    public static List<String> getValueList(String propertyKey, String separator) {

		String listOfProperties = PropFileUtil.getValue(propertyKey);
		List<String> valueList = new ArrayList<String>();

		StringTokenizer parser = new StringTokenizer(listOfProperties, separator, false);

		while (parser.hasMoreTokens()) {
			valueList.add(parser.nextToken());
		}

		return valueList;
    }
    
    public static String getValue(String propertyKey,String defaultValue) {
        String propertyValue = null;
        if ((propertyKey == null) || (propertyKey.trim().length() <= 0)) {
            throw new IllegalArgumentException("Property Key is empty or null");
        }
        if ( prop == null ){
        	try {
				load ();
			} catch (Exception e) {
				log.error( "prop is null in PropFileUtil, and exception thrown while trying to init.", e );
			}
        }
        propertyValue = prop.getProperty(propertyKey);
        if (propertyValue == null) propertyValue = defaultValue ;
    	/*if(propertyValue.indexOf(Constants.PROPERTY_ENCRYPTION_PREFIX)>=0) {
    		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    		encryptor.setPassword(Constants.PROPERTY_ENCRYPTION_PASSWORD);
    		propertyValue=propertyValue.substring(Constants.PROPERTY_ENCRYPTION_PREFIX.length(), propertyValue.length()-1);
    		propertyValue=encryptor.decrypt(propertyValue);
    	}*/
        return propertyValue.trim();
    }
}
