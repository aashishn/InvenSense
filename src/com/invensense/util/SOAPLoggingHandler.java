package com.invensense.util;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

/*
 * This simple SOAPHandler will output the contents of incoming
 * and outgoing messages.
 */
public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {

	private static final Logger log = Logger.getLogger(SOAPLoggingHandler.class);
	
	// change this to redirect output if desired
	//private static PrintStream out = System.out;

	public Set<QName> getHeaders() {
		return null;
	}

	public boolean handleMessage(SOAPMessageContext smc) {
		logToSystemOut(smc);
		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		logToSystemOut(smc);
		return true;
	}

	// nothing to clean up
	public void close(MessageContext messageContext) {
	}

	/*
	 * Check the MESSAGE_OUTBOUND_PROPERTY in the context to see if this is an
	 * outgoing or incoming message. Write a brief message to the print stream
	 * and output the message. The writeTo() method can throw SOAPException or
	 * IOException
	 */
	private void logToSystemOut(SOAPMessageContext smc) {
		Boolean outboundProperty = (Boolean) smc
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (outboundProperty.booleanValue()) {
			log.info("\nOutbound message:");
		} 
		
		/*else {
			log.info("\nInbound message:");
		}*/

		SOAPMessage message = smc.getMessage();
		try {
			//message.writeTo(out);
			log.info(getMsgAsString(message));
			log.info(""); // just to add a newline
		} catch (Exception e) {
			log.info("Exception in handler: " + e);
		}
	}
	
	 public String getMsgAsString(SOAPMessage message) {
	    	String msg = null;
	    	try {
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	message.writeTo(baos);
	    	msg = baos.toString();
	    	} catch (Exception e) {
	    	e.printStackTrace();
	    	}
	    	return msg;
	    	}
	    
}