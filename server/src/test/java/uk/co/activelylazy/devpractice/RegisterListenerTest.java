package uk.co.activelylazy.devpractice;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import com.google.common.collect.Lists;


public class RegisterListenerTest {

	private Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
	
	@Test public void
	registers_client() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final String endpoint = "http://endpoint.example.com/";
		final DevPracticeClient.Factory clientFactory = context.mock(DevPracticeClient.Factory.class);
		final DevPracticeClient client = context.mock(DevPracticeClient.class);
		final String groupName = "TDD";
		RegisterListener listener = new RegisterListener(participants, clientFactory);
		
		context.checking(new Expectations() {{
			oneOf(request).getParameter("endpoint"); will(returnValue(endpoint));
			oneOf(request).getParameter("runTests"); will(returnValue(null));
			oneOf(request).getParameter("group"); will(returnValue(groupName));
			oneOf(participants).getParticipant(endpoint); will(returnValue(null));
			oneOf(participants).addParticipant(with(endpoint), with(any(TaskRunner.class)));
			oneOf(participants).isValidGroup(groupName); will(returnValue(true));
			oneOf(clientFactory).create(endpoint); will(returnValue(client));
			oneOf(client).sendStatus("registered");
		}});
		
		assertThat(listener.request(request), ResponseMatcher.plain_text().with_content(is("OK")));
		context.assertIsSatisfied();
	}
	
	@Test public void
	failure_to_connect_to_client_gives_error_in_response() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final String endpoint = "http://endpoint.example.com/";
		final DevPracticeClient.Factory clientFactory = context.mock(DevPracticeClient.Factory.class);
		final DevPracticeClient client = context.mock(DevPracticeClient.class);
		final String groupName = "none";
		RegisterListener listener = new RegisterListener(participants, clientFactory);
		
		context.checking(new Expectations() {{
			oneOf(request).getParameter("endpoint"); will(returnValue(endpoint));
			oneOf(request).getParameter("group"); will(returnValue(groupName));
			oneOf(request).getParameter("runTests"); will(returnValue(null));
			oneOf(participants).getParticipant(endpoint); will(returnValue(null));
			oneOf(participants).isValidGroup(groupName); will(returnValue(true));
			oneOf(clientFactory).create(endpoint); will(returnValue(client));
			oneOf(client).sendStatus("registered"); will(throwException(new IOException("Cannot connect")));
		}});
		
		assertThat(listener.request(request), 
				ResponseMatcher.plain_text().with_content(is("Failed to register client: Cannot connect")));
		context.assertIsSatisfied();
	}
	
	@Test public void
	not_providing_endpoint_returns_useful_error() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final DevPracticeClient.Factory clientFactory = context.mock(DevPracticeClient.Factory.class);
		RegisterListener listener = new RegisterListener(participants, clientFactory);
		
		context.checking(new Expectations() {{
			oneOf(request).getParameter("endpoint"); will(returnValue(null));
			oneOf(request).getParameter("group"); will(returnValue("some group"));
			oneOf(request).getParameter("runTests"); will(returnValue(null));
		}});
		
		assertThat(listener.request(request), 
				ResponseMatcher.plain_text().with_content(is("Error - please pass parameter 'endpoint', which should be a http://.../ URL")));
		context.assertIsSatisfied();
	}
	
	@Test public void
	not_providing_group_returns_useful_error() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final DevPracticeClient.Factory clientFactory = context.mock(DevPracticeClient.Factory.class);
		final String endpoint = "http://endpoint.example.com/";
		RegisterListener listener = new RegisterListener(participants, clientFactory);
		
		context.checking(new Expectations() {{
			oneOf(request).getParameter("endpoint"); will(returnValue(endpoint));
			oneOf(request).getParameter("group"); will(returnValue(null));
			oneOf(request).getParameter("runTests"); will(returnValue(null));
			oneOf(participants).getParticipant(endpoint); will(returnValue(null));
			oneOf(participants).getGroupNames(); will(returnValue(Lists.newArrayList("TDD", "NoTDD")));
		}});
		
		assertThat(listener.request(request), 
				ResponseMatcher.plain_text().with_content(is("Error - please pass parameter 'group', which should be one of TDD, NoTDD")));
		context.assertIsSatisfied();
	}
	
	@Test public void
	registering_with_same_endpoint_updates_registration() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final String endpoint = "http://endpoint.example.com/";
		final DevPracticeClient.Factory clientFactory = context.mock(DevPracticeClient.Factory.class);
		final TaskRunner participant = context.mock(TaskRunner.class);
		final String groupName = "TDD";
		RegisterListener listener = new RegisterListener(participants, clientFactory);
		
		context.checking(new Expectations() {{
			oneOf(request).getParameter("endpoint"); will(returnValue(endpoint));
			oneOf(request).getParameter("runTests"); will(returnValue(null));
			oneOf(request).getParameter("group"); will(returnValue(groupName));
			oneOf(participants).getParticipant(endpoint); will(returnValue(participant));
			oneOf(participants).isValidGroup(groupName); will(returnValue(true));
			oneOf(participants).addParticipant(endpoint, participant);
			oneOf(participant).update(groupName);
			oneOf(participant).sendStatus("registered");
			oneOf(participant).start();
		}});
		
		assertThat(listener.request(request), ResponseMatcher.plain_text().with_content(is("OK")));
		context.assertIsSatisfied();
	}
	
	@Test public void
	registering_with_invalid_group_gives_useful_error() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final DevPracticeClient.Factory clientFactory = context.mock(DevPracticeClient.Factory.class);
		final String endpoint = "http://endpoint.example.com/";
		RegisterListener listener = new RegisterListener(participants, clientFactory);
		
		context.checking(new Expectations() {{
			oneOf(request).getParameter("endpoint"); will(returnValue(endpoint));
			oneOf(request).getParameter("group"); will(returnValue("not a valid group"));
			oneOf(request).getParameter("runTests"); will(returnValue(null));
			oneOf(participants).getParticipant(endpoint); will(returnValue(null));
			oneOf(participants).getGroupNames(); will(returnValue(Lists.newArrayList("TDD", "NoTDD")));
			oneOf(participants).isValidGroup("not a valid group"); will(returnValue(false));
		}});
		
		assertThat(listener.request(request), 
				ResponseMatcher.plain_text().with_content(is("Error - please pass parameter 'group', which should be one of TDD, NoTDD")));
		context.assertIsSatisfied();
	}
	
	@Test public void
	updating_registration_with_invalid_group_name_unregisters_client() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final String endpoint = "http://endpoint.example.com/";
		final DevPracticeClient.Factory clientFactory = context.mock(DevPracticeClient.Factory.class);
		final TaskRunner participant = context.mock(TaskRunner.class);
		final String groupName = "Invalid group";
		RegisterListener listener = new RegisterListener(participants, clientFactory);
		
		context.checking(new Expectations() {{
			oneOf(request).getParameter("endpoint"); will(returnValue(endpoint));
			oneOf(request).getParameter("group"); will(returnValue(groupName));
			oneOf(request).getParameter("runTests"); will(returnValue(null));
			oneOf(participants).getParticipant(endpoint); will(returnValue(participant));
			oneOf(participants).isValidGroup(groupName); will(returnValue(false));
			oneOf(participants).removeParticipant(endpoint);
			oneOf(participants).getGroupNames(); will(returnValue(Lists.newArrayList("TDD", "NoTDD")));
			oneOf(participant).close();
		}});
		
		assertThat(listener.request(request), 
				ResponseMatcher.plain_text().with_content(is("Error - please pass parameter 'group', which should be one of TDD, NoTDD")));
		context.assertIsSatisfied();
	}
}
