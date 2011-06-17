package uk.co.activelylazy.devpractice.listeners;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import uk.co.activelylazy.devpractice.DevPracticeClient;
import uk.co.activelylazy.devpractice.ParticipantRegistry;
import uk.co.activelylazy.devpractice.RegistrationRequest;
import uk.co.activelylazy.devpractice.RegistrationService;
import uk.co.activelylazy.devpractice.Response;
import uk.co.activelylazy.devpractice.DevPracticeClient.Factory;

public class RegisterListener implements RequestListener {

	private RegistrationService registrationService;

	public RegisterListener(ParticipantRegistry participants,
							DevPracticeClient.Factory clientFactory) {
		this.registrationService = new RegistrationService(participants, clientFactory);
	}
	
	@Override
	public Response request(HttpServletRequest request) throws IOException {
		RegistrationRequest registrationRequest = new RegistrationRequest(request);
		return registrationService.register(registrationRequest);
	}

}
