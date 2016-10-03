package com.invensense.util;
/*
* creating a class which have two function getByteArrayObject and getJavaObject.
* getByteArrayObject convert java object into byte[] and return the byte[].
* getJavaObject convert byte[] to java object. and return SimpleExample object.
*
*/
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class ConvertObject {
	//converting Interface object to byte[].
	public static byte[] getByteArrayObject(Object o){
		byte[] byteArrayObject = null;
        try {
           
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
           
            oos.close();
            bos.close();
            byteArrayObject = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return byteArrayObject;
        }
        return byteArrayObject;
    }
    //converting byte[] to Object  
    public static Object getJavaObject(byte[] convertObject){
    	Object obj = null;
    	ByteArrayInputStream bais;
    	ObjectInputStream ins;
    	try {
    		bais = new ByteArrayInputStream(convertObject);
    		ins = new ObjectInputStream(bais);
    		obj =(Object)ins.readObject();
    		ins.close();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	return obj;
    }
}
