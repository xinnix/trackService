package com.cloudbean.model;


public class Car{
	
	public String id;	
	public String ipAddress;
	public String name;
	public String devId;
	public String devtype;
	public String carGroupId;
	public int alive = 0;
	public CarState lastState;	
	public CarState curState;
	
	
	
	public CarState getCurState() {
		return curState;
	}



	public void setCurState(CarState curState) {
		this.curState = curState;
	}
	
	public CarState getLastState() {
		return lastState;
	}



	public void setLastState(CarState lastState) {
		this.lastState = lastState;
	}



	public int getAlive() {
		return alive;
	}



	public void setAlive(int alive) {
		this.alive = alive;
	}



	public Car(String id, String ipAddress, String name, String devId, String devtype, String carGroupId) {
		super();
		this.id = id.trim();
		this.ipAddress = ipAddress.trim();
		this.name = name.trim();
		this.devId = devId.trim();
		this.devtype = devtype.trim();
		this.carGroupId = carGroupId.trim();
		this.alive = 0;
		this.lastState = null;
	}



	public Car() {
		// TODO Auto-generated constructor stub
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getIpAddress() {
		return ipAddress;
	}



	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDevId() {
		return devId;
	}



	public void setDevId(String devId) {
		this.devId = devId;
	}



	public String getDevtype() {
		return devtype;
	}



	public void setDevtype(String devtype) {
		this.devtype = devtype;
	}



	public String getCarGroupId() {
		return carGroupId;
	}



	public void setCarGroupId(String carGroupId) {
		this.carGroupId = carGroupId;
	}

	

}
