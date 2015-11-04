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
import com.cloudbean.trackerUtil.SessionIdentifierGenerator;
import com.cloudbean.trackme.TrackAppClient;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@Path("/track")
public class TrackResources {

	@POST
	@Path("track")
	@Produces("text/plain")
	public String getTrackList(
			@FormParam("username") String username,
			@FormParam("carid") int carId,
			@FormParam("sdate") String start_date,
			@FormParam("edate") String end_date) {

		TrackAppClient appClient = SocketListener.mainTranslator.getTrackAppClient(username);

		if(carId >0 && start_date!=null && end_date!=null){
			appClient.getNa().sendGetCarTrackCmd(carId, start_date, end_date);
			// return a wilddog ref not a tracklist
			String wdRef = "track/" + carId + "/" + SessionIdentifierGenerator.nextSessionId();
			appClient.setTrackListHashString(wdRef);
			return username + "/" + wdRef;
		} 

		return null;
	}

}
