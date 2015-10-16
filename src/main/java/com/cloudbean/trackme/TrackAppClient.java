package com.cloudbean.trackme;

import java.io.IOException;

import com.cloudbean.network.NetworkAdapter;
import com.cloudbean.network.CNetworkAdapter;
import com.wilddog.client.Wilddog;
import com.cloudbean.model.*;

public class TrackAppClient { 
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
	private NetworkAdapter na;
	private CNetworkAdapter cna;
	private String sessionID;
	private int connectedConut;
	
	public String getSessionID() {
		return sessionID;
	}

	public int getConnectedConut() {
		return connectedConut;
	}

	public void setConnectedConut(int connectedConut) {
		this.connectedConut = connectedConut;
	}
	
	// connect number + 1
	public void incConnectedConut() {
		this.connectedConut = this.connectedConut + 1;
		System.out.println("[conn]" + this.curUsername + " connected with " + this.connectedConut + " instaces.");
	}
	
	// connect number - 1
	public void decConnectedConut() {
		this.connectedConut = this.connectedConut - 1;
		System.out.println("[exit]" + this.curUsername + "connected with " + this.connectedConut + " instaces.");
		
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public static String dServerIP = "61.145.122.143";
	public static int dServerPort = 4519;
	public static String cServerIP  = "61.145.122.143";
	public static int cServerPort  = 4508;
	
	public TrackAppClient(String name){
		this.connectedConut = 0;
		this.curUsername = name;
		this.na = new NetworkAdapter(dServerIP, dServerPort);
		this.cna= new CNetworkAdapter(cServerIP, cServerPort);	
		this.wdRootRef = new Wilddog("https://track-translator.wilddogio.com/" + name);	
		this.na.config(name, this.wdRootRef);
		this.cna.config(name, this.wdRootRef);		
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
		this.na.sendLoginCmd(username, password);
	}
	
	public void logout(String username){
		// handler.sLogout();
	}

	public NetworkAdapter getNa() {
		return na;
	}

	public void setNa(NetworkAdapter na) {
		this.na = na;
	}

	public CNetworkAdapter getCna() {
		return cna;
	}

	public void setCna(CNetworkAdapter cna) {
		this.cna = cna;
	}
	
	public void stopSocketConnect(){
		this.na.getConnectThread().interrupt();
		this.cna.getConnectThread().interrupt();
		
		System.out.println("closing na and cna socket.");
		// closing na and cna socket.
		try {
			this.na.getSocket().close();
			this.cna.getSocket().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
