package com.cloudbean.network;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.wilddog.client.Wilddog;

public abstract class BaseNetworkAdapter extends Thread{
	public MsgEventHandler handler;
	private  Socket socket;
	private  OutputStream outputStream;
	public  InputStream inputStream;
	public  Wilddog wdRootRef;	
	public  DataInputStream dis; 
	public byte[] sendBuffer;
	public byte[] recieveBuffer = new byte[20000];	
	private String serverIP = null;
	private int port = 0;

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
		new Thread () {
			public void run(){				
				try{
					socket = new Socket(InetAddress.getByName(serverIP),port);
					outputStream = socket.getOutputStream();
					inputStream = socket.getInputStream();
					dis =  new DataInputStream((new BufferedInputStream(inputStream)));

					while(true){
						try{
							recivePacket(); 
						}catch(Exception  e){
							e.printStackTrace();
							break;
						} 

						try {
							sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} 
					}
				}catch(Exception e ){
					e.printStackTrace();						
				} finally {  						  
					try  
					{  			  
						inputStream.close();			  
						outputStream.close();			  
					}  
					catch (IOException e)  
					{  
						e.printStackTrace();  			  
					}  
				}  // end of try		
			}
		}.start();
	}

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

}
