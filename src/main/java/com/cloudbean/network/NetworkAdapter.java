package com.cloudbean.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cloudbean.model.Alarm;
import com.cloudbean.model.Car;
import com.cloudbean.model.CarGroup;
import com.cloudbean.model.CarState;
import com.cloudbean.model.Fail;
import com.cloudbean.model.Login;
import com.cloudbean.model.Track;
import com.cloudbean.packet.DPacketParser;
import com.cloudbean.trackerUtil.ByteHexUtil;
import com.cloudbean.trackerUtil.GpsCorrect;
import com.wilddog.client.Wilddog;

public class NetworkAdapter extends BaseNetworkAdapter {

	public static int MSG_FAIL = 0x1000;
	public static int MSG_LOGIN = 0x1001;
	public static int MSG_CARINFO = 0x1002;
	public static int MSG_CARGROUPINFO = 0x1003;
	public static int MSG_TRACK = 0x1004;
	public static int MSG_ALARM = 0x1005;

	 Wilddog devRef =null;
	
	 public NetworkAdapter(final String serverIP,final int port){
		 super(serverIP,port);
		 connect();
		 System.out.println("start socket of networkAdapter");
	}
	
	 public NetworkAdapter(byte[] packet){
		 super(packet);
	 }

	 public byte[] preParser() throws Exception{
		 
		 ByteArrayOutputStream  bos = new ByteArrayOutputStream();
		 try{
			 int header = dis.readInt();
			 int datalen = dis.readInt();
			 byte[] packet = new byte[datalen-8];
			 dis.readFully(packet);
			 bos.write(ByteHexUtil.intToByte(header));
			 bos.write(ByteHexUtil.intToByte(datalen));
			 bos.write(packet);
			 
			 
		 }catch(Exception e){
			 throw e;
		 }
		 return bos.toByteArray();
	 }
 
	 public void recivePacket() throws Exception {
		 try{			 
			 byte[] packetByte  = preParser();				 
				 DPacketParser dp = new DPacketParser(packetByte);
				
				 
				 switch (dp.pktSignal){
				 case DPacketParser.SIGNAL_RE_LOGIN:	
					 System.out.println("Receving packet type: login");
					 Login l = this.handler.rLogin(dp);
					 Map<String, Login> loginInfo= new HashMap<String, Login>();
					 loginInfo.put(""+l.userid, l);
					 devRef = wdRootRef.child("login");
					 devRef.setValue(loginInfo);					 
					 this.sendGetCarInfoCmd(l.userid, "");
					 this.sendGetCarGroupCmd(l.userid, "");					
					 break;
				 case DPacketParser.SIGNAL_RE_HEARTBEAT:
					 System.out.println("heart beat");
					 break;
				 case DPacketParser.SIGNAL_RE_GETCARGROUP:
					 System.out.println("Receving packet type: carGroupInfo");
					 CarGroup[] carGroupList = this.handler.rGetCarGroup(dp);
					 Map<String, CarGroup> carGroup= new HashMap<String, CarGroup>();
					 for (int ii=0;ii<carGroupList.length;ii++){
						 carGroup.put(""+carGroupList[ii].vehGroupID, carGroupList[ii]);
					 }
					 devRef = wdRootRef.child("cargroup");
					 devRef.setValue(carGroup);
					 break;
				 case DPacketParser.SIGNAL_RE_GETUSERINFO:
					 System.out.println("Receving packet type: get user info");
					 this.handler.rGetUserInfo(dp);
					 break;
				 case DPacketParser.SIGNAL_RE_GETCARINFO:
					 System.out.println("Receving packet type: get car info");
					 Car[] carList=this.handler.rGetCarInfo(dp);
					 Map<String, Car> car= new HashMap<String, Car>();
					 for (int ii=0;ii<carList.length;ii++){
						 car.put(""+carList[ii].id, carList[ii]);
					 }
					 devRef = wdRootRef.child("car");
					 devRef.setValue(car);
					 
					 break;
				 case DPacketParser.SIGNAL_RE_GETCARTRACK:
					 System.out.println("Receving packet type: get car track");
					 Track[] curTrackList = this.handler.rGetCarTrack(dp);
					 Map<String, Track> track= new HashMap<String, Track>();
					 for (int ii=0;ii<curTrackList.length;ii++){
						 Track curTrackNode = curTrackList[ii];
						 
						 // GPS correct
						 double[] correctXY = new double[2];
 						 GpsCorrect.transform(curTrackNode.latitude, curTrackNode.longitude, correctXY);
 						 curTrackNode.latitude = correctXY[0];
 						 curTrackNode.longitude = correctXY[1];
 						 
						 track.put(""+ii, curTrackNode);						
					 }
					 devRef = wdRootRef.child("tracklist");
					 devRef.setValue(track);
					 break;
				 case DPacketParser.SIGNAL_RE_GETALARMLIST:
					 Alarm[] alarmList = MsgEventHandler.rGetAlarmList(dp);	
					 Map<String, Alarm> alarmSet= new HashMap<String, Alarm>();
					 for (int ii=0;ii<alarmList.length;ii++){
						 alarmSet.put(alarmList[ii].termid, alarmList[ii]);
					 }
					 devRef = wdRootRef.child("alarmlist");
					 devRef.setValue(alarmSet);
					 break;	
				 case DPacketParser.SIGNAL_FAIL:
					 System.out.println("Receving packet type: Fail");
					 Fail curFail = this.handler.rFail(dp);
					 devRef = wdRootRef.child("error");
					 Map<String, Fail> fail= new HashMap<String, Fail>();
					 fail.put("" + curFail.signal, curFail);
					 devRef.setValue(fail);
					 break;
				 }
		 }catch(Exception e ){
			throw e; 
		 }
	}
	
	// send login packet command
	 public void sendLoginCmd(String username, String password){
		 byte[] dataPacket = this.handler.sLogin(username, password);
		 this.sendPacket(dataPacket);
	 }
	 
	 public void sendHeartBeat(){
		 byte[] dataPacket = this.handler.sHeartBeat();
		 this.sendPacket(dataPacket);
	 }
	
	 public void sendGetCarGroupCmd(int userid,String date){
		 byte[] dataPacket = this.handler.sGetCarGroup(userid, date);
		 this.sendPacket(dataPacket);
	 }
	 
	 public void sendGetUserInfoCmd(int userid){
		 byte[] dataPacket = this.handler.sGetUserInfo(userid);
		 this.sendPacket(dataPacket);
	 }
	 
	 public void sendGetCarInfoCmd(int userid,String date){
		 byte[] dataPacket = this.handler.sGetCarInfo(userid, date);
		 this.sendPacket(dataPacket);
	 }
	 
	 public void sendGetCarTrackCmd(int carid,String sdate,String edate){
		 byte[] dataPacket = this.handler.sGetCarTrack(carid, sdate, edate);
		 this.sendPacket(dataPacket);
	 }
	 
	 public void sendGetAlarmListCmd(String carid,String sdate,String edate,String alarmType){
		 byte[] dataPacket = this.handler.sGetAlarmList(carid, sdate, edate, alarmType);
		 this.sendPacket(dataPacket);
	 }
	 
}
