package com.test.invensense;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.invensense.mail.MailManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/META-INF/spring/applicationContext.xml","classpath:/META-INF/spring/app-mail.xml"})
@Transactional
public class SendMailTestCase {

	@Resource
	private MailManager mailManager ;
	
	@Test
	public void sendMail() throws MessagingException{
		Map data = new HashMap();
		data.put("jobName", "Email notification");
		mailManager.sendMail("govindpatwa@gmail.com", new String[]{"gpatwa@serenecorp.com","schakrapani@serenecorp.com"},"Email notification", "job_status.vm", data );
	}
	
}
