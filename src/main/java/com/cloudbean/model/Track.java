package com.cloudbean.model;



public class Track{
	public static String ACC_START = "ACC¿ª";
	public static String ACC_SHUTDOWN = "ACC¹Ø";
	
	public int carId;
	public double longitude;
	public double latitude;
	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isAlarm() {
		return alarm;
	}

	public void setAlarm(boolean alarm) {
		this.alarm = alarm;
	}

	public String getDistant() {
		return distant;
	}

	public void setDistant(String distant) {
		this.distant = distant;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isLocated() {
		return isLocated;
	}

	public void setLocated(boolean isLocated) {
		this.isLocated = isLocated;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public int direction;
	public int speed;
	public boolean alarm;
	public String distant;
	public String status;
	public boolean isLocated;
	public String sdate;
	
	public Track(int carId, double longitude, double latitude, int direction, int speed, boolean alarm, String distant,
			String status, boolean isLocated, String date) {
		super();
		this.carId = carId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.direction = direction;
		this.speed = speed;
		this.alarm = alarm;
		this.distant = distant.trim();
		this.status = status.trim();
		this.isLocated = isLocated;
		this.sdate = date.trim();
	}

	public Track() {
		// TODO Auto-generated constructor stub
	}

	

}
