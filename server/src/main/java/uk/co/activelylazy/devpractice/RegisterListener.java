package uk.co.activelylazy.devpractice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

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
