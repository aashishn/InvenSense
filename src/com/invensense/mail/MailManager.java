package com.invensense.mail;

import java.io.File;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Component
public class MailManager {

   @Resource
   private JavaMailSender mailSender;
   
   @Resource
   private VelocityEngine velocityEngine;
	
   @Async
	public void sendMail(String fromEmailId,String[] toEmailId,String subject,String templateName,Map data) throws MessagingException {
		MimeMessage msg =  buildMessage(fromEmailId, toEmailId, subject, templateName, data,false,null);
		mailSender.send(msg);
	}
	
   @Async
	public void sendMailWithAttachment(String fromEmailId,String toEmailId[],String subject,String templateName,Map data,Map<String,File> attachments) throws MessagingException {
		mailSender.send(buildMessage(fromEmailId, toEmailId, subject, templateName, data,true, attachments));
	}
	
	private MimeMessage buildMessage(String fromEmailId,String[] toEmailId,String subject,String templateName,Map data,boolean multipart,Map<String,File> attachments) throws MessagingException{
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "" +templateName, data);

		MimeMessage msg = mailSender.createMimeMessage();
		// use the true flag to indicate you need a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg, multipart);
		helper.setTo(toEmailId);
		helper.setFrom(fromEmailId);
		helper.setText(text,true);
		helper.setSubject(subject);

		if (multipart) {
			for(Map.Entry<String,File> entry:  attachments.entrySet()) {
				FileSystemResource file = new FileSystemResource(entry.getValue());
				helper.addAttachment(entry.getKey(), file);
			}
		}
		return msg ;
	}
}
