package trackService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;



import com.cloudbean.network.HeartBeat;
//import com.cloudbean.network.CNetworkAdapter;
//import com.cloudbean.network.HeartBeat;
//import com.cloudbean.network.MsgEventHandler;
//import com.cloudbean.network.NetworkAdapter;
//import com.cloudbean.trackme.TrackAppClient;
import trackService.MainTranslator;


public class SocketListener implements ServletContextListener{

	
	public static MainTranslator mainTranslator = new MainTranslator();
		
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try{
			// TODO iterate the mainTranslator Mapset to stop every connnet.
//			na.interrupt();
//			cna.interrupt();
//			na.socket.close();
//			cna.socket.close();
		}catch(Exception e){
			
		}
		
		
	}
	private String[] decodeAddr(String addr){
		return addr.split(":");
		
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO iterate all the TrackAppClient in the mainTranslator Mapset
//		String[] d = decodeAddr(TrackApp.dServerAddr);
//		String[] c = decodeAddr(TrackApp.cServerAddr);
//		
//		dip = d[0];
//		dport = Integer.parseInt(d[1]);
//		
//		cip = c[0];
//		cport = Integer.parseInt(c[1]);
		
//		na = new NetworkAdapter(dip,dport);
//		cna = new CNetworkAdapter(cip,cport);
//		hb = new HeartBeat();
//		hb.setHandler(handler);
//		MsgEventHandler.config(na, cna);
		System.out.println("started");
	}
	
//	public void hreatBeat(){
//		if(hb.isInterrupted())
//		hb.start();	
//	}

}
