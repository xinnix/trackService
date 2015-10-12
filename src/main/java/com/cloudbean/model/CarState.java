package com.cloudbean.model;

import java.text.DecimalFormat;

import com.cloudbean.trackerUtil.ByteHexUtil;

public class CarState {
	public String devid;

	public String posAccuracy;
	public String height;
	public String portState;
	public String analogInput;
	public String temperature;
	public String baseStation;
	public String gsmStrength;
	public String distant;
	public String voltage;
	//public GPRMC gprmc ;
	public void setDevid(String devid){
		

		this.devid = devid;
	}
	public String getDevid(){
		return this.devid;
	}
	
	public CarState(String orgString){
		String[] org = orgString.split("\\|");
		// this.gprmc = new GPRMC(org[0]);
		this.posAccuracy = org[1];
		this.height	= org[2];
		this.portState	= new String(ByteHexUtil.hexStringToBytes(org[3].trim()));
		this.analogInput  = org[4];
		this.temperature = ""+decodeTemp(this.analogInput);			
		this.baseStation = org[5];
		this.gsmStrength = org[6];
		this.distant = decodeDistant(org[7]);
		this.voltage = decodeVoltage(org[8]);
				
	}
	
	
	private double decodeTemp(String analogInput){
		String[] tmp =  analogInput.split(",");
		String res;
		if (tmp[1].length()<3){
			res = "00"+tmp[1].substring(tmp[1].length()-2);
		}else{
			res = tmp[1];
		}
		short ress = ByteHexUtil.byteToShort(ByteHexUtil.hexStringToBytes(res));
		return ress;
	}
	private String decodeVoltage(String voltage){
		byte[] voltageByte = ByteHexUtil.hexStringToBytes(voltage); 
		int voltageInt  = ByteHexUtil.byteToShort(voltageByte);
		double res = (voltageInt*3.2*16)/4096;
		DecimalFormat formatter = new DecimalFormat("##0.0");
		
		return formatter.format(res);
		
	}
	private String decodeDistant(String dis){
		return ""+ByteHexUtil.bytesToInt(ByteHexUtil.hexStringToBytes(dis))/1000;
		 
	}
	
	
	
	

}
