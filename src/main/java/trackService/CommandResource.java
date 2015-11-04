package trackService;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.cloudbean.trackme.TrackAppClient;

import trackService.MainTranslator;

@Path("/cmd")
public class CommandResource {
	TrackAppClient appClient;
	// ��������
	@POST
	@Path("set_defense")
	@Produces("text/plain")
	public String setDefense(
			@FormParam("username") String username,
			@FormParam("devid") String devid,
			@FormParam("ip") String ip) {
		appClient = SocketListener.mainTranslator.getTrackAppClient(username);
		if(appClient == null) {
			return "fail";
		}
		// this.cna.
		appClient.getCna().sendSetDef(devid, ip, "01");
		return "ok";
	}
	
	// ��������
	@POST
	@Path("cancel_defense")
	@Produces("text/plain")
	public String cancelDefense(
			@FormParam("username") String username,
			@FormParam("devid") String devid,
			@FormParam("ip") String ip) {
		appClient = SocketListener.mainTranslator.getTrackAppClient(username);
		if(appClient == null) {
			return "fail";
		}
		// this.cna.
		appClient.getCna().sendSetDef(devid, ip, "00");
		return "ok";
	
	}
	
	// ��������澯
	@POST
	@Path("set_power_supply")
	@Produces("text/plain")
	public String setPowerSupply(
			@FormParam("username") String username,
			@FormParam("devid") String devid,
			@FormParam("ip") String ip) {
		appClient = SocketListener.mainTranslator.getTrackAppClient(username);
		if(appClient == null) {
			return "fail";
		}
		// this.cna.
		appClient.getCna().sendExpandCommand(devid, ip, "01000000010000000101");
		return "ok";
	}
	
	// ȡ������澯
	@POST
	@Path("cancel_power_supply")
	@Produces("text/plain")
	public String cancelPowerSupply(
			@FormParam("username") String username,
			@FormParam("devid") String devid,
			@FormParam("ip") String ip) {
		
		appClient = SocketListener.mainTranslator.getTrackAppClient(username);
		if(appClient == null) {
			return "fail";
		}
		// this.cna.
		appClient.getCna().sendExpandCommand(devid, ip, "01000000010000000001");
		return "ok";
	}
	
	//������绰�ظ���γ��
	@POST
	@Path("set_return_sms_after_call")
	@Produces("text/plain")
	public String setReturnSms(
			@FormParam("username") String username,
			@FormParam("devid") String devid,
			@FormParam("ip") String ip) {
		
		appClient = SocketListener.mainTranslator.getTrackAppClient(username);
		if(appClient == null) {
			return "fail";
		}
		// this.cna.
		appClient.getCna().sendExpandCommand(devid, ip, "01000000010000000001");
		return "ok";
	}
	
	// �رմ�绰�ظ���γ��
	@POST
	@Path("cancel_return_sms_after_call")
	@Produces("text/plain")
	public String cancelReturnSms(
			@FormParam("username") String username,
			@FormParam("devid") String devid,
			@FormParam("ip") String ip) {
		
		appClient = SocketListener.mainTranslator.getTrackAppClient(username);
		if(appClient == null) {
			return "fail";
		}
		// this.cna.
		appClient.getCna().sendExpandCommand(devid, ip, "00000000010000000001");
		return "ok";
	}
		

}


