package com.cloudbean.model;



public class Login  {
	
	
	public static int LOGIN_SUCCESS=1;
	public static int LOGIN_FAILURE=0;

	public int isLogin;
	public int userid;
	public String gprsIP;
	public String gprsPort;
	
	public int getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(int isLogin) {
		this.isLogin = isLogin;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getGprsIP() {
		return gprsIP;
	}

	public void setGprsIP(String gprsIP) {
		this.gprsIP = gprsIP;
	}

	public String getGprsPort() {
		return gprsPort;
	}

	public void setGprsPort(String gprsPort) {
		this.gprsPort = gprsPort;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int userType;
	
	public Login(){
		
	}
	
	public Login(int isLogin, int userid, String gprsIP, String gprsPort,  int userType) {
		super();
		this.isLogin = isLogin;
		this.userid = userid;
		this.gprsIP = gprsIP.trim();
		this.gprsPort = gprsPort.trim();
		this.userType = userType;
	}

	
}
