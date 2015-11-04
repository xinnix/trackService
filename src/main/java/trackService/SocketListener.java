package trackService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cloudbean.trackerUtil.SessionIdentifierGenerator;
import com.cloudbean.trackme.TrackAppClient;
import com.wilddog.client.Wilddog;

import trackService.MainTranslator;

public class SocketListener implements ServletContextListener{

	public static MainTranslator mainTranslator = new MainTranslator();
		
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {		
		try{
			// iterate the mainTranslator MapSet to stop every connection.
			for(TrackAppClient client: mainTranslator.mobClientSet.values()){
				client.getNa().interrupt();
				client.getCna().interrupt();
				// closing na  socket.
				System.out.println("closing na  socket.");
				client.getNa().getSocket().close();
				// closing cna  socket.
				client.getCna().getSocket().close();
			}
		}catch(Exception e){
			
		}
	}
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// the whole system entry point for the web server.
		// web server 
		
		System.out.println("JAVIS, let's rock and roll");
	}
	
}
