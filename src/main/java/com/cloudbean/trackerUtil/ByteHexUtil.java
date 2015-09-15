package com.cloudbean.trackerUtil;

import java.util.Arrays;


public class ByteHexUtil {
	
	/* Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。  
	 * @param src byte[] data  
	 * @return hex string  
	 */     
	public static String bytesToHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}  
	/** 
	 * Convert hex string to byte[] 
	 * @param hexString the hex string 
	 * @return byte[] 
	 */  
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	}  
	/** 
	 * Convert char to byte 
	 * @param c char 
	 * @return byte 
	 */  
	 private static byte charToByte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	}  
	 
	 
	 public static void printHexString( byte[] b) {    
		   for (int i = 0; i < b.length; i++) {   
		     String hex = Integer.toHexString(b[i] & 0xFF);   
		     if (hex.length() == 1) {   
		       hex = '0' + hex;   
		     }   
		     System.out.print(hex.toUpperCase() );   
		   }   
		  
	}  
	 
	 /** 
	  * 基于位移的int转化成byte[] 
	  * @param int  number 
	  * @return byte[] 
	  */  
	   
	 public static byte[] intToByte(int value)   
	 {   
	     byte[] src = new byte[4];  
	     src[0] = (byte) ((value>>24) & 0xFF);  
	     src[1] = (byte) ((value>>16)& 0xFF);  
	     src[2] = (byte) ((value>>8)&0xFF);    
	     src[3] = (byte) (value & 0xFF);       
	     return src;  
	 }
	   
	 /** 
	  *基于位移的 byte[]转化成int 
	  * @param byte[] bytes 
	  * @return int  number 
	  */  
	   
	 public static int bytesToInt(byte[] src) {  
		    int value;    
		    value = (int) ( ((src[0] & 0xFF)<<24)  
		            |((src[1] & 0xFF)<<16)  
		            |((src[2] & 0xFF)<<8)  
		            |(src[3] & 0xFF));  
		    return value;  
	 }
	 
	 
	
	 public static double bytesToDouble(byte[] bytes)
	    {
	        long l = getLong(bytes);
	        return Double.longBitsToDouble(l);
	    }
	 public static long getLong(byte[] bytes)  
	    {  
	        return(0xffL & (long)bytes[0]) | (0xff00L & ((long)bytes[1] << 8)) | (0xff0000L & ((long)bytes[2] << 16)) | (0xff000000L & ((long)bytes[3] << 24))  
	         | (0xff00000000L & ((long)bytes[4] << 32)) | (0xff0000000000L & ((long)bytes[5] << 40)) | (0xff000000000000L & ((long)bytes[6] << 48)) | (0xff00000000000000L & ((long)bytes[7] << 56));  
	    } 
	 
	 
	 
	 /** 
	  * 转换short为byte 
	  * 
	  */  
	public static byte[] shortToByte(short s) {
		byte[] b = new byte[2];
	     b[0] = (byte) (s >> 8);  
	     b[1] = (byte) (s >> 0); 
	     return b;
	}  
	  
	/** 
	  * 通过byte数组取到short 
	  * 
	  */  
	public static short byteToShort(byte[] b) {  
	      return (short) (((b[ 0] << 8) | b[1] & 0xff));  
	}  


}
