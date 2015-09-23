package trackService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cloudbean.model.Track;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@Path("/track")
public class TrackResources {
	
		@POST
	   @Path("track")
	   @Produces(MediaType.APPLICATION_JSON)
	   public List<Track> checkLogin(
			    @FormParam("username") String username,
			    @FormParam("carId") int carid,
				@FormParam("start_time") String sdate,
				@FormParam("end_time") String edate) {

		   if(sdate!=null&edate!=null){
			   SocketListener.mainTranslator.getTrackAppClient(username).getHandler().sGetCarTrack(carid, sdate, edate);
		   }

		   try{
			    Thread.currentThread().sleep(4000);
			}catch(InterruptedException ie){
			    ie.printStackTrace();
			}
		   
	       return Arrays.asList(SocketListener.mainTranslator.getTrackAppClient(username).getCurTrackList());
	   }

}
