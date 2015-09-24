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
import com.cloudbean.trackme.TrackAppClient;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@Path("/track")
public class TrackResources {

	@POST
	@Path("track")
	@Produces("text/plain")
	public String getTrackList(
			@FormParam("username") String username,
			@FormParam("carId") int carId,
			@FormParam("start_time") String sdate,
			@FormParam("end_time") String edate) {

		TrackAppClient appClient = SocketListener.mainTranslator.getTrackAppClient(username);

		if(carId>0
				&sdate!=null
				&edate!=null){
			appClient.getNa().sendGetCarTrackCmd(carId, sdate, edate);
			// return a wilddog ref not a tracklist
			String wdRef = "" + username + "/" + carId + sdate + edate;
			return wdRef;
		} 

		return null;
	}

}
