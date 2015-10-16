package com.cloudbean.network;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import trackService.SocketListener;

import com.cloudbean.model.Alarm;
import com.cloudbean.model.Car;
import com.cloudbean.model.CarState;
import com.cloudbean.model.GPRMC;
import com.cloudbean.packet.CPacketParser;
import com.cloudbean.packet.DPacketParser;
import com.cloudbean.packet.MsgGPRSParser;
import com.cloudbean.trackerUtil.ByteHexUtil;
import com.cloudbean.trackerUtil.GpsCorrect;
import com.cloudbean.trackme.TrackAppClient;
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
	Map<String, HashMap> carPosition=new HashMap<String, HashMap>();
	Map<String, GPRMC> carGPRMC=new HashMap<String, GPRMC>();
	private Object outputStream;
	private boolean isGettingAllLastPosition = false;
	
	public void reLogin(){
		System.out.println("[CNA-RELOGIN] for " + this.username + " with password: " + this.password);
		byte[] dataPacket = this.handler.c_sLogin(this.username, this.password);
		this.sendPacket(dataPacket);
	}
	
	public CNetworkAdapter(final String serverIP,final int port ){
		super(serverIP, port );
		connect(); 
		System.out.println("start socket of center-control-networkAdapter");
	}

	public CNetworkAdapter(byte[] packet){
		super(packet);
	}

	public byte[] preParser() throws Exception{

		ByteArrayOutputStream  bos = new ByteArrayOutputStream();
		
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


		byte[] buf = bos.toByteArray();
		//String res = ByteHexUtil.bytesToHexString(buf);
		return buf;
	}

	@Override
	public void recivePacket() throws Exception {		
				  			 			 
			byte[] packetByte  = preParser();

			CPacketParser cp = new CPacketParser(packetByte);			
			
			switch (cp.pktSignal){
			case CPacketParser.SIGNAL_RE_LOGIN:
				 System.out.println("Receving packet type: login [from center control server]");
				 // if login success, then get all last position
				 if (this.handler.c_rLogin(cp) == 0) {
					 this.isLoginValid = true;
					 this.password = this.tmpPassword;					
					 this.sendGetAllLastPosition();
					 
				 }
				
				break;
			case CPacketParser.SIGNAL_PREPOSITION:
				System.out.println(new Date() +"start to get all the last position");
				this.isGettingAllLastPosition = true;
				break;
			case CPacketParser.SIGNAL_POSCOMPLETE:
				this.isGettingAllLastPosition = false;
				System.out.println(new Date() + "end to get all the last position");
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
				 break;
			case CPacketParser.SIGNAL_RELAY:
				MsgGPRSParser mgp =  new MsgGPRSParser(Arrays.copyOfRange(cp.pktData, 4, cp.pktData.length));
				String resultMsg = "";
				switch(mgp.msgType){
				case MsgGPRSParser.MSG_TYPE_DEF:
					resultMsg = mgp.msgData.equals("00")? "设防/撤防失败" : "设防/撤防成功";

					break;
				case MsgGPRSParser.MSG_TYPE_POSITION:					
					// get the client
					TrackAppClient appClient = SocketListener.mainTranslator.getTrackAppClient(this.getUsername());
					Car[] carList = appClient.getCarList();
					HashMap<String, String> carStateMap = new HashMap<String, String>();
					String curCarDevType = "";
					CarState cs = this.handler.c_rGetCarPosition(mgp);					
					GPRMC gprmc = this.handler.c_rParseGPRMC(mgp);
					if(carList == null){
						System.out.println("no cars, send cmd to get all last position for user " + this.getUsername());
						this.sendGetAllLastPosition();
						
						break;
					}else if(gprmc == null){					
						break;
					}
					
					for(int i=0; i<carList.length; i++){						
						if(carList[i].devId .equals(cs.devid)){
							curCarDevType = carList[i].getDevtype();
							System.out.println(
									  "[position] " + this.getUsername() 
									+ " carList[" + i + "] Type:" + curCarDevType 
									+ " Name:" + carList[i].name 
									+ " devid:" + cs.devid );							
						}
					}
					String accState;
					String voltage ;
					double voltageDbl ;
					DecimalFormat formatter = new DecimalFormat("##0.0");
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(curCarDevType.equals("MT400")){
						carStateMap.put("devtype", "MT400");
					 	carStateMap.put("temperature", "0");
						accState = ByteHexUtil.getBooleanArray(cs.portState[0])[6] ? "开"
: "关";
						carStateMap.put("accState", accState);
						voltageDbl = cs.voltage * 3.2 * 16 / 4096;							
						voltage = formatter.format(voltageDbl);
						carStateMap.put("voltage", voltage);
					} else if (curCarDevType.equals("VT310")){
						carStateMap.put("devtype", "VT310");
						if (Float.parseFloat(cs.temperature) < 200 
					        && Float.parseFloat(cs.temperature) > 0){
							carStateMap.put("temperature", cs.temperature);
						} else {
							carStateMap.put("temperature", "0");
						}
						
						accState = ByteHexUtil.getBooleanArray(cs.portState[0])[3] ? "开"
: "关";
						carStateMap.put("accState", accState);
						voltageDbl = cs.voltage * 3.2 * 16 / 4096;
						
						voltage = formatter.format(voltageDbl);
						carStateMap.put("voltage", voltage);
					}else{
						carStateMap.put("devtype", curCarDevType);
						carStateMap.put("temperature", "0");
						voltageDbl = cs.voltage * 3.3 / 2048 + 0.5;
						voltage = formatter.format(voltageDbl);
						carStateMap.put("voltage", voltage);
						carStateMap.put("accState", "无状态");						
					}
					carStateMap.put("speed", gprmc.speed);
					carStateMap.put("distant", cs.distant);
					carStateMap.put("date", dateFormat.format(new Date()));
					carStateMap.put("gsmStrength", "" + ByteHexUtil.hexStringToBytes(cs.gsmStrength)[0]);
					
					// format the latitude and longitude
					DecimalFormat Latiformatter = new DecimalFormat("##0.000000");
					if(gprmc.latitude!=0&&gprmc.longitude!=0){
						// GPS correct
						double[] correctXY = new double[2];
						GpsCorrect.transform(gprmc.latitude, gprmc.longitude, correctXY);
						gprmc.latitude = correctXY[0];
						gprmc.longitude = correctXY[1];
						cs.setGprmc(gprmc);
						carStateMap.put("lat", Latiformatter.format(gprmc.latitude));
						carStateMap.put("lon", Latiformatter.format(gprmc.longitude));
					}	
					gprmc = null;
					
					carPosition.put(cs.devid, carStateMap);					
					Wilddog positionRef = wdRootRef.child("position");				    
					positionRef.setValue(carPosition);										
					
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
					Alarm al = this.handler.c_rGetAlarmInfo(mgp);
					System.out.println("[CNA-ALARM] type " + al.alarmType + " term-id: " + al.termid + " time: " + al.alarmTime);
					Map<String, Alarm> carAlarm= new HashMap<String, Alarm>();
					carAlarm.put(al.termid, al);
					Wilddog devRef = wdRootRef.child("alarm");
					devRef.setValue(carAlarm);
					break;
				}
			}	

	}

	/**
	 * send login command to the center control server.
	 * @param username
	 * @param password
	 */
	public void sendLoginCmd(String username, String password){
		byte[] dataPacket = this.handler.c_sLogin(username, password);
		this.username = username;
		this.tmpPassword = password;
		this.sendPacket(dataPacket);
	}
	
	public void sendGetAllLastPosition(){
		if (!this.isGettingAllLastPosition){
			System.out.println("c_s Get All Last Position cmd is sending...");
			
			byte[] dataPacket = this.handler.c_sGetAllLastPosition();
			this.sendPacket(dataPacket);
			this.isGettingAllLastPosition = true;
		}else{
			System.out.println("[Wait for response] GetAllLastPosition cmd is sending...");
		}
	}
	
	public void sendCommand(String car_devid, String ipAddress, short commandType,String data){
		byte[] dataPacket = this.handler.c_sCommand(car_devid, ipAddress, commandType, data);
		// return ByteHexUtil.bytesToHexString(dataPacket);
		this.sendPacket(dataPacket);
	}	
	
	// 发送自定义指令
	public void sendSetDef(String devid, String ipAddress, String data){
		byte[] dataPacket = this.handler.c_sSetDef(devid, ipAddress, data);
		this.sendPacket(dataPacket);			
	}
	
	
	public void sendSetCircuit(String devid, String ipAddress,String data){	
		byte[] dataPacket = this.handler.c_sSetCircuit(devid, ipAddress, data);
		this.sendPacket(dataPacket);
	}
	
	public void sendSetPhoneCommand(String car_devid, String ipAddress, String data){
		this.sendCommand(car_devid, ipAddress, MsgGPRSParser.MSG_TYPE_PHONE, data);
	}	
	
	public void sendGPSReboot(String car_devid, String ipAddress,String data){
		this.sendCommand(car_devid, ipAddress, MsgGPRSParser.MSG_TYPE_GPSREBOOT, data);
	}
	
	public void sendExpandCommand(String car_devid, String ipAddress,String data){
		this.sendCommand(car_devid, ipAddress, MsgGPRSParser.MSG_TYPE_EXPANDCOMMAND, data);
	}
	
	// 设置GPRS心跳间隔时间
	public void sendGPSHeartBeat(String car_devid, String ipAddress,String data){
		this.sendCommand(car_devid, ipAddress, MsgGPRSParser.MSG_TYPE_GPSHEARTBEAT, data);
	}
	
	public void  sendTraceInterval(String car_devid, String ipAddress,String data){
		this.sendCommand(car_devid, ipAddress, MsgGPRSParser.MSG_TYPE_TRACEINTERVAL, data);
	}
	
}
