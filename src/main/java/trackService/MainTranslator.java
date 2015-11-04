package trackService;

import java.util.HashMap;
import java.util.Map;

import com.cloudbean.trackme.TrackAppClient;

public class MainTranslator {
	public Map<String, TrackAppClient> mobClientSet;
	
	public MainTranslator(){
		mobClientSet = new HashMap<String, TrackAppClient>();
	}
	
	public void putTrackAppClient(String name){
		mobClientSet.put(name, new TrackAppClient(name));
	}
	
	public TrackAppClient getTrackAppClient(String name){
		return mobClientSet.get(name);
	}
	
	public boolean removeTrackAppClient(String name){
		try {
			mobClientSet.remove(name);
		}catch(Error e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
