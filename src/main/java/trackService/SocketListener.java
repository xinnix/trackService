package trackService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cloudbean.network.CNetworkAdapter;
import com.cloudbean.network.HeartBeat;
import com.cloudbean.network.MsgEventHandler;
import com.cloudbean.network.NetworkAdapter;
import com.cloudbean.trackme.TrackApp;



public class SocketListener implements ServletContextListener{
	public NetworkAdapter na;
	public CNetworkAdapter cna;
	public HeartBeat hb;	
	private String dip;
	private String cip;
	private int dport;
	private int cport;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try{
			na.interrupt();
			cna.interrupt();
			na.socket.close();
			cna.socket.close();
		}catch(Exception e){
			
		}
		
		
	}
	private String[] decodeAddr(String addr){
		return addr.split(":");
		
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		String[] d = decodeAddr(TrackApp.dServerAddr);
		String[] c = decodeAddr(TrackApp.cServerAddr);
		
		dip = d[0];
		dport = Integer.parseInt(d[1]);
		
		cip = c[0];
		cport = Integer.parseInt(c[1]);
		
		na = new NetworkAdapter(dip,dport);
		cna = new CNetworkAdapter(cip,cport);
		hb = new HeartBeat();
		MsgEventHandler.config(na, cna);
		System.out.println("started");
	}
	
	public void hreatBeat(){
		if(hb.isInterrupted())
		hb.start();	
	}

}
