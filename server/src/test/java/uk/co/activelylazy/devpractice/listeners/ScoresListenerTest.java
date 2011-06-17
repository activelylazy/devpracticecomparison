package uk.co.activelylazy.devpractice.listeners;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import uk.co.activelylazy.devpractice.ParticipantRegistry;
import uk.co.activelylazy.devpractice.ResponseMatcher;
import uk.co.activelylazy.devpractice.TaskRunner;
import uk.co.activelylazy.devpractice.listeners.ScoresListener;


public class ScoresListenerTest {
	private Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
	
	@Test public void
	retrieves_scores_for_a_client() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final TaskRunner participant = context.mock(TaskRunner.class);
		final String endpoint = "http://endpoint.example.com/";
		final String groupName = "TDD";
		ScoresListener listener = new ScoresListener(participants);
		
		context.checking(new Expectations() {{
			oneOf(participants).getParticipants(); will(returnValue(Arrays.asList(participant)));
			oneOf(participant).getScore(); will(returnValue(0));
			oneOf(participant).getEndpoint(); will(returnValue(endpoint));
			oneOf(participant).getGroupName(); will(returnValue(groupName));
		}});
		
		assertThat(listener.request(request), 
				ResponseMatcher.json().with_content(is("{\"clients\":[{\"endpoint\":\""+endpoint+"\",\"group\":\"TDD\",\"score\":0}]}")));
		context.assertIsSatisfied();
	}
}
