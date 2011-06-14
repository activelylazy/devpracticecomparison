package uk.co.activelylazy.devpractice;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;


public class ScoresListenerTest {
	private Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
	
	@Test public void
	retrieves_scores_for_a_client() throws IOException {
		final HttpServletRequest request = context.mock(HttpServletRequest.class);
		final ParticipantRegistry participants = context.mock(ParticipantRegistry.class);
		final TaskRunner participant = context.mock(TaskRunner.class);
		final ParticipantGroup group = context.mock(ParticipantGroup.class);
		final String endpoint = "http://endpoint.example.com/";
		ScoresListener listener = new ScoresListener(participants);
		
		context.checking(new Expectations() {{
			oneOf(participants).getParticipantGroups(); will(returnValue(Arrays.asList(group)));
			allowing(group).getParticipants(); will(returnValue(Arrays.asList(participant)));
			allowing(group).getGroupName(); will(returnValue("TDD"));
			oneOf(participant).getScore(); will(returnValue(0));
			oneOf(participant).getEndpoint(); will(returnValue(endpoint));
		}});
		
		String response = listener.request(request);

		assertThat(response, is("{\"groups\":[{\"clients\":[{\"endpoint\":\""+endpoint+"\",\"score\":0}],\"name\":\"TDD\"}]}"));
		context.assertIsSatisfied();
	}
}
