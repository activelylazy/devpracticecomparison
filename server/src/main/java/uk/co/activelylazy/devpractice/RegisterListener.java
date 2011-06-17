package uk.co.activelylazy.devpractice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import uk.co.activelylazy.devpractice.DevPracticeClient.Factory;

public class RegisterListener implements RequestListener {

	private final ParticipantRegistry participants;
	private final Factory clientFactory;

	public RegisterListener(ParticipantRegistry participants,
							DevPracticeClient.Factory clientFactory) {
		this.participants = participants;
		this.clientFactory = clientFactory;
	}
	
	@Override
	public Response request(HttpServletRequest request) throws IOException {
		String endpoint = request.getParameter("endpoint");
		if (endpoint == null || "".equals(endpoint)) {
			return Response.plainText("Error - please pass parameter 'endpoint', which should be a http://.../ URL");
		}
		TaskRunner runner = participants.getParticipant(endpoint);
		String groupName = request.getParameter("group");
		if (groupName == null || !participants.isValidGroup(groupName)) {
			if (runner != null) {
				runner.close();
				participants.removeParticipant(endpoint);
			}
			return Response.plainText("Error - please pass parameter 'group', which should be one of " + 
				StringUtils.join(participants.getGroupNames(), ", "));
		}
		
		if (runner == null) {
			DevPracticeClient client = clientFactory.create(endpoint);
			runner = new TaskRunner(client, groupName);
		} else {
			runner.update(groupName);
		}
		
		try {
			runner.sendStatus("registered");
		} catch (IOException e){
			return Response.plainText("Failed to register client: "+e.getMessage());
		}
		participants.addParticipant(endpoint, runner);
		if (request.getParameter("runTests") == null) { 
			runner.start();
		}

		return Response.plainText("OK");
	}

}
