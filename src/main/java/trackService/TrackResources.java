package trackService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cloudbean.model.Login;
import com.cloudbean.model.Track;
import com.cloudbean.network.MsgEventHandler;
import com.cloudbean.trackme.TrackApp;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@Path("/")
public class TrackResources {
	
		@POST
	   @Path("track")
	   @Produces(MediaType.APPLICATION_JSON)
	   public List<Track> checkLogin(@FormParam("carid") int carid,
	                            @FormParam("sdate") String sdate,
	                            @FormParam("edate") String edate) {

		   
//		   List TrackList = new ArrayList();
		   if(sdate!=null&edate!=null){
			   MsgEventHandler.sGetCarTrack(carid, sdate, edate);
		   }

//	       while(TrackApp.login==null){
//	    	  
//	       }
		   try{
			    Thread.currentThread().sleep(2000);
			}catch(InterruptedException ie){
			    ie.printStackTrace();
			}
		   
	       
	       return Arrays.asList(TrackApp.curTrackList);
	   }

}
