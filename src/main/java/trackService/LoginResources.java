package trackService;

import javax.ws.rs.FormParam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cloudbean.model.Login;
import com.cloudbean.network.MsgEventHandler;
import com.cloudbean.trackme.TrackApp;


@Path("/login")
public class LoginResources {
	
	
	   @POST
	   @Path("login")
	   @Produces(MediaType.APPLICATION_JSON)
	   public Login checkLogin(@FormParam("username") String username,
	                            @FormParam("password") String password) {

		   System.out.println(username);
		   
		   if(username!=null&&password!=null){
			   MsgEventHandler.sLogin(username, password);
			   MsgEventHandler.c_sLogin(username, password);
		   }

//	       while(TrackApp.login==null){
//	    	  
//	       }
		   try{
			    Thread.currentThread().sleep(1000);
			}catch(InterruptedException ie){
			    ie.printStackTrace();
			}
		   
		   if(TrackApp.login!=null){
			   TrackApp.curUsername = username;
			   TrackApp.curPassword = password;
		   }
	       
	       return TrackApp.login;
	   }

}
