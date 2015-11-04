package com.cloudbean.network;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cloudbean.model.Alarm;
import com.cloudbean.model.Car;
import com.cloudbean.model.CarGroup;
import com.cloudbean.model.CarState;
import com.cloudbean.model.Fail;
import com.cloudbean.model.GPRMC;
import com.cloudbean.model.Login;
import com.cloudbean.model.Track;
import com.cloudbean.model.User;
import com.cloudbean.packet.CPacketParser;
import com.cloudbean.packet.DPacketParser;
import com.cloudbean.packet.MsgGPRSParser;
import com.cloudbean.trackerUtil.ByteHexUtil;

public class MsgEventHandler {	
	
	
	public static Map<Integer, String> alarmMap = new HashMap<Integer,String>(){{
		put(0x64,"非法点火报警");
		put(0x11,"超速报警");
		put(0x12,"出围栏报警");
		put(0x50,"掉电报警");
		put(0x12,"移动报警");
		put(0x14,"终端开机报警");
		put(0x01,"SOS报警");
		put(0x10,"内置电池低电压报警");
		put(0x03,"接触成功");
		put(0x33,"脱落报警");
		put(0x66,"长时间停留报警");
	}};		

	public byte[] sLogin(String username,String password){
		int[] pktDataColumnType  = {DPacketParser.DATA_TYPE_STRING,DPacketParser.DATA_TYPE_STRING};
		int[] pktDataColumnLength = {username.length()*2,password.length()*2};
		byte[] pktData = new byte[username.length()*2+password.length()*2];
		
		byte[] busername=username.getBytes();
		byte[] bpassword=password.getBytes();
		
		System.arraycopy(busername, 0, pktData, 0, busername.length);
		System.arraycopy(bpassword, 0, pktData, busername.length*2, bpassword.length);
		DPacketParser dp = new DPacketParser(DPacketParser.SIGNAL_LOGIN,1,2,pktDataColumnType, pktDataColumnLength, pktData);	
		return dp.pktBuffer;		
	}
	
	public Login rLogin(DPacketParser dp){
		String res = ByteHexUtil.bytesToHexString(dp.pktBuffer);
		Login l =new Login((Integer) (dp.dataTable.table[0][0]),
				(Integer) (dp.dataTable.table[0][1]),
				(String) (dp.dataTable.table[0][2]),
				(String) (dp.dataTable.table[0][3]),
				(Integer) (dp.dataTable.table[0][4]));	
		return l;	
	}	
	
	public Fail rFail(DPacketParser dp){		
		Fail f = new Fail((Integer)dp.dataTable.table[0][0],(String)dp.dataTable.table[0][1]);
		return f;		
	}
	
	public byte[] sHeartBeat(){
		int[] pktDataColumnType  = {DPacketParser.DATA_TYPE_INTEGER};
		int[] pktDataColumnLength = {4};
		byte[] pktData = new byte[4];
		DPacketParser dp = new DPacketParser(DPacketParser.SIGNAL_HEARTBEAT,1,1,pktDataColumnType, pktDataColumnLength, pktData);	
		return dp.pktBuffer;		
	}
	
	public byte[] sGetCarGroup(int userid,String date){
		int[] pktDataColumnType  = {DPacketParser.DATA_TYPE_INTEGER,DPacketParser.DATA_TYPE_STRING};
		int[] pktDataColumnLength = {4,date.length()*2};
		byte[] pktData = new byte[4+date.length()*2];		
		byte[] buserid = ByteHexUtil.intToByte(userid);
		byte[] bdate = date.getBytes();
		
		System.arraycopy(buserid, 0, pktData, 0, buserid.length);
		System.arraycopy(bdate, 0, pktData, buserid.length, bdate.length*2);
		
		DPacketParser dp = new DPacketParser(DPacketParser.SIGNAL_GETCARGROUP,1,2,pktDataColumnType, pktDataColumnLength, pktData);	
		return dp.pktBuffer;		
	}
	
	public CarGroup[] rGetCarGroup(DPacketParser dp){		 
		CarGroup[] cg = new CarGroup[dp.dataTable.table.length];
		for (int ii=0;ii<cg.length;ii++){
			cg[ii] = new CarGroup((Integer)dp.dataTable.table[ii][0],
					(String)dp.dataTable.table[ii][1],
					(String)dp.dataTable.table[ii][2],
					(String)dp.dataTable.table[ii][3],
					(String)dp.dataTable.table[ii][4],
					(String)dp.dataTable.table[ii][5],
					(Integer)dp.dataTable.table[ii][6],
					(String)dp.dataTable.table[ii][7]);
		}		
		
		for (int ii=0;ii<cg.length;ii++){
			System.out.print(""+cg[ii].vehGroupID+'#'+cg[ii].vehGroupName.trim()+'|'+cg[ii].updateTime);
			System.out.println("");	
		}		
		return cg;		
	}
	
	public byte[] sGetUserInfo(int userid){
		int[] pktDataColumnType  = {DPacketParser.DATA_TYPE_INTEGER};
		int[] pktDataColumnLength = {4};
		byte[] pktData = new byte[4];		
		byte[] buserid = ByteHexUtil.intToByte(userid);		
		
		System.arraycopy(buserid, 0, pktData, 0, buserid.length);		
		
		DPacketParser dp = new DPacketParser(DPacketParser.SIGNAL_GETUSERINFO,1,pktDataColumnType.length,pktDataColumnType, pktDataColumnLength, pktData);	
		return dp.pktBuffer;		
	}
	
	public User rGetUserInfo(DPacketParser dp){		
		User[] u = new User[dp.dataTable.table.length];
		for (int ii=0;ii<u.length;ii++){
			u[ii] = new User((Integer)dp.dataTable.table[ii][0],
					(String)dp.dataTable.table[ii][1],
					(String)dp.dataTable.table[ii][2],
					(Integer)dp.dataTable.table[ii][3],
					(String)dp.dataTable.table[ii][4],
					(String)dp.dataTable.table[ii][5],
					(String)dp.dataTable.table[ii][6],
					(String)dp.dataTable.table[ii][7],
					(String)dp.dataTable.table[ii][8],
					(String)dp.dataTable.table[ii][9],
					(Integer)dp.dataTable.table[ii][10],
					(String)dp.dataTable.table[ii][11]);
		}
				
		for (int ii=0;ii<u.length;ii++){
			System.out.print(""+u[ii].username+'#'+u[ii].password.trim()+'|'+u[ii].birthday);
			System.out.println("");	
		}		
		return u[0];		
	}
	
	
	public byte[] sGetCarInfo(int userid,String date){
		int[] pktDataColumnType  = {DPacketParser.DATA_TYPE_INTEGER,DPacketParser.DATA_TYPE_STRING};
		int[] pktDataColumnLength = {4,date.length()*2};
		byte[] pktData = new byte[4+date.length()*2];		
		byte[] buserid = ByteHexUtil.intToByte(userid);
		byte[] bdate = date.getBytes();
		
		System.arraycopy(buserid, 0, pktData, 0, buserid.length);
		System.arraycopy(bdate, 0, pktData, buserid.length, bdate.length*2);
		
		DPacketParser dp = new DPacketParser(DPacketParser.SIGNAL_GETCARINFO,1,2,pktDataColumnType, pktDataColumnLength, pktData);	
		return dp.pktBuffer;		
	}
	
	
	public Car[] rGetCarInfo(DPacketParser dp){		 
		Car[] cars = new Car[dp.dataTable.table.length];
		for (int ii=0;ii<cars.length;ii++){
			cars[ii] = new Car((String)dp.dataTable.table[ii][0],
					(String)dp.dataTable.table[ii][3],
					(String)dp.dataTable.table[ii][4],
					(String)dp.dataTable.table[ii][8],
					(String)dp.dataTable.table[ii][14],
					(String)dp.dataTable.table[ii][64]
					);
		}
		return cars;
	}
	
	
	public byte[] sGetCarTrack(int carid,String sdate,String edate){
		int[] pktDataColumnType  = {DPacketParser.DATA_TYPE_INTEGER,DPacketParser.DATA_TYPE_STRING,DPacketParser.DATA_TYPE_STRING};
		int[] pktDataColumnLength = {4,sdate.length()*2,edate.length()*2};
		byte[] pktData = new byte[4+sdate.length()*2+edate.length()*2];		
		byte[] bcarid =  ByteHexUtil.intToByte(carid);
		byte[] bsdate=sdate.getBytes();
		byte[] bedate=edate.getBytes();		
		
		System.arraycopy(bcarid, 0, pktData, 0, bcarid.length);
		System.arraycopy(bsdate, 0, pktData, bcarid.length, bsdate.length);
		System.arraycopy(bedate, 0, pktData, bcarid.length+bsdate.length*2, bedate.length);
		DPacketParser dp = new DPacketParser(DPacketParser.SIGNAL_GETCARTRACK,1,3,pktDataColumnType, pktDataColumnLength, pktData);	
		return dp.pktBuffer;		
	}
	
	public Track[] rGetCarTrack(DPacketParser dp){	
		Track[] t = new Track[dp.dataTable.table.length];

		for (int ii=0;ii<t.length;ii++){
			t[ii] = new Track((Integer)dp.dataTable.table[ii][0],
					(Double)dp.dataTable.table[ii][1],
					(Double)dp.dataTable.table[ii][2],
					(Integer)dp.dataTable.table[ii][3],
					(Integer)dp.dataTable.table[ii][4],
					(Boolean)dp.dataTable.table[ii][5],
					(String)dp.dataTable.table[ii][6],
					(String)dp.dataTable.table[ii][7],
					(Boolean)dp.dataTable.table[ii][8],
					(String)dp.dataTable.table[ii][9]
					);
		}
			
		return t;
	}
	
	public byte[] sGetAlarmList(String carid,String startdate,String enddate,String alarmType){
		int[] pktDataColumnType  = {DPacketParser.DATA_TYPE_STRING,DPacketParser.DATA_TYPE_STRING,DPacketParser.DATA_TYPE_STRING,DPacketParser.DATA_TYPE_STRING};
		int[] pktDataColumnLength = {startdate.length()*2,enddate.length()*2,carid.length()*2,alarmType.length()*2};
		byte[] pktData = new byte[startdate.length()*2+enddate.length()*2+carid.length()*2+alarmType.length()*2];
		
		byte[] bstartdate = startdate.getBytes();
		byte[] benddate = enddate.getBytes();
		byte[] bcarid = carid.getBytes();
		byte[] balarmtype = alarmType.getBytes();
		
		System.arraycopy(bstartdate, 0, pktData, 0, bstartdate.length);
		System.arraycopy(benddate, 0, pktData, bstartdate.length*2,benddate.length);
		System.arraycopy(bcarid, 0, pktData,bstartdate.length*2+benddate.length*2 , bcarid.length);
		System.arraycopy(balarmtype, 0, pktData,bstartdate.length*2+benddate.length*2+bcarid.length*2 , balarmtype.length);
				
		DPacketParser dp = new DPacketParser(DPacketParser.SIGNAL_GETALARMLIST,1,4,pktDataColumnType, pktDataColumnLength, pktData);
		return dp.pktBuffer;		
	}

	
	/*
	 * 以下是中心报文相关控制函数
	 */
	
	
	public byte[] c_sLogin(String username,String password){
		byte signal = (byte)0xa3;
		int fakeip = 0;
		byte[] busername = username.getBytes();
		byte[] bpassword = password.getBytes();
		byte[] data = new byte[40];
		for (int ii=0;ii<data.length;ii++){
			data[ii]=(byte)0x20;
		}
		
		System.arraycopy(busername, 0, data, 0, busername.length);
		System.arraycopy(bpassword, 0, data, 20, bpassword.length);
		
		CPacketParser cp = new CPacketParser(signal,fakeip, data);
		return cp.pktBuffer;			
	}
	
	public int c_rLogin(CPacketParser cp){
		System.out.println("clogin:"+ByteHexUtil.bytesToHexString(cp.pktBuffer));
		byte sig = ByteHexUtil.intToByte(cp.pktFakeIP)[0];
		
		if (sig==(byte)0x01){
			System.out.println("login complete");
			//c_sGetAllCarPosition();
			return 0;
		}else{
			return 1;
		}	
	}
	
	
	public byte[] c_sGetAllLastPosition(){
		// this.cna.sendPacket(this.bis.toByteArray());
		byte signal = (byte)0xa4;
		int fakeip = 0;
		byte[] data = null;		
		CPacketParser cp = new CPacketParser(signal,fakeip, data);		
		return cp.pktBuffer;		
	}
	
	
	public CarState c_rGetAllCarPosition(CPacketParser cp) {
		MsgGPRSParser mgp =  new MsgGPRSParser(Arrays.copyOfRange(cp.pktData, 4, cp.pktData.length));
		CarState cs = new CarState(mgp.msgData);
		
		return cs;		
	}
	
	
	public void c_sGetCarPosition(Car car){		
		// String res = c_sCommand(car,MsgGPRSParser.MSG_TYPE_GETPOSITION,"");			
	}
	
	public static Alarm[] rGetAlarmList(DPacketParser dp){
		 
		Alarm[] al = new Alarm[dp.dataTable.table.length];
		for (int ii=0;ii<al.length;ii++){
			al[ii] = new Alarm((Integer)dp.dataTable.table[ii][0],
					(String)dp.dataTable.table[ii][1],
					(String)dp.dataTable.table[ii][2]
					);
		}

		return al;
		
	}
	public CarState c_rGetCarPosition(MsgGPRSParser mgp){
		CarState cs = new CarState(mgp.msgData);
		int i  = mgp.msgTermID.indexOf("f");		
		
		if(i!=0){
			cs.setDevid(mgp.msgTermID.substring(0, i));
		}else{
			cs.setDevid(mgp.msgTermID);
		}
				
		return cs;			
	}
	
	public GPRMC c_rParseGPRMC(MsgGPRSParser mgp){
		String[] org = mgp.msgData.split("\\|");
		GPRMC gprmc = new GPRMC(org[0]);
		return gprmc;
	}
	
	public Alarm c_rGetAlarmInfo(MsgGPRSParser mgp){		
		int alarmType = ByteHexUtil.hexStringToBytes(mgp.msgData.substring(0, 2))[0];
		String alarm = alarmMap.get(alarmType);
		if(alarm==null){
			alarm = "未知报警类型";
		}
		Alarm al = new Alarm(mgp.msgTermID, alarm);
		return al;
	}

	
	public byte[] c_sSetDef(String devid, String ipAddress,String data){		
		return c_sCommand(devid, ipAddress, MsgGPRSParser.MSG_TYPE_DEF,data);
			
	}
	public byte[] c_sSetCircuit(String devid, String ipAddress,String data){	
		return c_sCommand(devid, ipAddress, MsgGPRSParser.MSG_TYPE_CIRCUIT,data);
	}
	
	public byte[] c_sCommand(String devid, String ipAddress, short commandType,String data){
		// String devid = car.devId;
		for(int i = (14-devid.length());i>0;i--){
			devid=devid.concat("f");
		}
		int fakeip = ByteHexUtil.bytesToInt(ipToBytesByReg(ipAddress));
		ByteArrayOutputStream bis = new ByteArrayOutputStream();
		bis.write(0x0b);
		MsgGPRSParser mgp = new MsgGPRSParser(devid, commandType, data);
		try{
			bis.write(mgp.msgByteBuf);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		CPacketParser cp = new CPacketParser(CPacketParser.SIGNAL_RELAY, fakeip, bis.toByteArray());
		// String test = ByteHexUtil.bytesToHexString(cp.pktBuffer);
		
		return cp.pktBuffer;
		
	}
	

	public byte[] c_sSetPhone(String devid, String ipAddress,String data){			
		return c_sCommand(devid, ipAddress, MsgGPRSParser.MSG_TYPE_PHONE, data);		
	}
	
	public byte[] c_sGPSReboot(String devid, String ipAddress,String data){	
		
		return c_sCommand(devid, ipAddress, MsgGPRSParser.MSG_TYPE_GPSREBOOT,data);
		
	}
	
	public   byte[] c_sExpandCommand(String devid, String ipAddress,String data){	
		
		return c_sCommand(devid, ipAddress, MsgGPRSParser.MSG_TYPE_EXPANDCOMMAND,data);
		
	}
	
	public  byte[] c_sGPSHeartBeat(String devid, String ipAddress,String data){	
		
		return c_sCommand(devid, ipAddress, MsgGPRSParser.MSG_TYPE_GPSHEARTBEAT,data);
		
	}
	
	public  byte[] c_sTraceInterval(String devid, String ipAddress,String data){	
		
		return c_sCommand(devid, ipAddress, MsgGPRSParser.MSG_TYPE_GPSHEARTBEAT,data);
		
	}
	
	/**
     * 把IP地址转化为int
     * @param ipAddr
     * @return int
     */
    private static byte[] ipToBytesByReg(String ipAddr) {
        byte[] ret = new byte[4];
        try {
            String[] ipArr = ipAddr.split("\\.");
            ret[0] = (byte) (Integer.parseInt(ipArr[0]) & 0xFF);
            ret[1] = (byte) (Integer.parseInt(ipArr[1]) & 0xFF);
            ret[2] = (byte) (Integer.parseInt(ipArr[2]) & 0xFF);
            ret[3] = (byte) (Integer.parseInt(ipArr[3]) & 0xFF);
            return ret;
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }

    }

}
