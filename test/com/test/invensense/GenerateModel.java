package com.test.invensense;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class GenerateModel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			FileReader fr = new FileReader(new File(args[0]));
			FileWriter writer = new FileWriter(new File(args[1]));
			StringBuilder sb = new StringBuilder();
			sb.append("package com.crmod.model;");
			sb.append("import javax.persistence.Entity;");

			sb.append("import javax.persistence.Id;");
			sb.append("import javax.xml.bind.annotation.XmlAccessType;");
			sb.append("import javax.persistence.Id;");
			sb.append("import javax.xml.bind.annotation.XmlAccessType;");
			sb.append("import javax.xml.bind.annotation.XmlAccessorType;");
			sb.append("import javax.xml.bind.annotation.XmlElement;");
			sb.append("import javax.xml.bind.annotation.XmlRootElement;");
			
			sb.append("@XmlRootElement(name=\"Account\")");
			sb.append("@XmlAccessorType(XmlAccessType.NONE)");
			sb.append("@Entity(name=\"staging_account\")");
			BufferedReader br = new BufferedReader(fr);
			String s;
			int index  = 0 ;
			while((s = br.readLine()) != null) {
				String[] fieldInfo = s.split(",");
				if (index == 0) {
					sb.append("@Id");	
				} else {
					sb.append("@Column(name=\"");
					String cName = fieldInfo[0];
					cName = cName.replaceAll(" ", "_");
					cName = cName.toUpperCase();
					sb.append(cName);
					sb.append("\",length=");
					sb.append(fieldInfo[3]);
				}
				
				sb.append("@XmlElement(name=\"Id\")");
				sb.append("private");
				sb.append(" ");
				sb.append(fieldInfo[2]);
				sb.append(fieldInfo[0]);
				sb.append(";");
				sb.append(s);
			}
			fr.close();
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
