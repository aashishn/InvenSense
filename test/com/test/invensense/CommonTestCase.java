package com.test.invensense;

import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/META-INF/spring/applicationContext.xml","classpath:/META-INF/spring/app-mail.xml"})
public class CommonTestCase {
	
	private static Logger log = Logger.getLogger(CommonTestCase.class);
	
	@Test
	public void encryptDecryptString() {
		try {
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword("crmod");
			String encrypt = encryptor.encrypt("111");
			log.debug(" Encrypted String ENC(" + encrypt + ")");
			
			log.debug(" Decrypted String " + encryptor.decrypt(encrypt) );
		} catch (Exception e) {
			log.error(" Error ",e);
		}
	}

}
