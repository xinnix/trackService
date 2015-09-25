package com.cloudbean.packet;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import com.cloudbean.trackerUtil.ByteHexUtil;

public class CPacketParser {
	
	public final static short PACKET_HEADER = 0x2929; 
	public final static byte PACKET_END = (byte)0x0d; 
	
	public final static byte SIGNAL_LOGIN = (byte)0xa3; 
	public final static byte SIGNAL_RE_LOGIN = (byte)0xe3; 

	public final static byte SIGNAL_RELAY = (byte)0xaa; 
	public final static byte SIGNAL_POSCOMPLETE = (byte)0xa9; 
	public final static byte SIGNAL_CENTERALARM = (byte)0xf2;
	public final static byte SIGNAL_PREPOSITION = (byte)0xbb; 
	
	public short pktHead=0x2929;
	public byte pktSignal;
	public short pktLength;
	public int pktFakeIP;
	public byte[] pktData;
	public byte pktCheck;
	public byte pktEnd=0x0d;
	
	//存储字节流
	public byte[] pktBuffer;
	
	/*
	 * 发送时指定包类型
	 */
	public CPacketParser(byte pktSignal,int pktFakeIP,byte[] data){
		
		
		
		
		
		
		this.pktSignal = pktSignal;
		this.pktData = data;
		this.pktLength = (short)(6+(data!=null?data.length:0));
		this.pktFakeIP = pktFakeIP;
		
		ByteArrayOutputStream  bis = new ByteArrayOutputStream();
		
		try{
			bis.write(ByteHexUtil.shortToByte(this.pktHead));
			bis.write(this.pktSignal);
			bis.write(ByteHexUtil.shortToByte(this.pktLength));
			bis.write(ByteHexUtil.intToByte(this.pktFakeIP));
			if (this.pktData != null){
				bis.write(this.pktData);
			}
			
			
			this.pktCheck =packetCheck(bis.toByteArray());
			
			bis.write(this.pktCheck);
			bis.write(this.pktEnd);
			
			
			
			this.pktBuffer = bis.toByteArray();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
	/*
	 * 接收包并解析
	 */
	public CPacketParser(byte[] pktBuffer){
		
		// System.out.println(ByteHexUtil.bytesToHexString(pktBuffer));
		int head = 0;
		
		this.pktHead = ByteHexUtil.byteToShort(Arrays.copyOfRange(pktBuffer,head,head+=2));
		this.pktSignal = pktBuffer[head];
		head++;
		this.pktLength = ByteHexUtil.byteToShort(Arrays.copyOfRange(pktBuffer,head,head+=2));
		this.pktFakeIP = ByteHexUtil.bytesToInt(Arrays.copyOfRange(pktBuffer,head,head+=4));
		
		int datalen = this.pktLength - 6;
		this.pktData =  Arrays.copyOfRange(pktBuffer, head, head+=datalen);
		this.pktCheck = pktBuffer[head];
		head++;
		this.pktEnd = pktBuffer[head];
		this.pktBuffer = pktBuffer;
	
	}
	
	public static byte packetCheck(byte[] data){
		byte res=data[0];
	    for (int ii=1; ii<data.length;ii++)
	    { 
	    	   res = (byte) (res ^ data[ii]);
	     }
		return res;
	}

}
