package com.cloudbean.trackme;
import java.util.ArrayList;
import java.util.List;

import com.cloudbean.model.Alarm;
import com.cloudbean.model.Car;
import com.cloudbean.model.CarGroup;
import com.cloudbean.model.Fail;
import com.cloudbean.model.Login;
import com.cloudbean.model.Track;
import com.cloudbean.model.User;



public class TrackApp {
	public static User getUser() {
		return user;
	}
	public static void setUser(User user) {
		TrackApp.user = user;
	}
	public static Login getLogin() {
		return login;
	}
	public static void setLogin(Login login) {
		TrackApp.login = login;
	}
	public static Car[] getCarList() {
		return carList;
	}
	public static void setCarList(Car[] carList) {
		TrackApp.carList = carList;
	}
	public static CarGroup[] getCarGroupList() {
		return carGroupList;
	}
	public static void setCarGroupList(CarGroup[] carGroupList) {
		TrackApp.carGroupList = carGroupList;
	}
	public static Car getCurrentCar() {
		return currentCar;
	}
	public static void setCurrentCar(Car currentCar) {
		TrackApp.currentCar = currentCar;
	}
	public static String getCurUsername() {
		return curUsername;
	}
	public static void setCurUsername(String curUsername) {
		TrackApp.curUsername = curUsername;
	}
	public static String getCurPassword() {
		return curPassword;
	}
	public static void setCurPassword(String curPassword) {
		TrackApp.curPassword = curPassword;
	}
	public static boolean isLogin() {
		return isLogin;
	}
	public static void setLogin(boolean isLogin) {
		TrackApp.isLogin = isLogin;
	}
	public static List<Alarm> getAlarmList() {
		return alarmList;
	}
	public static void setAlarmList(List<Alarm> alarmList) {
		TrackApp.alarmList = alarmList;
	}
	public static String getdServerAddr() {
		return dServerAddr;
	}
	public static void setdServerAddr(String dServerAddr) {
		TrackApp.dServerAddr = dServerAddr;
	}
	public static String getcServerAddr() {
		return cServerAddr;
	}
	public static void setcServerAddr(String cServerAddr) {
		TrackApp.cServerAddr = cServerAddr;
	}
	public static User  user = null;
	public static Login  login = null;
	public static Car[] carList = null;
	public static CarGroup[] carGroupList=null;
	public static Car currentCar = null;
	public static String curUsername = null;
	public static String curUserid = null;
	public static Track[] curTrackList = null;
	
	public static Fail curFail = null;
	
	public static String getCurUserid() {
		return curUserid;
	}
	public static void setCurUserid(String curUserid) {
		TrackApp.curUserid = curUserid;
	}
	public static String curPassword = null;
	public static boolean isLogin = false;	
	public static List<Alarm> alarmList = new ArrayList<Alarm>();

	public static String dServerAddr  = "61.145.122.143:4519";
	public static String cServerAddr  = "61.145.122.143:4508";

}
