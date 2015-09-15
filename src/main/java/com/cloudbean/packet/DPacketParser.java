package com.cloudbean.packet;

import java.io.*;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.cloudbean.trackerUtil.ByteHexUtil;
import com.cloudbean.trackerUtil.JzilbHelp;

public class DPacketParser {
	public static final int DATA_TYPE_STRING = 0x00000001;
	public static final int DATA_TYPE_INTEGER = 0x00000002;
	public static final int DATA_TYPE_BYTE = 0x00000003;
	public static final int DATA_TYPE_DOUBLE = 0x00000004;
	public static final int DATA_TYPE_BOOLEAN = 0x00000005;
	
	public static final int SIGNAL_HEARTBEAT = 0x00000000;
	public static final int SIGNAL_RE_HEARTBEAT = 0xffff0000;
	
	public static final int SIGNAL_LOGIN = 0x00000001;
	public static final int SIGNAL_RE_LOGIN = 0xffff0001;
	
	public static final int SIGNAL_GETUSERINFO = 0x00000003;
	public static final int SIGNAL_RE_GETUSERINFO = 0xffff0003;
	
	public static final int SIGNAL_GETCARGROUP = 0x00000005;
	public static final int SIGNAL_RE_GETCARGROUP = 0xffff0005;
	
	public static final int SIGNAL_GETCARTRACK = 0x00000011;
	public static final int SIGNAL_RE_GETCARTRACK = 0xffff0011;
	
	public static final int SIGNAL_GETCARINFO = 0x00000006;
	public static final int SIGNAL_RE_GETCARINFO = 0xffff0006;
	
	public static final int SIGNAL_GETALARMLIST = 0x00000013;
	public static final int SIGNAL_RE_GETALARMLIST = 0xffff0013;
	
	
	public static final int SIGNAL_FAIL = 0xffffffff;
	
	
	public int pktHead=0x12345678;	//数据库报文头
	public int pktLength;
	public int pktSignal;
	public int pktDataRow;
	public int pktDataColumn; 
	public int[] pktDataColumnType;
	public int[] pktDataColumnLength;
	public byte[] pktData;
	public byte pktCheck;
	public byte pktVersion = 0x01;	//版本
	public int pktEnd = 0x87654321;	//数据库报文尾
	
	
	public byte[] pktBuffer;
	
	
	public DataTable dataTable;
	
	public DPacketParser(int pktSingal,int pktDataRow,int pktDataColumn,int[] pktDataColumnType,int[] pktDataColumnLength,byte[] pktData){
		this.pktSignal = pktSingal;
		this.pktDataRow = pktDataRow;
		this.pktDataColumn = pktDataColumn;
		this.pktDataColumnType = pktDataColumnType;
		this.pktDataColumnLength = pktDataColumnLength;
	//	System.out.println(ByteHexUtil.bytesToHexString(pktData));
		this.pktData = JzilbHelp.jzlib(pktData);
		
		this.pktLength = 20+this.pktDataColumn*4*2+this.pktData.length+6;//报文长度
		
		//System.out.println(ByteHexUtil.bytesToHexString(this.pktData));
		//this.pktData = pktData;
		ByteArrayOutputStream  bis = new ByteArrayOutputStream();
		
		try{
			bis.write(ByteHexUtil.intToByte(this.pktHead));
			bis.write(ByteHexUtil.intToByte(this.pktLength));
			bis.write(ByteHexUtil.intToByte(this.pktSignal));
			bis.write(ByteHexUtil.intToByte(this.pktDataRow));
			bis.write(ByteHexUtil.intToByte(this.pktDataColumn));
			for (int ii=0;ii<this.pktDataColumn;ii++){
				bis.write(ByteHexUtil.intToByte(pktDataColumnType[ii]));
			}
			for (int ii=0;ii<this.pktDataColumn;ii++){
				bis.write(ByteHexUtil.intToByte(pktDataColumnLength[ii]));
			}
			bis.write(this.pktData);
			this.pktCheck =packetCheck(bis.toByteArray());
			
			bis.write(this.pktCheck);
			bis.write(this.pktVersion);
			bis.write(ByteHexUtil.intToByte(this.pktEnd));
			
			
			this.pktBuffer = bis.toByteArray();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public DPacketParser(byte[] pktBuffer){
		
		int head = 0;//指向包头指针
		
		
		this.pktBuffer = pktBuffer;
		
		this.pktHead = ByteHexUtil.bytesToInt(Arrays.copyOfRange(pktBuffer,head,head+=4));
		this.pktLength = ByteHexUtil.bytesToInt(Arrays.copyOfRange(pktBuffer,head,head+=4));
		byte[] pkt =  Arrays.copyOfRange(pktBuffer, 0, this.pktLength);
		
		this.pktSignal = ByteHexUtil.bytesToInt(Arrays.copyOfRange(pktBuffer,head,head+=4));
		this.pktDataRow = ByteHexUtil.bytesToInt(Arrays.copyOfRange(pktBuffer,head,head+=4));
		this.pktDataColumn = ByteHexUtil.bytesToInt(Arrays.copyOfRange(pktBuffer,head,head+=4));
		this.pktDataColumnType = new int[this.pktDataColumn];
		this.pktDataColumnLength = new int[this.pktDataColumn];
		for (int ii=0;ii<this.pktDataColumn;ii++){
			this.pktDataColumnType[ii] = ByteHexUtil.bytesToInt(Arrays.copyOfRange(pktBuffer,head,head+=4));
		}
		for (int ii=0;ii<this.pktDataColumn;ii++){
			this.pktDataColumnLength[ii] = ByteHexUtil.bytesToInt(Arrays.copyOfRange(pktBuffer,head,head+=4));
		}
		
		int datalen = this.pktLength-4*5-this.pktDataColumn*4*2-6;
		byte[] dataorg = Arrays.copyOfRange(pkt,head,head+=datalen);
		this.pktData = JzilbHelp.unjzlib(dataorg);
		this.pktCheck = pkt[head++];
		this.pktVersion = pkt[head++];
		
		this.pktEnd = ByteHexUtil.bytesToInt(Arrays.copyOfRange(pkt,head++,head+=4));
		
		this.dataTable = new DataTable(this.pktDataRow,this.pktDataColumn,this.pktDataColumnType,this.pktDataColumnLength,this.pktData);
	}
	
	private byte packetCheck(byte[] data){
		byte res=data[0];
	    for (int ii=1; ii<data.length;ii++)
	    { 
	    	   res = (byte) (res ^ data[ii]);
	     }
		return res;
	}

}



