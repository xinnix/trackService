package trackService;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cloudbean.model.Login;
import com.cloudbean.network.MsgEventHandler;
import com.cloudbean.trackme.TrackAppClient;

import trackService.MainTranslator;

@Path("/login")
public class LoginResources {


	@POST
	@Path("login")
	@Produces("text/plain")
	public String checkLogin(
			@FormParam("username") String username,
			@FormParam("password") String password) {

		if(username!=null&&password!=null){
			System.out.println(username + " is connecting...");
			
			TrackAppClient appClient = null;	
			appClient = SocketListener.mainTranslator.getTrackAppClient(username);

			if(appClient == null){
				System.out.println(username + " start to create the connection...");
				SocketListener.mainTranslator.putTrackAppClient(username);
				appClient = SocketListener.mainTranslator.getTrackAppClient(username);
				appClient.getNa().sendLoginCmd(username, password);
				appClient.getCna().sendLoginCmd(username, password);				
			} else {
				System.out.println(username + " has connected, return the ref directly.");
			}
			
			//  return a wilddog ref not a tracklist
			return username+"/login";				
		}
		return null;
	}
}
