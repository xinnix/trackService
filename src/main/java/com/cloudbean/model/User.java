package com.cloudbean.model;

public class User {
	
	public int userId;
	public String username;
	public String password;
	public int userTypeId;
	public String ownerName;
	public String sTel;
	public String sMemo;
	public String timeLimit;
	public String birthday;
	public String signLimit;
	public int userGroupid;
	public String menuLimit;
	
	public User(int userId, String username, String password, int userTypeId, String ownerName, String sTel,
			String sMemo, String timeLimit, String birthday, String signLimit, int userGroupid, String menuLimit) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.userTypeId = userTypeId;
		this.ownerName = ownerName;
		this.sTel = sTel;
		this.sMemo = sMemo;
		this.timeLimit = timeLimit;
		this.birthday = birthday;
		this.signLimit = signLimit;
		this.userGroupid = userGroupid;
		this.menuLimit = menuLimit;
	}
}
