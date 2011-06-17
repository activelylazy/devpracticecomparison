package uk.co.activelylazy.devpractice;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import uk.co.activelylazy.devpractice.DevPracticeClient.Factory;

public class RegistrationService {
	private final ParticipantRegistry participants;
	private final Factory clientFactory;

	public RegistrationService(ParticipantRegistry participants, DevPracticeClient.Factory clientFactory) {
		this.participants = participants;
		this.clientFactory = clientFactory;
	}

	public Response register(RegistrationRequest request) {
		if (request.getEndpoint() == null || "".equals(request.getEndpoint())) {
			return Response.plainText("Error - please pass parameter 'endpoint', which should be a http://.../ URL");
		}
		TaskRunner runner = participants.getParticipant(request.getEndpoint());
		if (request.getGroupName() == null || !participants.isValidGroup(request.getGroupName())) {
			if (runner != null) {
				runner.close();
				participants.removeParticipant(request.getEndpoint());
			}
			return Response.plainText("Error - please pass parameter 'group', which should be one of " + 
				StringUtils.join(participants.getGroupNames(), ", "));
		}

		if (runner == null) {
			DevPracticeClient client = clientFactory.create(request.getEndpoint());
			runner = new TaskRunner(client, request.getGroupName());
		} else {
			runner.update(request.getGroupName());
		}
		
		try {
			runner.sendStatus("registered");
		} catch (IOException e){
			return Response.plainText("Failed to register client: "+e.getMessage());
		}
		participants.addParticipant(request.getEndpoint(), runner);
		if (request.isRunTests()) { 
			runner.start();
		}

		return Response.plainText("OK");
	}

}
