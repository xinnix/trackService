package com.cloudbean.model;


public class Alarm {
	
	public String devid;	
	public String alarmTime;
	public String alarmType;

	
	public Alarm(String termid, String alarmTime, String alarmType) {
		super();
		this.devid = termid;
		this.alarmTime = alarmTime.trim();
		this.alarmType = alarmType.trim();

	}
	
	
	public Alarm() {
		// TODO Auto-generated constructor stub
	}


	public String getTermid() {
		return devid;
	}


	public void setTermid(String termid) {
		this.devid = termid;
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


}
