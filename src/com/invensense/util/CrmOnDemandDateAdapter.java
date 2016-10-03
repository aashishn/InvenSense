package com.invensense.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CrmOnDemandDateAdapter extends XmlAdapter<String, Date> {
	
	private SimpleDateFormat onDemandDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	@Override
	public String marshal(Date v) throws Exception {
		return onDemandDateFormat.format(v);
	}

	@Override
	public Date unmarshal(String v) throws Exception {
		return onDemandDateFormat.parse(v);
	}

}
