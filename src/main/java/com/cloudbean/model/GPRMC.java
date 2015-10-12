package com.cloudbean.model;
import java.text.DecimalFormat;

public class GPRMC {
		public String utc;
		public String locateState;
		public double latitude;
		public String NorS;
		public double longitude;
		public String EorW;
		public String speed;
		public String direction;
		public String date;
		public String declination;
		public String mDirection;
		public String separator;
		public String check;
		
		public GPRMC(String orgString) {
			String org[] = orgString.split(",");
				if(org.length >= 12) {
					
				this.utc = org[0];
				this.locateState = org[1];
				if(this.locateState.equals("A")){
					this.latitude = decodeLat(org[2]);
					this.NorS = org[3];
					this.longitude = decodeLon(org[4]);
					this.EorW = org[5];
					this.speed = decodeSpeed(org[6]);
				}else{
					this.latitude = 0;
					this.NorS = "";
					this.longitude = 0;
					this.EorW = "";
					this.speed = "0";
				}
				
				this.direction = org[7];
				this.date = decodeDate(org[8]);
				this.declination = org[9];
				this.mDirection = org[10];
				this.separator = org[11].substring(0,0);
				this.check = org[11].substring(1, 2);
			}

		}

		private double decodeLat(String lat){
			int a = Integer.parseInt(lat.substring(0, 2));
			double b = Double.parseDouble(lat.substring(2, lat.length()))/60;
			return a+b;
		}
		
		private double decodeLon(String lat){
			int a = Integer.parseInt(lat.substring(0, 3));
			double b = Double.parseDouble(lat.substring(3, lat.length()))/60;
			return a+b;
		}		
		
		private String decodeDate(String date){
			String y = date.substring(4);
			String m = date.substring(2, 4);
			String d = date.substring(0, 2);
			return y+"年"+m+"月"+d+"日";
		}
		
		private String decodeSpeed(String speed){
			Double v = Double.parseDouble(speed)*1.852;
			v = v<6?0:v;//速度小于6km过滤
			DecimalFormat formatter = new DecimalFormat("##0.00");
			String ns = formatter.format(v);			
			return ns;
		}
	}