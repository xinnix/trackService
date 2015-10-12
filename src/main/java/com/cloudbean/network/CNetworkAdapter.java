package com.cloudbean.network;


import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cloudbean.model.Alarm;
import com.cloudbean.model.Car;
import com.cloudbean.model.CarState;
import com.cloudbean.model.GPRMC;
import com.cloudbean.packet.CPacketParser;
import com.cloudbean.packet.DPacketParser;
import com.cloudbean.packet.MsgGPRSParser;
import com.cloudbean.trackerUtil.ByteHexUtil;
import com.cloudbean.trackerUtil.GpsCorrect;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

public class CNetworkAdapter extends BaseNetworkAdapter {
	
	public static int MSG_FAIL = 0x2000;
	public static int MSG_LOGIN = 0x2001;
	public static int MSG_POSITION = 0x2002;
	public static int MSG_DEF = 0x2003;
	public static int MSG_CIRCUIT = 0x2004;
	public static int MSG_ALARM = 0x2005;
	public static int MSG_POSCOMPLETE = 0x2006;
	Map<String, CarState> carPosition=new HashMap<String, CarState>();
	Map<String, GPRMC> carGPRMC=new HashMap<String, GPRMC>();
	
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
				 // if login succ, then get all last position
				 if (this.handler.c_rLogin(cp) == 1) {
					 this.sendGetAllLastPosition();
				 }
				
				break;
			case CPacketParser.SIGNAL_PREPOSITION:
				break;
			case CPacketParser.SIGNAL_POSCOMPLETE:

				break;
			 case CPacketParser.SIGNAL_CENTERALARM:
				 //final Alarm alarm = null;
				 //TrackApp.playAlarmSound();
				 String alarmtype = null;
				 if(cp.pktBuffer[9]==3){
					 if(cp.pktBuffer[19]==1){
						 alarmtype ="进入区域报警";
					 }else{
						 alarmtype = "出区域报警";
					 }
				 }
				 // final Alarm alarm = new Alarm(cp.pktFakeIP,alarmtype);
				 //TrackApp.alarmList.add(alarm);
//				 if(alarm.alarmType != null){
//					 new Thread(){
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								Looper.prepare();
//								Toast.makeText(context,alarm.termName+"发生"+alarm.alarmType ,Toast.LENGTH_SHORT).show();
//								Looper.loop();
//							}	 
//						 }.start();
//				 }
//				 
//				msg.what =MSG_ALARM;
//				TrackApp.curHandle
			case CPacketParser.SIGNAL_RELAY:
				MsgGPRSParser mgp =  new MsgGPRSParser(Arrays.copyOfRange(cp.pktData, 4, cp.pktData.length));
				String resultMsg = "";
				switch(mgp.msgType){
				case MsgGPRSParser.MSG_TYPE_DEF:
					resultMsg = mgp.msgData.equals("00")? "设防/撤防失败" : "设防/撤防成功";

					break;
				case MsgGPRSParser.MSG_TYPE_POSITION:
					System.out.println("[recv:postion-cna]:" + this.getUsername());
					CarState cs =this.handler.c_rGetCarPosition(mgp);
					GPRMC gprmc = this.handler.c_rParseGPRMC(mgp);
					if(gprmc.latitude!=0&&gprmc.longitude!=0){

						// GPS correct
						double[] correctXY = new double[2];
						GpsCorrect.transform(gprmc.latitude, gprmc.longitude, correctXY);
						gprmc.latitude = correctXY[0];
						gprmc.longitude = correctXY[1];
						
						carGPRMC.put("gprmc", gprmc);						
						carPosition.put(cs.devid, cs);
						
						Wilddog devRef = wdRootRef.child("position");
						devRef.setValue(carPosition);
						
						devRef.child(cs.devid).setValue(carGPRMC);
					}
					break;
				 case MsgGPRSParser.MSG_TYPE_PHONE:
					 resultMsg = mgp.msgData.equals("00")?"监听号码设置失败":"监听号码设置成功";
				 case MsgGPRSParser.MSG_TYPE_GPSREBOOT:
					 resultMsg = mgp.msgData.equals("00")?"GPS重置失败":"GPS重置成功";
				 case MsgGPRSParser.MSG_TYPE_GPSHEARTBEAT:
					 resultMsg = mgp.msgData.equals("00")?"GPS心跳间隔设置失败":"GPS心跳间隔设置成功";
				 case MsgGPRSParser.MSG_TYPE_EXPANDCOMMAND:
					 resultMsg = mgp.msgData.equals("00")?"扩展命令执行失败":"扩展命令执行成功";
				 case MsgGPRSParser.MSG_TYPE_TRACEINTERVAL:
					 resultMsg = mgp.msgData.equals("00")?"设置定时追踪执行失败":"设置定时追踪执行成功";
				case MsgGPRSParser.MSG_TYPE_CIRCUIT:
					String test = ByteHexUtil.bytesToHexString(mgp.msgByteBuf);
					break;
				case MsgGPRSParser.MSG_TYPE_ALARM:
					System.out.println("Receving packet type: msg type alarm [from center control server]");
					Alarm al = this.handler.c_rGetAlarmInfo(mgp);
					Map<String, Alarm> carAlarm= new HashMap<String, Alarm>();
					carAlarm.put(al.termid, al);
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
	
	public void sendCommand(Car car,short commandType,String data){
		byte[] dataPacket = this.handler.c_sCommand(car, commandType, data);
		// return ByteHexUtil.bytesToHexString(dataPacket);
		this.sendPacket(dataPacket);
	}	
	
	public void sendSetPhoneCommand(Car car, String data){
		this.sendCommand(car, MsgGPRSParser.MSG_TYPE_PHONE, data);
	}	
	
	public void sendGPSReboot(Car car,String data){
		this.sendCommand(car, MsgGPRSParser.MSG_TYPE_GPSREBOOT, data);
	}
	
	public void sendExpandCommand(Car car,String data){
		this.sendCommand(car, MsgGPRSParser.MSG_TYPE_EXPANDCOMMAND, data);
	}
	
	public void sendGPSHeartBeat(Car car,String data){
		this.sendCommand(car, MsgGPRSParser.MSG_TYPE_GPSHEARTBEAT, data);
	}
	
	public void  sendTraceInterval(Car car,String data){
		this.sendCommand(car, MsgGPRSParser.MSG_TYPE_TRACEINTERVAL, data);
	}
	
}
