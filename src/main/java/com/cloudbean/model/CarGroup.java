package com.cloudbean.model;



public class CarGroup{
	
	public int vehGroupID;
	public String vehGroupName;
	public String contact;
	public String sTel1;
	public String sTel2;
	public String address;
	public int fVehGroupID;
	public String updateTime;
	
	public CarGroup(int vehGroupID, String vehGroupName, String contact, String sTel1, String sTel2, String address,
			int fVehGroupID, String updateTime) {
		super();
		this.vehGroupID = vehGroupID;
		this.vehGroupName = vehGroupName;
		this.contact = contact;
		this.sTel1 = sTel1;
		this.sTel2 = sTel2;
		this.address = address;
		this.fVehGroupID = fVehGroupID;
		this.updateTime = updateTime;
	}

	public CarGroup() {
		// TODO Auto-generated constructor stub
	}

	
}
