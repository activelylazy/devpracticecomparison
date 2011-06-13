package uk.co.activelylazy.devpractice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

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
	public String request(HttpServletRequest request) throws IOException {
		String endpoint = request.getParameter("endpoint");
		
		DevPracticeClient client = clientFactory.create(endpoint);
		TaskRunner runner = new TaskRunner(client);
		try {
			client.sendStatus("registered");
		} catch (IOException e){
			return "Failed to register client: "+e.getMessage();
		}
		participants.addParticipant(endpoint, runner);
		if (request.getParameter("runTests") == null) { 
			runner.start();
		}

		return "OK";
	}

}
