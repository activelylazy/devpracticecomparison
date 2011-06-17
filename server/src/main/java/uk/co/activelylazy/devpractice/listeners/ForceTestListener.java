package uk.co.activelylazy.devpractice.listeners;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import uk.co.activelylazy.devpractice.ParticipantRegistry;
import uk.co.activelylazy.devpractice.Response;
import uk.co.activelylazy.devpractice.TaskRunner;

public class ForceTestListener implements RequestListener {
	
	private final ParticipantRegistry participants;

	public ForceTestListener(ParticipantRegistry participants) {
		this.participants = participants;
	}

	@Override
	public Response request(HttpServletRequest request) throws IOException {
		String client = request.getParameter("client");
		int iteration = Integer.parseInt(request.getParameter("iteration"));
		int text = 0;
		if (request.getParameter("text") != null) {
			text = Integer.parseInt(request.getParameter("text"));
		}
		
		TaskRunner theClient = participants.getParticipantByEndpoint(client);
		if (theClient == null) {
			throw new NullPointerException("No such client "+client);
		}
		theClient.executeTask(iteration, text);
		
		return Response.plainText("OK");
	}

}
