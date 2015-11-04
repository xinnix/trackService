package com.cloudbean.network;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.cloudbean.trackme.TrackAppClient;
import com.wilddog.client.Wilddog;

public abstract class BaseNetworkAdapter extends Thread{
	public MsgEventHandler handler;
	protected String username;
	public String tmpPassword;
	protected String password;
	private  Socket socket;
	private  OutputStream outputStream;
	public  InputStream inputStream;
	public  Wilddog wdRootRef;	
	public  DataInputStream dis; 
	public byte[] sendBuffer;
	public byte[] recieveBuffer = new byte[20000];	
	private String serverIP = null;
	private int port = 0;
	public TrackAppClient appClient ;
	private Thread connectThread;
	boolean isLoginValid = false;
	
	public String getUsername() {
		return username;
	}

	public void config(String name, Wilddog ref){
		this.username = name;
		this.wdRootRef = ref;		
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setWdRootRef(Wilddog ref){
		this.wdRootRef = ref;
	}
	
	

	public BaseNetworkAdapter(String serverIP, int port) {
		super();
		this.serverIP = serverIP;
		this.port = port;
		this.handler = new MsgEventHandler();
	}

	public BaseNetworkAdapter(byte[] packet){
		super();
		this.sendBuffer= packet;
	}

	public void connect(){
		this.connectThread = new Thread () {
			public void run(){				
				try{
					while(!isInterrupted()){
						try{
							socket = new Socket(InetAddress.getByName(serverIP),port);
							outputStream = socket.getOutputStream();
							inputStream = socket.getInputStream();
							dis =  new DataInputStream((new BufferedInputStream(inputStream)));
							
							System.out.println( getUsername() +" login valid result is " + isLoginValid);
							if (isLoginValid) {
								reLogin();
							} 
							
							while(true){
								try{
									recivePacket(); 
								}catch(EOFException  e){									
									e.printStackTrace();
									break;
								}
							}
						}catch (InterruptedException e) {
							// TODO Auto-generated catch block
								e.printStackTrace();
								break;
						}catch(Exception e ){
							e.printStackTrace();
						}// end of try		 
							
							sleep(1000);
					}// end of while
				}catch(Exception e){
					e.printStackTrace();
				}		
			}
		};
		
		connectThread.start();
	}

	public abstract void reLogin();
	
	public Socket getSocket(){
		return this.socket;
	}

	public  void sendPacket(byte[] packet){
		try{
			this.sendBuffer = packet;
			outputStream.write(packet);
			outputStream.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public abstract void recivePacket() throws Exception;

	public Thread getConnectThread() {
		return connectThread;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	

}
