package com.cloudbean.network;


import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cloudbean.model.Alarm;
import com.cloudbean.model.Car;
import com.cloudbean.model.CarState;
import com.cloudbean.packet.CPacketParser;
import com.cloudbean.packet.DPacketParser;
import com.cloudbean.packet.MsgGPRSParser;
import com.cloudbean.trackerUtil.ByteHexUtil;
import com.cloudbean.trackerUtil.GpsCorrect;
import com.wilddog.client.Wilddog;

public class CNetworkAdapter extends BaseNetworkAdapter {
	
	public static int MSG_FAIL = 0x2000;
	public static int MSG_LOGIN = 0x2001;
	public static int MSG_POSITION = 0x2002;
	public static int MSG_DEF = 0x2003;
	public static int MSG_CIRCUIT = 0x2004;
	public static int MSG_ALARM = 0x2005;
	public static int MSG_POSCOMPLETE = 0x2006;
	Map<String, CarState> carPosition=new HashMap<String, CarState>();
	
	public CNetworkAdapter(final String serverIP,final int port){
		super(serverIP,port);
		connect(); 
		System.out.println("start socket of center-control-networkAdapter");
	}

	public CNetworkAdapter(byte[] packet){
		super(packet);
	}

	public byte[] preParser(){

		ByteArrayOutputStream  bos = new ByteArrayOutputStream();
		try{
			short header = dis.readShort();
			byte signal = dis.readByte();
			short datalen = dis.readShort();
			if(signal == 0xa9){
				System.out.print("pos complete");
			}
			byte[] packet = new byte[datalen];
			dis.readFully(packet);
			bos.write(ByteHexUtil.shortToByte(header));
			bos.write(signal);
			bos.write(ByteHexUtil.shortToByte(datalen));
			bos.write(packet);


		}catch(Exception e){
			e.printStackTrace();
		}
		byte[] buf = bos.toByteArray();
		String res = ByteHexUtil.bytesToHexString(buf);
		return buf;
	}

	@Override
	public void recivePacket() throws Exception {
		// TODO Auto-generated method stub
		try{		  			 			 
			byte[] packetByte  = preParser();

			CPacketParser cp = new CPacketParser(packetByte);

			switch (cp.pktSignal){
			case CPacketParser.SIGNAL_RE_LOGIN:
				 System.out.println("Receving packet type: login [from center control server]");
				// int i = this.handler.c_rLogin(cp);
				this.sendGetAllLastPosition();
				//msg.what =MSG_LOGIN;
				break;
			case CPacketParser.SIGNAL_PREPOSITION:
				break;
			case CPacketParser.SIGNAL_POSCOMPLETE:

				break;
			case CPacketParser.SIGNAL_RELAY:
				MsgGPRSParser mgp =  new MsgGPRSParser(Arrays.copyOfRange(cp.pktData, 4, cp.pktData.length));

				switch(mgp.msgType){
				case MsgGPRSParser.MSG_TYPE_DEF:


					break;
				case MsgGPRSParser.MSG_TYPE_POSITION:
					 System.out.println("Receving packet type: msg type postion [from center control server]");
					CarState cs =this.handler.c_rGetCarPosition(mgp);
					if(cs.gprmc.latitude!=0&&cs.gprmc.longitude!=0){

						// GPS correct
						double[] correctXY = new double[2];
						GpsCorrect.transform(cs.gprmc.latitude, cs.gprmc.longitude, correctXY);
						cs.gprmc.latitude = correctXY[0];
						cs.gprmc.longitude = correctXY[1];

						carPosition.put(cs.devid, cs);
						Wilddog devRef = wdRootRef.child("position");
						devRef.setValue(carPosition);
					}
					break;
				case MsgGPRSParser.MSG_TYPE_CIRCUIT:
					String test = ByteHexUtil.bytesToHexString(mgp.msgByteBuf);
					break;
				case MsgGPRSParser.MSG_TYPE_ALARM:
					 System.out.println("Receving packet type: msg type alarm [from center control server]");
					Alarm al = this.handler.c_rGetAlarmInfo(mgp);
					Map<String, Alarm> carAlarm= new HashMap<String, Alarm>();
					carAlarm.put(al.devid, al);
					Wilddog devRef = wdRootRef.child("alarm");
					devRef.setValue(carAlarm);
					break;
				}
			}
		}catch(Exception e ){
			throw e; 
		}

	}

	/**
	 * send login command to the center control server.
	 * @param username
	 * @param password
	 */
	public void sendLoginCmd(String username, String password){
		byte[] dataPacket = this.handler.c_sLogin(username, password);
		this.sendPacket(dataPacket);
	}
	
	public void sendGetAllLastPosition(){
		System.out.println("c_sGetAllLastPosition cmd is sending...");
		byte[] dataPacket = this.handler.c_sGetAllLastPosition();
		this.sendPacket(dataPacket);
	}
	
	public String sendCommand(Car car,short commandType,String data){
		byte[] dataPacket = this.handler.c_sCommand(car, commandType, data);
		return ByteHexUtil.bytesToHexString(dataPacket);
	}	
}
