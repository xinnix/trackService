package com.cloudbean.model;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Alarm {
	
	public String termid;	
	public String alarmTime;
	public String alarmType;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public Alarm(String termid,String alarmType) {
		this.termid = decodeDevId(termid);		
		this.alarmTime = format.format(new Date());
		this.alarmType = alarmType.trim();
	}
	public Alarm(String termid, String alarmTime, String alarmType) {
		super();
		this.termid = decodeDevId(termid);
		this.alarmTime = alarmTime.trim();
		this.alarmType = alarmType.trim();

	}
	
	public Alarm(int carid, String alarmTime, String alarmType) {
		super();
		this.termid = decodeDevId(""+carid);
		this.alarmTime = alarmTime.trim();
		this.alarmType = alarmType.trim();

	}
	
	public Alarm(int fakeip, String alarmType) {
		super();		
		this.alarmTime = format.format(new Date());
	}
	
	public Alarm() {
		// TODO Auto-generated constructor stub
	}


	public String getTermid() {
		return termid;
	}


	public void setTermid(String termid) {
		this.termid = termid;
	}


	public String getAlarmTime() {
		return alarmTime;
	}


	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}


	public String getAlarmType() {
		return alarmType;
	}


	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	
	private String decodeDevId(String devid){
		if(devid.indexOf("f")>0){
			return devid.substring(0, devid.indexOf("f"));
		}
		return devid;
	}
	
	

}
