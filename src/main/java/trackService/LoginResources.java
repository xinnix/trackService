package trackService;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cloudbean.model.Login;
import com.cloudbean.network.MsgEventHandler;
import com.cloudbean.trackerUtil.SessionIdentifierGenerator;
import com.cloudbean.trackme.TrackAppClient;

import trackService.MainTranslator;

@Path("/auth")
public class LoginResources {
	@GET
	@Path("/exit/{username}")	
	public String leave(@PathParam("username") String username){
		TrackAppClient appClient = SocketListener.mainTranslator.getTrackAppClient(username);
		if(appClient != null){
			appClient.decConnectedConut();
			if(appClient.getConnectedConut() == 0){
				// destory appClient 
				System.out.println("close the socket connect");
				appClient.stopSocketConnect();
				
				System.out.println("remove the client form mainTranslator");
				SocketListener.mainTranslator.removeTrackAppClient(username);
				return "closed";
			}
			return "connected";
		}
		
		return "offline";		
	}

	@POST
	@Path("login")
	@Produces("text/plain")
	public String checkLogin(
			@FormParam("username") String username,
			@FormParam("password") String password) {

		if(username!=null && password!=null){
			System.out.println(username + " is connecting...");
			
			TrackAppClient appClient = null;	
			appClient = SocketListener.mainTranslator.getTrackAppClient(username);
			if (appClient != null) {
				// verify username / password
				if(appClient.getNa().getPassword() !=null && 
						appClient.getNa().getPassword().equals(password)){
					appClient.incConnectedConut();  // if auth succ, increase the count of connect instances.
				} else {
					return null;
				}
			}
			
			if(appClient == null){
				System.out.println(username + " start to create the connection...");
				SocketListener.mainTranslator.putTrackAppClient(username);
				appClient = SocketListener.mainTranslator.getTrackAppClient(username);
				appClient.incConnectedConut();
				appClient.setSessionID(SessionIdentifierGenerator.nextSessionId());
				
				// wait for the appClient init with na and nac's threads, 
				// before you can read and write with the socket.
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				appClient.getNa().sendLoginCmd(username, password);
				appClient.getCna().sendLoginCmd(username, password);				
			} else {
				System.out.println(username + " has connected, return the ref directly.");
			}
			
			// return a wilddog ref not a tracklist
			return username + "/login/" + appClient.getSessionID();				
		}
		return null;
	}
}
