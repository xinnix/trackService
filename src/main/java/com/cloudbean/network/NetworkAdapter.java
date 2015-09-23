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
		// TODO Auto-generated method stub

		 try{
			 
			 byte[] packetByte  = preParser();
				 
				 DPacketParser dp = new DPacketParser(packetByte);
				 switch (dp.pktSignal){
				 case DPacketParser.SIGNAL_RE_LOGIN:			 
					 Login l = this.handler.rLogin(dp);
					 // todo: pass to TrackApp
//					 TrackApp.curUserid = ""+l.userid;
//					 TrackApp.login = l;
					 this.handler.sGetCarInfo(l.userid,"");
					 this.handler.sGetCarGroup(l.userid,"");
					 this.handler.c_sGetAllLastPosition();
					 break;
				 case DPacketParser.SIGNAL_RE_HEARTBEAT:
					 System.out.println("heart beat");
					 break;
				 case DPacketParser.SIGNAL_RE_GETCARGROUP:
					 CarGroup[] carGroupList = this.handler.rGetCarGroup(dp);
					 // todo: pass to trackApp
//					 TrackApp.carGroupList = carGroupList;
					 Map<String, CarGroup> carGroup= new HashMap<String, CarGroup>();
					 for (int ii=0;ii<carGroupList.length;ii++){
						 carGroup.put(""+carGroupList[ii].vehGroupID, carGroupList[ii]);
					 }
					 devRef = wdRootRef.child("cargroup");
					 devRef.setValue(carGroup);
					 break;
				 case DPacketParser.SIGNAL_RE_GETUSERINFO:
					 this.handler.rGetUserInfo(dp);
					 break;
				 case DPacketParser.SIGNAL_RE_GETCARINFO:
					 Car[] carList=this.handler.rGetCarInfo(dp);
					 // todo: pass to TrackApp
//					 TrackApp.carList = carList;
//					 TrackApp.currentCar = carList[0];
					 Map<String, Car> car= new HashMap<String, Car>();
					 for (int ii=0;ii<carList.length;ii++){
						 car.put(""+carList[ii].id, carList[ii]);
					 }
					 devRef = wdRootRef.child("car");
					 devRef.setValue(car);
					 
					 break;
				 case DPacketParser.SIGNAL_RE_GETCARTRACK:
					 //todo: pass to TrackApp
					 // TrackApp.curTrackList = this.handler.rGetCarTrack(dp);
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
//				 case DPacketParser.SIGNAL_RE_GETALARMLIST:
//					 Alarm[] alarmList = MsgEventHandler.rGetAlarmList(dp);	
//					 msg = TrackApp.curHandler.obtainMessage(); 
//					 bundle = new Bundle();
//					 bundle.putParcelableArray("alarmlist", alarmList);
//					 msg.setData(bundle);
//					 msg.what = MSG_ALARM;
//					 TrackApp.curHandler.sendMessage(msg);
//					 break;	
				 case DPacketParser.SIGNAL_FAIL:
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
	
	
	
	

}
