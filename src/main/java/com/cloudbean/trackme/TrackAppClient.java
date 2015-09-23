package com.cloudbean.trackme;

import com.cloudbean.network.NetworkAdapter;
import com.cloudbean.network.CNetworkAdapter;
import com.cloudbean.network.HeartBeat;
import com.cloudbean.network.MsgEventHandler;
import com.wilddog.client.Wilddog;
import com.cloudbean.model.*;

public class TrackAppClient {
	private MsgEventHandler handler  ; 
	private Wilddog wdRootRef;
	private User  user = null;
	private Login  login = null;
	private Car[] carList = null;
	private CarGroup[] carGroupList=null;
	private Car currentCar = null;
	private String curUsername = null;
	private String curPassword = null;
	private String curUserid = null;
	private Track[] curTrackList = null;
	private Fail curFail = null;
	private NetworkAdapter na ;
	private CNetworkAdapter cna ;
	private HeartBeat hb;
	public static String dServerIP = "61.145.122.143";
	public static int dServerPort = 4519;
	public static String cServerIP  = "61.145.122.143";
	public static int cServerPort  = 4508;
	
	public TrackAppClient(String name){
		this.handler = new MsgEventHandler();
		na = new NetworkAdapter(dServerIP, dServerPort);
		cna = new CNetworkAdapter(cServerIP, cServerPort);
		hb = new HeartBeat();
		hb.setHandler(this.handler);
		this.handler.config(na, cna);
		this.wdRootRef = new Wilddog("https://track-translator.wilddogio.com/" + name);
	}
	
	public MsgEventHandler getHandler() {
		return handler;
	}

	public Wilddog getWdRootRef() {
		return wdRootRef;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public Car[] getCarList() {
		return carList;
	}

	public void setCarList(Car[] carList) {
		this.carList = carList;
	}

	public CarGroup[] getCarGroupList() {
		return carGroupList;
	}

	public void setCarGroupList(CarGroup[] carGroupList) {
		this.carGroupList = carGroupList;
	}

	public Car getCurrentCar() {
		return currentCar;
	}

	public void setCurrentCar(Car currentCar) {
		this.currentCar = currentCar;
	}

	public String getCurUsername() {
		return curUsername;
	}

	public void setCurUsername(String curUsername) {
		this.curUsername = curUsername;
	}

	public String getCurPassword() {
		return this.curPassword;
	}

	public void setCurPassword(String curPasswd) {
		this.curPassword = curPasswd;
	}

	public String getCurUserid() {
		return curUserid;
	}

	public void setCurUserid(String curUserid) {
		this.curUserid = curUserid;
	}

	public Track[] getCurTrackList() {
		return curTrackList;
	}

	public void setCurTrackList(Track[] curTrackList) {
		this.curTrackList = curTrackList;
	}

	public Fail getCurFail() {
		return curFail;
	}

	public void setCurFail(Fail curFail) {
		this.curFail = curFail;
	}

	
	
	public void login(String username, String password){
		// run a thread to create socket
		handler.sLogin(username, password);
	}
	
	public void logout(String username){
		// handler.sLogout();
	}
	
	
	
}
