package com.cloudbean.network;

public class HeartBeat extends Thread{

	private MsgEventHandler handler;
	final long timeInterval = 50000;
	
	public void setHandler (MsgEventHandler handler){
		this.handler = handler;
	}
	@Override
	public void run() {
		while (true){
			this.handler.sHeartBeat();
			
			try {  
                Thread.sleep(timeInterval);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
		}
		
		
	}
	

}
