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
	   @Produces(MediaType.APPLICATION_JSON)
	   public Login checkLogin(@FormParam("username") String username,
	                            @FormParam("password") String password) {

		   	   
		   if(username!=null&&password!=null){
			   System.out.println(username);
			   TrackAppClient appClient = null;	
			    appClient = SocketListener.mainTranslator.getTrackAppClient(username);
			   
			   if(appClient!=null){
				   SocketListener.mainTranslator.putTrackAppClient(username);
				   appClient = SocketListener.mainTranslator.getTrackAppClient(username);
			   }
			   
			   
			   appClient.getHandler().sLogin(username, password);
			   appClient.getHandler().c_sLogin(username, password);
			   

			   try{
				    Thread.currentThread().sleep(1000);
				}catch(InterruptedException ie){
				    ie.printStackTrace();
				}
			   
			   if(appClient.getLogin()!=null){
				   appClient.setCurUsername(username);
				   appClient.setCurPassword(password);
				 
			   }
		       
		       return appClient.getLogin();
		   }

		   return null;
	   }

}
