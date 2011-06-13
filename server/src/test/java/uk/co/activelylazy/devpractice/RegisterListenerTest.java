package uk.co.activelylazy.devpractice;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;


public class RegisterListenerTest {

	private Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
	
	@Test public void
	registers_client() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final String endpoint = "http://endpoint.example.com/";
		final DevPracticeClient.Factory clientFactory = context.mock(DevPracticeClient.Factory.class);
		final DevPracticeClient client = context.mock(DevPracticeClient.class);
		RegisterListener listener = new RegisterListener(participants, clientFactory);
		
		context.checking(new Expectations() {{
			oneOf(request).getParameter("endpoint"); will(returnValue(endpoint));
			oneOf(request).getParameter("runTests"); will(returnValue(null));
			oneOf(participants).addParticipant(with(endpoint), with(any(TaskRunner.class)));
			oneOf(clientFactory).create(endpoint); will(returnValue(client));
			oneOf(client).sendStatus("registered");
		}});
		
		String responseText = listener.request(request);
		
		assertThat(responseText, is("OK"));
		context.assertIsSatisfied();
	}
	
	@Test public void
	failure_to_connect_to_client_gives_error_in_response() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final String endpoint = "http://endpoint.example.com/";
		final DevPracticeClient.Factory clientFactory = context.mock(DevPracticeClient.Factory.class);
		final DevPracticeClient client = context.mock(DevPracticeClient.class);
		RegisterListener listener = new RegisterListener(participants, clientFactory);
		
		context.checking(new Expectations() {{
			oneOf(request).getParameter("endpoint"); will(returnValue(endpoint));
			oneOf(clientFactory).create(endpoint); will(returnValue(client));
			oneOf(client).sendStatus("registered"); will(throwException(new IOException("Cannot connect")));
		}});
		
		String responseText = listener.request(request);
		
		assertThat(responseText, is("Failed to register client: Cannot connect"));
		context.assertIsSatisfied();
	}
}
